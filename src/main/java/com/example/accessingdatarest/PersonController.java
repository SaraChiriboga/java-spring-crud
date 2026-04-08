package com.example.accessingdatarest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    // UPDATE (PATCH)

    // DELETE (DELETE)
}