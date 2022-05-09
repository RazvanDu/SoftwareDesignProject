package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
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

@RestController
public class SignupController {

    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("/signup")
    public ModelAndView signup(@RequestParam(name = "age", required = false) Integer age,
                               @RequestParam(name = "last_name", required = false) String lastName,
                               @RequestParam(name = "first_name", required = false) String firstName,
                               @RequestParam(name = "email", required = false) String email,
                               @RequestParam(name = "pswd", required = false) String pswd,
                               HttpServletRequest request, HttpServletResponse resp) throws IOException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signup.html");

        Utils.addClientToModel(modelAndView, request, restTemplate);

        boolean alreadyExists = false;

        if (age != null && email != null && lastName != null && firstName != null && pswd != null) {

            String path = "clients/signup?" + "age=" + age + "&" +
                                              "lastName=" + lastName + "&" +
                                              "firstName=" + firstName + "&" +
                                              "email=" + email + "&" +
                                              "pswd=" + pswd;

            ResponseEntity<String> response =
                    restTemplate.getForEntity(Utils.getDataURL(ServiceType.USER_SERVICE, path), String.class);

            JsonPrimitive jsonObject = new Gson().fromJson(response.getBody(), JsonPrimitive.class);

            if (jsonObject.getAsString().equals("Success!")) {
                logger.info("The user with email " + email + " has just signed up!");
                resp.sendRedirect("/login?success=true");
            } else {
                alreadyExists = true;
                modelAndView.addObject("errorMessage", jsonObject.getAsString());
            }

        }

        modelAndView.addObject("alreadyExists", alreadyExists);

        return modelAndView;

    }

}
