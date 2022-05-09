package com.Project.CatalogService;

import com.Project.CatalogService.Controllers.ScheduleController;
import com.Project.CatalogService.Database.Schedule;
import com.Project.CatalogService.Database.ScheduleRepository;
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

public class ScheduleControllerTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private final ScheduleController scheduleController = new ScheduleController();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllSchedulesTest() {
        List<Schedule> schedules = new ArrayList<>();
        schedules.add(new Schedule("Type1"));
        schedules.add(new Schedule("Type2"));

        Mockito.doReturn(schedules).when(scheduleRepository).findAll();
        String actual = scheduleController.getAllSchedules();

        Assert.assertEquals(schedules.stream().map(Schedule::getType).collect(Collectors.toList()), getTypes(actual));
        Mockito.verify(scheduleRepository).findAll();
    }

    private List<String> getTypes(String schedules) {
        List<String> schedulesTypes = new ArrayList<>();
        JsonArray schedulesJson = (JsonArray) new JsonParser().parse(schedules);
        schedulesJson.forEach(j -> schedulesTypes.add(((JsonObject)j).get("type").getAsString()));
        return schedulesTypes;
    }

    @Test
    public void getScheduleByIdTest() throws ResourceNotFoundException {
        int id = 1;

        Optional<Schedule> schedule = Optional.of(new Schedule("Type1"));
        schedule.get().setId(id);

        Mockito.doReturn(schedule).when(scheduleRepository).findById(id);

        JsonObject jsonObject = new JsonParser().parse(scheduleController.getScheduleById(id)).getAsJsonObject();//Got String, Convert into JSonObject to get Body, to recreate the Schedule
        Schedule actual = new Gson().fromJson(jsonObject.get("body"), Schedule.class);

        Assert.assertEquals(schedule.get().getType(),actual.getType());
        Mockito.verify(scheduleRepository).findById(id);

    }
}