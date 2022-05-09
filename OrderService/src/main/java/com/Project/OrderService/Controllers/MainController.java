package com.Project.OrderService.Controllers;

import com.Project.OrderService.Database.OrdersRepository;
import com.Project.OrderService.Utilities.Issue;
import com.Project.OrderService.Utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.istack.logging.Logger;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Main controller.
 */
@RestController
@RequestMapping(path="/cart")
@Timed
public class MainController {

    @Autowired
    private OrdersRepository ordersRepository;

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    private final HashMap<String, HashMap<Integer, Issue>> cart = new HashMap<>();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Add to cart.
     *
     * @param session the session
     * @param id      the id
     */
    @GetMapping("/add")
    @Timed(value = "addIssue.time", description = "Time taken to add an issue.")
    public @ResponseBody
    void addToCart(@RequestParam(value = "sessionId") String session,
                   @RequestParam(value = "issueId") Integer id) {

        if (!cart.containsKey(session))
            cart.put(session, new HashMap<>());

        Issue issue;

        logger.info("The user with sessionId " + session + " just added the issue with id " + id + " to its cart!");

        if (!cart.get(session).containsKey(id)) {

            final Issue[] issues = new Issue[1];

            if (cart.values().stream().anyMatch(cartIssue -> cartIssue.containsKey(id))) {

                cart.values().forEach(cartIssue -> {
                    if (cartIssue.containsKey(id))
                        issues[0] = cartIssue.get(id);
                });

                issue = issues[0];

            } else {

                ResponseEntity<String> response;

                try {
                    response = restTemplate.getForEntity(Utils.getDataURL("issues/" + id), String.class);
                } catch (HttpClientErrorException e) {
                    e.printStackTrace();
                    return;
                }

                issue = Utils.getIssueFromJson(new Gson().fromJson(response.getBody(), JsonObject.class).get("body")
                        .getAsJsonObject());

            }

            issue.setAmount(1);
            cart.get(session).put(issue.getId(), issue);

        } else {

            cart.get(session).get(id).increaseAmount();

        }
    }

    /**
     * Gets cart.
     *
     * @param session the session
     * @return the cart
     */
    @GetMapping("/get")
    @Timed(value = "cart.time", description = "Time taken to return the cart")
    public @ResponseBody
    String getCart(@RequestParam(value = "sessionId") String session) {

        JsonObject jsonObject = new JsonObject();

        if (cart.containsKey(session)) {

            recommendSet(session);

            jsonObject.addProperty("total", new DecimalFormat("#.##").format(cart.get(session).values().stream()
                    .mapToDouble(issue -> issue.getPrice() * issue.getAmount()).sum()));
            jsonObject.add("cart", new Gson().toJsonTree(cart.get(session).values()));

        } else {

            jsonObject.addProperty("total", -1);
            jsonObject.add("cart", new Gson().toJsonTree(new ArrayList<>()));

        }

        return new Gson().toJson(ResponseEntity.ok().body(jsonObject));

    }

    /**
     * Buy cart string.
     *
     * @param session the session
     * @param address the address
     * @param message the message
     * @return the string
     */
    @GetMapping("/buy")
    public @ResponseBody
    String buyCart(@RequestParam(value = "sessionId") String session,
                   @RequestParam(value = "address") String address,
                   @RequestParam(value = "message") String message) {

        logger.info("The cart of user with sessionId " + session + " has just been ordered!");

        if (!cart.containsKey(session)) {

            return new Gson().toJson(ResponseEntity.ok().body(-1));

        } else {

            Double total = cart.get(session).values().stream()
                    .mapToDouble(issue -> issue.getPrice() * issue.getAmount()).sum();

            if (address != null && !address.equals("null")) {

                cart.get(session).values().forEach(issue -> {
                    String path = "issues/sellcomic/" + issue.getId() + "/" + issue.getAmount();
                    restTemplate.getForEntity(Utils.getDataURL(path), String.class);
                });

                String amounts = Utils.getAmounts(cart.get(session));

                ordersRepository.save(address,
                                      message,
                                      Utils.getIssues(cart.get(session)), amounts, total.floatValue());

            }

            cart.remove(session);

            return new Gson().toJson(ResponseEntity.ok().body(new DecimalFormat("#.##").format(total)));

        }

    }

    /**
     * Remove issue from cart.
     *
     * @param session the session
     * @param id      the id
     */
    @GetMapping("/remove")
    public @ResponseBody
    void removeIssueFromCart(@RequestParam(value = "sessionId") String session,
                             @RequestParam(value = "issueId") Integer id) {

        logger.info("The user with sessionId " + session + " just removed the issue with id " + id + " from its cart!");

        if (cart.containsKey(session)) {

            if (!cart.get(session).containsKey(id) || cart.get(session).get(id).getAmount() <= 0)
                return;

            cart.get(session).get(id).decreaseAmount();

            if (cart.get(session).get(id).getAmount() == 0)
                cart.get(session).remove(id);

        }
    }

    /**
     * Remove all issue from cart.
     *
     * @param session the session
     */
    @GetMapping("/removeAll")
    public @ResponseBody
    void removeAllIssueFromCart(@RequestParam(value = "sessionId") String session) {

        logger.info("The user with sessionId " + session + " just cleared its cart!");

        if (cart.containsKey(session))
            cart.get(session).clear();

    }

    private void recommendSet(String session) {

        cart.get(session).values().forEach( issue -> {

            List<String> stringIssues = ordersRepository.findByIssue(String.valueOf(issue.getId()));

            List<Integer> issues = new ArrayList<>();

            stringIssues.forEach(i -> issues.addAll(Arrays.stream(i.split(","))
                    .map(Integer::parseInt).collect(Collectors.toList())));
            issues.removeIf(i -> i == issue.getId());

            AtomicReference<Integer> rec = new AtomicReference<>(-1);
            issues.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue()).ifPresent(i -> rec.set(i.getKey()));

            issue.setRecommendId(rec.get());

        });

    }

    /**
     * Gets cart hm.
     *
     * @return the cart hm
     */
    public HashMap<String, HashMap<Integer, Issue>> getCartHM() {

        return cart;

    }
}
