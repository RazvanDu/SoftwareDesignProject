package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.UserType;
import com.Project.MainService.Utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.istack.logging.Logger;
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
 * The type Cart controller.
 */
@RestController
public class CartController {

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
    @RequestMapping("/cart")
    public ModelAndView cart(HttpServletRequest request) {

        if (Utils.getUserType(restTemplate, request) == UserType.NOT_LOGGED) {
            logger.warning("A user that is not logged in tried to check its cart!");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cart.html");

        Utils.addClientToModel(modelAndView, request, restTemplate);

        String path = "cart/get?sessionId=" + request.getSession().getId();

        ResponseEntity<String> response = restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.ORDER_SERVICE, path), String.class);

        String totalPrice = new Gson().fromJson(response.getBody(), JsonObject.class)
                .get("body").getAsJsonObject().get("total").getAsString();

        JsonArray jsonArray = new Gson().fromJson(response.getBody(), JsonObject.class)
                .get("body").getAsJsonObject().get("cart").getAsJsonArray();

        List<HashMap<String, String>> cart = new ArrayList<>();

        jsonArray.forEach(jsonObject -> {
            HashMap<String, String> issue = new HashMap<>();
            issue.put("cover", jsonObject.getAsJsonObject().get("cover").getAsString());
            issue.put("title", jsonObject.getAsJsonObject().get("title").getAsString());
            issue.put("price", jsonObject.getAsJsonObject().get("price").getAsString());
            issue.put("amount", jsonObject.getAsJsonObject().get("amount").getAsString());
            issue.put("link", "/comics?id=" + jsonObject.getAsJsonObject().get("id").getAsString());
            issue.put("addLink", "/addToCart?issueId=" + jsonObject.getAsJsonObject().get("id").getAsString()
                                         + "&isCart=true");
            issue.put("removeLink", "/removeFromCart?issueId=" + jsonObject.getAsJsonObject().get("id").getAsString()
                                                 + "&isCart=true");
            issue.put("recommend", "/comics?id=" + jsonObject.getAsJsonObject().get("recommendId").getAsString());
            cart.add(issue);
        });

        modelAndView.addObject("cart", cart);
        modelAndView.addObject("empty", jsonArray.size() == 0);
        modelAndView.addObject("totalPrice", totalPrice);

        return modelAndView;

    }

}
