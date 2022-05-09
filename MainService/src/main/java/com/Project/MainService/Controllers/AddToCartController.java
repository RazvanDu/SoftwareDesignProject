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

/**
 * The type Add to cart controller.
 */
@RestController
public class AddToCartController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Cart.
     *
     * @param issueId the issue id
     * @param isCart  the is cart
     * @param request the request
     * @param resp    the resp
     * @throws IOException the io exception
     */
    @RequestMapping("/addToCart")
    public void cart(@RequestParam(name = "issueId") String issueId,
                     @RequestParam(name = "isCart") String isCart,
                     HttpServletRequest request,
                     HttpServletResponse resp) throws IOException {

        if (Utils.getUserType(restTemplate, request) == UserType.NOT_LOGGED) {
            logger.warning("Add to cart was accesed by a user that is not logged in!");
            return;
        }

        String path = "cart/add?" + "sessionId=" + request.getSession().getId() + "&issueId=" + issueId;

        restTemplate.getForEntity(Utils.getDataURL(ServiceType.ORDER_SERVICE, path), String.class);

        if (isCart.equals("false"))
            resp.sendRedirect("/comics?id=" + issueId);
        else
            resp.sendRedirect("/cart");

    }

}
