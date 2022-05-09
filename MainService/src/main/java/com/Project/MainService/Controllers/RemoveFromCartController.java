package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.UserType;
import com.Project.MainService.Utilities.Utils;
import com.sun.istack.logging.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RemoveFromCartController {

    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("/removeFromCart")
    public void cart(@RequestParam(name = "issueId") String issueId,
                     @RequestParam(name = "isCart") String isCart,
                     HttpServletRequest request,
                     HttpServletResponse resp) throws IOException {

        if (Utils.getUserType(restTemplate, request) == UserType.NOT_LOGGED) {
            logger.warning("A user that is not logged in tried to remove from its cart!");
            return;
        }

        System.out.println("CALLED");

        String path = "cart/remove?sessionId=" + request.getSession().getId() + "&issueId=" + issueId;

        restTemplate.getForEntity(Utils.getDataURL(ServiceType.ORDER_SERVICE, path), String.class);

        if (isCart.equals("false"))
            resp.sendRedirect("/comics?id=" + issueId);
        else
            resp.sendRedirect("/cart");

    }

    @RequestMapping("/removeAllFromCart")
    public void cartAll(@RequestParam(name = "isCart") String isCart,
                        HttpServletRequest request,
                        HttpServletResponse resp) throws IOException {

        if (Utils.getUserType(restTemplate, request) == UserType.NOT_LOGGED) {
            logger.warning("A user that is not logged in tried to remove from its cart!");
            return;
        }

        String path = "cart/removeAll?sessionId=" + request.getSession().getId();

        restTemplate.getForEntity(Utils.getDataURL(ServiceType.ORDER_SERVICE, path), String.class);

        if (isCart.equals("false"))
            resp.sendRedirect("/");
        else
            resp.sendRedirect("/cart");

    }

}
