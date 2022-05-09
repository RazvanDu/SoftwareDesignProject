package com.Project.CatalogService;

import com.Project.CatalogService.Controllers.RatingController;
import com.Project.CatalogService.Database.Rating;
import com.Project.CatalogService.Database.RatingRepository;
import com.Project.CatalogService.Utilities.ResourceNotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class RatingControllerTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private final RatingController ratingController = new RatingController();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllRatingsTest() {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating("Name1",12,"TEST"));
        ratings.add(new Rating("Name2",12,"TEST"));
        ratings.add(new Rating("Name3",13,"TEST"));
        ratings.add(new Rating("Name4",14,"TEST"));

        Mockito.doReturn(ratings).when(ratingRepository).findAll();
        String actual = ratingController.getAllRatings();

        Assert.assertEquals(ratings.stream().map(Rating::getName).collect(Collectors.toList()), getNames(actual));
        Mockito.verify(ratingRepository).findAll();
    }

    private List<String> getNames(String ratings) {
        List<String> ratingsNames = new ArrayList<>();
        JsonArray ratingsJson = (JsonArray) new JsonParser().parse(ratings);
        ratingsJson.forEach(j -> ratingsNames.add(((JsonObject)j).get("name").getAsString()));
        return ratingsNames;
    }

    @Test
    public void getRatingByIdTest() throws ResourceNotFoundException {
        int id = 1;

        Optional<Rating> rating = Optional.of(new Rating("Name1",12,"TEST"));
        rating.get().setId(id);

        Mockito.doReturn(rating).when(ratingRepository).findById(id);

        JsonObject jsonObject = new JsonParser().parse(ratingController.getRatingById(id)).getAsJsonObject();//Got String, Convert into JSonObject to get Body, to recreate the Rating
        Rating actual = new Gson().fromJson(jsonObject.get("body"), Rating.class);

        Assert.assertEquals(rating.get().getName(),actual.getName());
        Mockito.verify(ratingRepository).findById(id);
    }
}