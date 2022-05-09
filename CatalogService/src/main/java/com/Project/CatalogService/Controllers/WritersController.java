package com.Project.CatalogService.Controllers;

import com.Project.CatalogService.Database.WritersRepository;
import com.Project.CatalogService.Database.Writer;
import com.Project.CatalogService.Utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import javax.validation.Valid;

/**
 * The type Writers controller.
 */
@RestController
@RequestMapping(path="/writers")
public class WritersController {

    @Autowired
    private WritersRepository writersRepository;

    /**
     * Gets all writers.
     *
     * @return the all writers
     */
    @GetMapping(path = "/get")
    public @ResponseBody
    String getAllWriters() {

        return new Gson().toJson(writersRepository.findAll());

    }

    /**
     * Gets writer by id.
     *
     * @param writersId the writers id
     * @return the writer by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/{id}")
    public @ResponseBody
    String getWriterById(@PathVariable(value = "id") Integer writersId) throws ResourceNotFoundException {

        Writer writer = writersRepository.findById(writersId).orElseThrow(() ->
                new ResourceNotFoundException("Writers not found with id :" + writersId));

        return new Gson().toJson(ResponseEntity.ok().body(writer));

    }

    /**
     * Add writer writer.
     *
     * @param writer the writer
     * @return the writer
     */
    @PostMapping("/addWriter")
    public Writer addWriter(@Valid @RequestBody Writer writer) {

        return writersRepository.save(writer);

    }

}