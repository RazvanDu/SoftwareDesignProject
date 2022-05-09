package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.UserType;
import com.Project.MainService.Utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.istack.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Order cart controller.
 */
@RestController
public class OrderCartController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Order.
     *
     * @param address the address
     * @param card    the card
     * @param cvc     the cvc
     * @param message the message
     * @param request the request
     * @param resp    the resp
     * @throws IOException the io exception
     */
    @RequestMapping("/placeOrder")
    public void order(@RequestParam(name = "address", required = false) String address,
                      @RequestParam(name = "card", required = false) String card,
                      @RequestParam(name = "cvc", required = false) String cvc,
                      @RequestParam(name = "message", required = false) String message,
                      HttpServletRequest request, HttpServletResponse resp) throws IOException {

        if (Utils.getUserType(restTemplate, request) == UserType.NOT_LOGGED) {
            logger.warning("An user that is not logged in tried to order his cart!");
            return;
        }

        String id = request.getSession().getId();
        String path = "cart/buy?sessionId=" + id + "&address=" + address + "&message=" + message;

        ResponseEntity<String> response = restTemplate
                .getForEntity(Utils.getDataURL(ServiceType.ORDER_SERVICE, path), String.class);

        String totalPrice = new Gson().fromJson(response.getBody(), JsonObject.class).get("body").getAsString();

        logger.info("A cart of price " + totalPrice + "$ has been ordered!");

        resp.sendRedirect("?emptyCart=true&price=" + Double.valueOf(totalPrice));

    }

}
