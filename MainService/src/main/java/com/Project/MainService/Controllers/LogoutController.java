package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.UserType;
import com.Project.MainService.Utilities.Utils;
import com.sun.istack.logging.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Logout controller.
 */
@RestController
public class LogoutController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Logout.
     *
     * @param request the request
     * @param resp    the resp
     * @throws IOException the io exception
     */
    @RequestMapping("/logoutAction")
    public void logout(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        if (Utils.getUserType(restTemplate, request) == UserType.NOT_LOGGED)
            return;

        logger.info("Sent a request to log out the user with sessionId " + request.getSession().getId());

        String id = request.getSession().getId();

        restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.USER_SERVICE,
                                          "clients/logout?sessionId=" + id), String.class);
        resp.sendRedirect("/");

    }

}
