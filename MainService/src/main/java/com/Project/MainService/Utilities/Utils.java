package com.Project.MainService.Utilities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    public static InstanceInfo getService(ServiceType type) {

        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        Applications applications = registry.getApplications();

        return applications.getRegisteredApplications(type.toString().replaceAll("_", "-")).getInstances().get(0);

    }

    public static String getDataURL(ServiceType type,
                                    String path) {

        return getService(type).getHomePageUrl() + "/" + path;

    }

    public static UserType getUserType(RestTemplate restTemplate,
                                       HttpServletRequest request) {

        String path = "clients/session?" + "id=" + request.getSession().getId();

        ResponseEntity<String> response = restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.USER_SERVICE, path), String.class);

        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class)
                .get("body").getAsJsonObject();

        int id = jsonObject.get("id").getAsInt();

        if (id == -1)
            return UserType.NOT_LOGGED;

        boolean seller = jsonObject.get("seller").getAsBoolean();

        if (seller)
            return UserType.SELLER;

        boolean admin = jsonObject.get("adminStatus").getAsBoolean();

        if (admin)
            return UserType.ADMIN;

        return UserType.REGULAR;

    }

    public static void addClientToModel(ModelAndView modelAndView,
                                        HttpServletRequest request,
                                        RestTemplate restTemplate) {

        String path = "clients/session?id=" + request.getSession().getId();

        ResponseEntity<String> loggedResponse = restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.USER_SERVICE, path), String.class);

        JsonObject loggedJson = new Gson().fromJson(loggedResponse.getBody(), JsonObject.class).get("body").getAsJsonObject();

        if (loggedJson.get("id").getAsString().equals("-1")) {

            modelAndView.addObject("logged", false);

        } else {

            modelAndView.addObject("logged", true);
            modelAndView.addObject("clientFirstName", loggedJson.get("firstName").getAsString());
            modelAndView.addObject("clientLastName", loggedJson.get("lastName").getAsString());
            modelAndView.addObject("seller", loggedJson.get("seller").getAsString());
            modelAndView.addObject("isAdmin", loggedJson.get("adminStatus").getAsString());

        }
    }

}
