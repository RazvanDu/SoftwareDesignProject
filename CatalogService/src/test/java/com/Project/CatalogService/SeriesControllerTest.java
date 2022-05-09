package com.Project.CatalogService;

import com.Project.CatalogService.Controllers.SeriesController;
import com.Project.CatalogService.Database.Series;
import com.Project.CatalogService.Database.SeriesRepository;
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

public class SeriesControllerTest {

    @Mock
    private SeriesRepository serieRepository;

    @InjectMocks
    private final SeriesController serieController = new SeriesController();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllSeriesTest() {
        List<Series> series = new ArrayList<>();
        //String name, String type, String logo, String website
        series.add(new Series("Title1"));
        series.add(new Series("Title2"));
        series.add(new Series("Title3"));
        series.add(new Series("Title4"));

        Mockito.doReturn(series).when(serieRepository).findAll();
        String actual = serieController.getAllSeries();

        Assert.assertEquals(series.stream().map(Series::getTitle).collect(Collectors.toList()), getTitles(actual));
        Mockito.verify(serieRepository).findAll();
    }

    private List<String> getTitles(String series) {
        List<String> seriesTitles = new ArrayList<>();
        JsonArray seriesJson = (JsonArray) new JsonParser().parse(series);
        seriesJson.forEach(j -> seriesTitles.add(((JsonObject)j).get("title").getAsString()));
        return seriesTitles;
    }

    @Test
    public void getSerieByIdTest() throws ResourceNotFoundException {
        int id = 1;

        Optional<Series> serie = Optional.of(new Series("Title1"));
        serie.get().setId(id);

        Mockito.doReturn(serie).when(serieRepository).findById(id);

        JsonObject jsonObject = new JsonParser().parse(serieController.getSeriesById(id)).getAsJsonObject();//Got String, Convert into JSonObject to get Body, to recreate the Serie
        Series actual = new Gson().fromJson(jsonObject.get("body"), Series.class);

        Assert.assertEquals(serie.get().getTitle(),actual.getTitle());
        Mockito.verify(serieRepository).findById(id);

    }

}