package com.example.accessingdatarest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@RestController
@RequestMapping("/api/people")
@CrossOrigin(origins = "*") // resuelve el problema de CORS
public class PersonController {

    @Autowired
    private PersonRepository repository;

    private final String ALL_PEOPLE_KEY = "all_people_list";

    // CREATE (POST)
    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        Person saved = repository.save(person); // guarda en la base de datos

        // al crear uno nuevo, la lista cacheada ya no es válida
        Cache.delete(ALL_PEOPLE_KEY);

        return saved;
    }

    // READ (GET) - leer todos los datos
    @GetMapping
    public Iterable<Person> getAllPeople() {
        // 1. intentar obtener de la caché manual
        Iterable<Person> cachedList = (Iterable<Person>) Cache.get(ALL_PEOPLE_KEY);

        if (cachedList != null) {
            System.out.println("Retornando lista desde caché manual...");
            return cachedList;
        }

        // 2. si no está en caché, ir a la DB
        System.out.println("Caché vacía, consultando base de datos...");
        Iterable<Person> peopleFromDb = repository.findAll();

        // 3. guardar en la caché para la próxima vez
        Cache.set(ALL_PEOPLE_KEY, peopleFromDb);

        return peopleFromDb;
    }

    // READ (GET) - Leer por ID
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        String cacheKey = "person_" + id;

        // 1. buscar en la caché
        Person cachedPerson = (Person) Cache.get(cacheKey);
        if (cachedPerson != null) {
            System.out.println("Retornando persona " + id + " desde caché...");
            return ResponseEntity.ok(cachedPerson);
        }

        // 2. buscar en la DB
        return repository.findById(id).map(person -> {
            Cache.set(cacheKey, person); // guardar en caché si se encuentra
            return ResponseEntity.ok(person);
        }).orElse(ResponseEntity.notFound().build());
    }

    // UPDATE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person personDetails) {
        return repository.findById(id).map(person -> {
            // Actualizar campos
            person.setFirstName(personDetails.getFirstName());
            person.setLastName(personDetails.getLastName());
            person.setEmail(personDetails.getEmail());
            person.setPhoneNumber(personDetails.getPhoneNumber());

            Person updated = repository.save(person); // guardar en base de datos

            // ACTUALIZACIÓN DE CACHÉ
            Cache.set("person_" + id, updated); // actualizar objeto individual
            Cache.delete(ALL_PEOPLE_KEY);      // invalidar la lista completa

            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id); // eliminar de la base de datos

            // LIMPIEZA DE CACHÉ
            Cache.delete("person_" + id); // eliminar objeto individual de la caché
            Cache.delete(ALL_PEOPLE_KEY); // invalidar la lista completa

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}