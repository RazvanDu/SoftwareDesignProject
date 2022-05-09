package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.istack.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Login controller.
 */
@RestController
public class LoginController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Login model and view.
     *
     * @param success the success
     * @param email   the email
     * @param pswd    the pswd
     * @param request the request
     * @param resp    the resp
     * @return the model and view
     * @throws IOException the io exception
     */
    @RequestMapping("/login")
    //@Timed(value = "loginPageLoad.time", description = "Time taken to show the login page.")
    public ModelAndView login(@RequestParam(name = "success", required = false) boolean success,
                              @RequestParam(name = "email", required = false) String email,
                              @RequestParam(name = "pswd", required = false) String pswd,
                              HttpServletRequest request, HttpServletResponse resp) throws IOException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login.html");

        Utils.addClientToModel(modelAndView, request, restTemplate);

        modelAndView.addObject("success", success);

        if (email != null && pswd != null) {

            logger.info("Sent a request to login in the user with email " + email + "!");

            String id = request.getSession().getId();
            String path = "clients/login?" + "email=" + email + "&" + "pswd=" + pswd + "&" + "sessionId=" + id;

            ResponseEntity<String> response = restTemplate
                    .getForEntity(Utils.getDataURL(ServiceType.USER_SERVICE, path), String.class);

            JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class).get("body").getAsJsonObject();

            boolean exists = !jsonObject.get("id").getAsString().equals("-1");

            modelAndView.addObject("failed", !exists);
            modelAndView.addObject("logged", exists);

            if (exists)
                resp.sendRedirect("/");

        }

        return modelAndView;

    }

}
