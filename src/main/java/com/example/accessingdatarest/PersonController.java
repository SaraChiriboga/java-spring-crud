package com.example.accessingdatarest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@RestController
@RequestMapping("/api/people")
@CrossOrigin(origins = "*") // esto resuelve el problema de CORS para desarrollo
public class PersonController {

    @Autowired
    private PersonRepository repository;

    // CREATE (POST)
    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        // guardar en la base de datos
        return repository.save(person);
    }

    // READ (GET)
        // Leer todos los datos
    @GetMapping
    public Iterable<Person> getAllPeople() {
        return repository.findAll();
    }

        // Leer por ID
    @GetMapping("/{id}")
    public Optional<Person> getPersonById(@PathVariable Long id) {
        return repository.findById(id);
    }
    // UPDATE (PATCH)

    // DELETE (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        // Primero verificamos si el registro existe en la base de datos
        if (repository.existsById(id)) {
            repository.deleteById(id);
            // Si se elimina correctamente, devolvemos un estado 204 (No Content)
            return ResponseEntity.noContent().build();
        } else {
            // Si el ID no existe, devolvemos un estado 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }
}