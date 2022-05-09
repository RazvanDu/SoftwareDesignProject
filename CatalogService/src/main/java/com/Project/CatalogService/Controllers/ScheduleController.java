package com.Project.CatalogService.Controllers;

import com.Project.CatalogService.Database.Schedule;
import com.Project.CatalogService.Database.ScheduleRepository;
import com.Project.CatalogService.Utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

/**
 * The type Schedule controller.
 */
@RestController
@RequestMapping(path="/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * Gets all schedules.
     *
     * @return the all schedules
     */
    @GetMapping(path = "/get")
    public @ResponseBody
    String getAllSchedules() {

        return new Gson().toJson(scheduleRepository.findAll());

    }

    /**
     * Gets schedule by id.
     *
     * @param scheduleId the schedule id
     * @return the schedule by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/{id}")
    public @ResponseBody
    String getScheduleById(@PathVariable(value = "id") Integer scheduleId) throws ResourceNotFoundException {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() ->
                new ResourceNotFoundException("Schedule not found with id :" + scheduleId));

        return new Gson().toJson(ResponseEntity.ok().body(schedule));

    }
}