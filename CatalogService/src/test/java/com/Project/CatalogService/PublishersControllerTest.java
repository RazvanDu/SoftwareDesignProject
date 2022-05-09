package com.Project.CatalogService;

import com.Project.CatalogService.Controllers.PublishersController;
import com.Project.CatalogService.Database.Publisher;
import com.Project.CatalogService.Database.PublishersRepository;
import com.Project.CatalogService.Utilities.ResourceNotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class PublishersControllerTest {

    @Mock
    private PublishersRepository publisherRepository;

    @InjectMocks
    private final PublishersController publisherController = new PublishersController();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllPublishersTest() {
        List<Publisher> publishers = new ArrayList<>();
        //String name, String type, String logo, String website
        publishers.add(new Publisher("Name1","MangaTest","TEST_LOGO","Test_website.com"));
        publishers.add(new Publisher("Name2","MangaTest","TEST_LOGO2","Test_website.com"));
        publishers.add(new Publisher("Name3","MangaTest2","TEST_LOGO","Test_website.com"));
        publishers.add(new Publisher("Name4","MangaTest2","TEST_LOGO2","Test_website.com"));

        Mockito.doReturn(publishers).when(publisherRepository).findAll();
        String actual = publisherController.getAllPublishers();

        Assert.assertEquals(publishers.stream().map(Publisher::getName).collect(Collectors.toList()), getNames(actual));
        Mockito.verify(publisherRepository).findAll();
    }

    private List<String> getNames(String publishers) {
        List<String> publishersNames = new ArrayList<>();
        JsonArray publishersJson = (JsonArray) new JsonParser().parse(publishers);
        publishersJson.forEach(j -> publishersNames.add(((JsonObject)j).get("name").getAsString()));
        return publishersNames;
    }

    @Test
    public void getPublisherByIdTest() throws ResourceNotFoundException {
        int id = 1;

        Optional<Publisher> publisher = Optional.of(new Publisher("Name1","MangaTest","TEST_LOGO","Test_website.com"));
        publisher.get().setId(id);

        Mockito.doReturn(publisher).when(publisherRepository).findById(id);

        JsonObject jsonObject = new JsonParser().parse(publisherController.getPublisherById(id)).getAsJsonObject();//Got String, Convert into JSonObject to get Body, to recreate the Publisher
        Publisher actual = new Gson().fromJson(jsonObject.get("body"), Publisher.class);

        Assert.assertEquals(publisher.get().getName(),actual.getName());
        Mockito.verify(publisherRepository).findById(id);

    }
}