package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.istack.logging.Logger;
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
 * The type Main controller.
 */
@RestController
public class MainController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    /**
     * Index model and view.
     *
     * @param emptyCart the empty cart
     * @param price     the price
     * @param request   the request
     * @return the model and view
     */
    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "emptyCart", required = false) Boolean emptyCart,
                              @RequestParam(name = "price", required = false) String price,
                              HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");

        Utils.addClientToModel(modelAndView, request, restTemplate);

        String path = "issues/get";

        ResponseEntity<String> response =
                restTemplate.getForEntity(Utils.getDataURL(ServiceType.CATALOG_SERVICE, path), String.class);

        JsonArray jsonArray = new Gson().fromJson(response.getBody(), JsonArray.class);

        List<Map<String, String>> finalMap = new ArrayList<>();

        jsonArray.forEach(object -> {

            Map<String, String> mapped = new HashMap<>();

            JsonObject jsonObject = object.getAsJsonObject();

            mapped.put("title", jsonObject.get("title").getAsString());
            mapped.put("writerName", jsonObject.get("writer").getAsJsonObject().get("name").getAsString());
            mapped.put("price", String.valueOf(jsonObject.get("price").getAsDouble()));
            mapped.put("link", "/comics?id=" + jsonObject.get("id").getAsString());
            mapped.put("removeLink", "/removecomic?issueId=" + jsonObject.get("id").getAsString());
            mapped.put("cover", jsonObject.get("cover").getAsString());

            finalMap.add(mapped);

        });

        modelAndView.addObject("issues", finalMap);

        if (emptyCart != null) {
            modelAndView.addObject("emptyCart", emptyCart);
            if (emptyCart)
                modelAndView.addObject("paid", price);
        }

        return modelAndView;

    }

}
