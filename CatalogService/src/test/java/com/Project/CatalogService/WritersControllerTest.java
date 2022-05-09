package com.Project.CatalogService;

import com.Project.CatalogService.Controllers.WritersController;
import com.Project.CatalogService.Database.Writer;
import com.Project.CatalogService.Database.WritersRepository;
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

public class WritersControllerTest {

    @Mock
    private WritersRepository writersRepository;

    @InjectMocks
    private final WritersController writersController = new WritersController();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllWritersTest() {
        List<Writer> writers = new ArrayList<>();
        //String name, String type, String logo, String website
        writers.add(new Writer("Name1","MangaTest",1922,1));
        writers.add(new Writer("Name2","MangaTest",1923,2));
        writers.add(new Writer("Name3","MangaTest2",1924,2022));
        writers.add(new Writer("Name4","MangaTest2",1925,2005));

        Mockito.doReturn(writers).when(writersRepository).findAll();
        String actual = writersController.getAllWriters();

        Assert.assertEquals(writers.stream().map(Writer::getName).collect(Collectors.toList()), getNames(actual));
        Mockito.verify(writersRepository).findAll();
    }

    private List<String> getNames(String writers) {
        List<String> writersNames = new ArrayList<>();
        JsonArray writersJson = (JsonArray) new JsonParser().parse(writers);
        writersJson.forEach(j -> writersNames.add(((JsonObject)j).get("name").getAsString()));
        return writersNames;
    }

    @Test
    public void getWriterByIdTest() throws ResourceNotFoundException {
        int id = 1;

        Optional<Writer> writer = Optional.of(new Writer("Name1","MangaTest",1922,1));
        writer.get().setId(id);

        Mockito.doReturn(writer).when(writersRepository).findById(id);

        JsonObject jsonObject = new JsonParser().parse(writersController.getWriterById(id)).getAsJsonObject();//Got String, Convert into JSonObject to get Body, to recreate the Writer
        Writer actual = new Gson().fromJson(jsonObject.get("body"), Writer.class);

        Assert.assertEquals(writer.get().getName(),actual.getName());
        Mockito.verify(writersRepository).findById(id);

    }

    @Test
    public void addWriter() {
        //(String name, String picture, int born, int deceased)
        Writer writer = new Writer("Name1","MangaTest",1922,1);
        Mockito.doReturn(writer).when(writersRepository).save(writer);
        Writer actual = writersRepository.save(writer);

        Assert.assertEquals(writer.getName(),actual.getName());
    }
}