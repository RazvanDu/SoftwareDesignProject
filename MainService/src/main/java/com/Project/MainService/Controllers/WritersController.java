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

@RestController
public class WritersController {

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/writers")
    public ModelAndView writers(@RequestParam(name = "id") String writerId,
                                HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("writers.html");

        Utils.addClientToModel(modelAndView, request, restTemplate);

        String path = "writers/" + writerId;

        ResponseEntity<String> response = restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.CATALOG_SERVICE, path), String.class);

        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class)
                .get("body").getAsJsonObject();

        Map<String, String> writer = new HashMap<>();

        writer.put("picture", jsonObject.get("picture").getAsString());
        writer.put("name", jsonObject.get("name").getAsString());
        writer.put("born", jsonObject.get("born").getAsString());

        modelAndView.addObject("writer", writer);

        path = "issues/findByWriter/" + writerId;

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
