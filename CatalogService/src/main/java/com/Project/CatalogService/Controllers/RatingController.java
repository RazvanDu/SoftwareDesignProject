package com.Project.CatalogService.Controllers;

import com.Project.CatalogService.Database.Rating;
import com.Project.CatalogService.Database.RatingRepository;
import com.Project.CatalogService.Utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

/**
 * The type Rating controller.
 */
@RestController
@RequestMapping(path="/ratings")
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;

    /**
     * Gets all ratings.
     *
     * @return the all ratings
     */
    @GetMapping(path = "/get")
    public @ResponseBody
    String getAllRatings() {

        return new Gson().toJson(ratingRepository.findAll());

    }

    /**
     * Gets rating by id.
     *
     * @param ratingId the rating id
     * @return the rating by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/{id}")
    public @ResponseBody String getRatingById(@PathVariable(value = "id") Integer ratingId)
            throws ResourceNotFoundException {

        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() ->
                new ResourceNotFoundException("Rating not found with id :" + ratingId));

        return new Gson().toJson(ResponseEntity.ok().body(rating));

    }

}