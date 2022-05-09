package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.UserType;
import com.Project.MainService.Utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sun.istack.logging.Logger;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Add comic controller.
 */
@RestController
public class AddComicController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Cart model and view.
     *
     * @param request the request
     * @return the model and view
     */
    @RequestMapping("/addcomic")
    @Timed(value = "addcomicPage.time", description = "Time taken to display the add comic page")
    public ModelAndView cart(HttpServletRequest request) {

        if (Utils.getUserType(restTemplate, request) != UserType.SELLER) {
            logger.warning("Add comic was accessed by an user of type " + Utils.getUserType(restTemplate, request));
            return null;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addcomic.html");

        Utils.addClientToModel(modelAndView, request, restTemplate);

        ResponseEntity<String> response = restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.CATALOG_SERVICE, "series/get"), String.class);

        JsonArray jsonArray = new Gson().fromJson(response.getBody(), JsonArray.class);

        List<HashMap<String, String>> series = new ArrayList<>();

        jsonArray.forEach(jsonObject -> {
            HashMap<String, String> seriess = new HashMap<>();
            seriess.put("title", jsonObject.getAsJsonObject().get("title").getAsString());
            seriess.put("id", jsonObject.getAsJsonObject().get("id").getAsString());
            series.add(seriess);
        });

        modelAndView.addObject("series", series);

        response = restTemplate.getForEntity(Utils.getDataURL(ServiceType.CATALOG_SERVICE, "writers/get"),
                                                              String.class);

        jsonArray = new Gson().fromJson(response.getBody(), JsonArray.class);

        List<HashMap<String, String>> writers = new ArrayList<>();

        jsonArray.forEach(jsonObject -> {
            HashMap<String, String> writer = new HashMap<>();
            writer.put("name", jsonObject.getAsJsonObject().get("name").getAsString());
            writers.add(writer);
        });

        modelAndView.addObject("series", series);
        modelAndView.addObject("writers", writers);

        return modelAndView;

    }

}
