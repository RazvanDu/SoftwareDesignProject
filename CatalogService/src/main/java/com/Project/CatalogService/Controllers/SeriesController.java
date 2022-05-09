package com.Project.CatalogService.Controllers;

import com.Project.CatalogService.Database.Series;
import com.Project.CatalogService.Database.SeriesRepository;
import com.Project.CatalogService.Utilities.ResourceNotFoundException;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

@RestController
@RequestMapping(path="/series")
public class SeriesController {

    @Autowired
    private SeriesRepository seriesRepository;

    @GetMapping(path = "/get")
    public @ResponseBody
    String getAllSeries() {

        return new Gson().toJson(seriesRepository.findAll());

    }

    @GetMapping("/{id}")
    @Timed(value = "getSeriesById.time", description = "Time taken to find a specific series by its id")
    public @ResponseBody
    String getSeriesById(@PathVariable(value = "id") Integer seriesId) throws ResourceNotFoundException {

        Series series = seriesRepository.findById(seriesId).orElseThrow(() ->
                new ResourceNotFoundException("Series not found with id :" + seriesId));

        return new Gson().toJson(ResponseEntity.ok().body(series));

    }

}