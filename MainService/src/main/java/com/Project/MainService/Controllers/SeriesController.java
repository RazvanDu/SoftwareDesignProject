package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Series controller.
 */
@RestController
public class SeriesController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    /**
     * Series model and view.
     *
     * @param seriesId the series id
     * @param request  the request
     * @return the model and view
     */
    @RequestMapping("/series")
    public ModelAndView series(@RequestParam(name = "id") String seriesId,
                               HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("series.html");

        Utils.addClientToModel(modelAndView, request, restTemplate);

        String path = "series/" + seriesId;

        ResponseEntity<String> response = restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.CATALOG_SERVICE, path), String.class);

        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class).get("body").getAsJsonObject();

        Map<String, String> series = new HashMap<>();

        series.put("cover", jsonObject.get("cover").getAsString());
        series.put("title", jsonObject.get("title").getAsString());
        series.put("startDate", jsonObject.get("startDate").getAsString());
        series.put("publisherName", jsonObject.get("publisher").getAsJsonObject().get("name").getAsString());
        series.put("rating", jsonObject.get("rating").getAsJsonObject().get("name").getAsString());
        series.put("scheduleType", jsonObject.get("schedule").getAsJsonObject().get("type").getAsString());

        modelAndView.addObject("series", series);

        modelAndView.addObject("publisherLink", jsonObject.get("publisher")
                .getAsJsonObject().get("website").getAsString());
        modelAndView.addObject("ongoing", jsonObject.get("ongoing").getAsBoolean());

        path = "issues/findBySeries/" + seriesId;

        ResponseEntity<String> responseWriter = restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.CATALOG_SERVICE, path), String.class);

        JsonArray jsonArray = new Gson().fromJson(responseWriter.getBody(), JsonObject.class)
                .get("body").getAsJsonArray();

        List<Map<String, String>> issues = new ArrayList<>();

        jsonArray.forEach(issue -> {

            Map<String, String> mapped = new HashMap<>();
            mapped.put("title", issue.getAsJsonObject().get("title").getAsString());
            mapped.put("link", "/comics?id=" + issue.getAsJsonObject().get("id").getAsString());

            issues.add(mapped);

        });

        modelAndView.addObject("issues", issues);

        return modelAndView;

    }

}
