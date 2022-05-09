package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.UserType;
import com.Project.MainService.Utilities.Utils;
import com.sun.istack.logging.Logger;
import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Remove comic controller.
 */
@RestController
public class RemoveComicController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Cart.
     *
     * @param issueId the issue id
     * @param request the request
     * @param resp    the resp
     * @throws IOException the io exception
     */
    @RequestMapping("/removecomic")
    //@Timed(value = "removeComic.time", description = "Time taken to remove a comic")
    public void cart(@RequestParam(name = "issueId") String issueId,
                     HttpServletRequest request,
                     HttpServletResponse resp) throws IOException {

        if (Utils.getUserType(restTemplate, request) != UserType.ADMIN) {
            logger.warning("A user of type " + Utils.getUserType(restTemplate, request) + " tried to remove a comic!");
            return;
        }

        String path = "issues/delete/" + issueId;

        restTemplate.getForEntity(Utils.getDataURL(ServiceType.CATALOG_SERVICE, path), String.class);
        resp.sendRedirect("/");

        logger.info("A request to remove the issue with id " + issueId + " has been sent!");

    }

}
