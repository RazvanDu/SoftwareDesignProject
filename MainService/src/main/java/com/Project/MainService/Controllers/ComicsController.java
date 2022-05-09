package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Comics controller.
 */
@RestController
public class ComicsController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    /**
     * Comics model and view.
     *
     * @param issueId the issue id
     * @param request the request
     * @return the model and view
     */
    @RequestMapping("/comics")
    @Timed(value = "mainPage.time", description = "Time taken to display the main page")
    public ModelAndView comics(@RequestParam(name = "id") String issueId, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("comics.html");

        Utils.addClientToModel(modelAndView, request, restTemplate);

        ResponseEntity<String> response = restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.CATALOG_SERVICE, "issues/" + issueId), String.class);

        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class).get("body").getAsJsonObject();

        Map<String, String> mapped = new HashMap<>();
        mapped.put("writerLink", "/writers?id=" + jsonObject.get("writer").getAsJsonObject().get("id").getAsString());
        mapped.put("seriesLink", "/series?id=" + jsonObject.get("series").getAsJsonObject().get("id").getAsString());
        mapped.put("title", jsonObject.get("title").getAsString());
        mapped.put("writerName", jsonObject.get("writer").getAsJsonObject().get("name").getAsString());
        mapped.put("seriesTitle", jsonObject.get("series").getAsJsonObject().get("title").getAsString());
        mapped.put("price", String.valueOf(jsonObject.get("price").getAsDouble()));
        mapped.put("cover", String.valueOf(jsonObject.get("cover").getAsString()));
        mapped.put("sold", String.valueOf(jsonObject.get("sold").getAsInt()));

        modelAndView.addObject("hasLink", jsonObject.get("hasLink").getAsBoolean());

        if (jsonObject.get("hasLink").getAsBoolean())
            mapped.put("linkPreview", jsonObject.get("linkPreview").getAsString());

        modelAndView.addObject("cartLink", "/addToCart?issueId=" + issueId + "&isCart=false");
        modelAndView.addObject("issue", mapped);

        return modelAndView;

    }
}
