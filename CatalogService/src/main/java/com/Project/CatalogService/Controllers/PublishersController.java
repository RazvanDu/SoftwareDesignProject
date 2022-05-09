package com.Project.CatalogService.Controllers;

import com.Project.CatalogService.Database.Publisher;
import com.Project.CatalogService.Database.PublishersRepository;
import com.Project.CatalogService.Utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

@RestController
@RequestMapping(path="/publishers")
public class PublishersController {

    @Autowired
    private PublishersRepository publishersRepository;

    @GetMapping(path = "/get")
    public @ResponseBody
    String getAllPublishers() { //Must have @ResponseBody otherwise returns error
        // This returns a JSON or XML with the users
        return new Gson().toJson(publishersRepository.findAll());
    }

    @GetMapping("/{id}")
    public @ResponseBody
    String getPublisherById(@PathVariable(value = "id") Integer publisherId) throws ResourceNotFoundException {
        Publisher publisher = publishersRepository.findById(publisherId).orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id :" + publisherId));
        return new Gson().toJson(ResponseEntity.ok().body(publisher));
    }

}