import com.Project.OrderService.Controllers.MainController;
import com.Project.OrderService.Database.OrdersRepository;
import com.Project.OrderService.Utilities.Issue;
import com.Project.OrderService.Utilities.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class) @MockitoSettings(strictness = Strictness.LENIENT)
//@RunWith(MockitoJUnitRunner.class)
public class MainControllerTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    OrdersRepository ordersRepository;

    @InjectMocks
    MainController mainController = new MainController();

   private final String session = "1";
   private final Integer id = 1;

    @BeforeEach
    public void setupCart() {
        mainController.getCartHM().clear();
        mainController.getCartHM().put(session, new HashMap<>());
        mainController.getCartHM().get(session).put(id, new Issue(id, "Infinity Gauntlet #1", 2.79, 1, -1));
    }

    @Test
    public void addToCartNewIssueNewSessionTest() {
        Integer id = 3;
        String session = "2";
        Issue iss = new Issue(id, "Infinity Gauntlet #2", 2.79, 1, -1);

        Mockito
                .doReturn(new ResponseEntity<>(new Gson().toJson(ResponseEntity.ok().body(iss)), HttpStatus.OK))
                .when(restTemplate).getForEntity(Utils.getDataURL("issues/" + id), String.class);

        mainController.addToCart(session, id);
        Issue issue = mainController.getCartHM().get(session).get(id);

        Assert.assertEquals(1, issue.getAmount());
        Assert.assertEquals(iss, issue);
        Mockito.verify(restTemplate).getForEntity(Utils.getDataURL("issues/" + id), String.class);
    }

    @Test
    public void addToCartNewIssueSameSessionTest() {
        Integer id = 3;
        Issue iss = new Issue(id, "Infinity Gauntlet #2", 2.79, 1, -1);

        Mockito
                .doReturn(new ResponseEntity<>(new Gson().toJson(ResponseEntity.ok().body(iss)), HttpStatus.OK))
                .when(restTemplate).getForEntity(Utils.getDataURL("issues/" + id), String.class);

        mainController.addToCart(session, id);
        Issue issue = mainController.getCartHM().get(session).get(id);

        Assert.assertEquals(1, issue.getAmount());
        Assert.assertEquals(iss, issue);
        Mockito.verify(restTemplate).getForEntity(Utils.getDataURL("issues/" + id), String.class);
    }

    @Test
    public void addToCartSameIssueNewSessionTest() {
        String session = "2";
        Issue iss = new Issue(id, "Infinity Gauntlet #1", 2.79, 1, -1);

        Mockito
                .doReturn(new ResponseEntity<>(new Gson().toJson(ResponseEntity.ok().body(iss)), HttpStatus.OK))
                .when(restTemplate).getForEntity(Utils.getDataURL("issues/" + id), String.class);

        mainController.addToCart(session, id);
        Issue issue = mainController.getCartHM().get(session).get(id);

        Assert.assertEquals(1, issue.getAmount());
        Assert.assertEquals(iss, issue);
        Mockito.verify(restTemplate, Mockito.never()).getForEntity(Utils.getDataURL("issues/" + id), String.class);
    }

    @Test
    public void addToCartSameIssueSameSessionTest() {
        Issue iss = new Issue(id, "Infinity Gauntlet #1", 2.79, 1, -1);

        Mockito
                .doReturn(new ResponseEntity<>(new Gson().toJson(ResponseEntity.ok().body(iss)), HttpStatus.OK))
                .when(restTemplate).getForEntity(Utils.getDataURL("issues/" + id), String.class);

        mainController.addToCart(session, id);
        Issue issue = mainController.getCartHM().get(session).get(id);

        Assert.assertEquals(2, issue.getAmount());
        Assert.assertEquals(iss, issue);
        Mockito.verify(restTemplate, Mockito.never()).getForEntity(Utils.getDataURL("issues/" + id), String.class);
    }

    @Test
    public void getCartNoSessionTest() {
        String actual = mainController.getCart("2");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("total", -1);
        jsonObject.add("cart", new Gson().toJsonTree(new ArrayList<>()));
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(jsonObject)), actual);
    }

    @Test
    public void getCartSessionTest() {
        String actual = mainController.getCart(session);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("total", new DecimalFormat("#.##").format(2.79));
        jsonObject.add("cart", new Gson().toJsonTree(new Issue[]{new Issue(id, "Infinity Gauntlet #1", 2.79, 1, -1)}));
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(jsonObject)), actual);
    }

    @Test
    public void buyNoSessionTest() {
        String actual = mainController.buyCart("2", "", "");

        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(-1)), actual);
    }

    @Test
    public void buySessionTest() {
        mainController.getCartHM().get(session).values().forEach(issue -> Mockito
                .doReturn(null)
                .when(restTemplate).getForEntity(Utils.getDataURL("issues/sellcomic/" + issue.getId() + "/" + issue.getAmount()), String.class));

        Mockito
                .doNothing()
                .when(ordersRepository).save("", "", Utils.getIssues(mainController.getCartHM().get(session)), Utils.getAmounts(mainController.getCartHM().get(session)), (float) 2.79);

        String actual = mainController.buyCart(session, "", "");

        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(new DecimalFormat("#.##").format(2.79))),
                actual);
        Assert.assertFalse(mainController.getCartHM().containsKey(session));
    }

    @Test
    public void removeTest() {
        mainController.removeIssueFromCart(session, id);

        Assert.assertFalse(mainController.getCartHM().get(session).containsKey(id));
    }

    @Nested
    public class CartTest {

        @BeforeEach
        public void setupCart() {
            mainController.getCartHM().get(session).put(3, new Issue(3, "Infinity Gauntlet #2", 2.79, 1, -1));
            mainController.getCartHM().get(session).put(4, new Issue(4, "Eternals #3", 3.87, 1, -1));
            mainController.getCartHM().get(session).get(id).increaseAmount();
        }

        @Test
        public void getCartNoSessionTest() {
            String actual = mainController.getCart("2");

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("total", -1);
            jsonObject.add("cart", new Gson().toJsonTree(new ArrayList<>()));
            Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(jsonObject)), actual);
        }

        @Test
        public void getCartSessionTest() {
            String actual = mainController.getCart(session);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("total", new DecimalFormat("#.##").format(12.24));
            jsonObject.add("cart", new Gson().toJsonTree(new Issue[]{new Issue(id, "Infinity Gauntlet #1", 2.79, 2, -1),
                    new Issue(3, "Infinity Gauntlet #2", 2.79, 1, -1), new Issue(4, "Eternals #3", 3.87, 1, -1)}));
            Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(jsonObject)), actual);
        }

        @Test
        public void buyNoSessionTest() {
            String actual = mainController.buyCart("2", "", "");

            Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(-1)), actual);
        }

        @Test
        public void buySessionTest() {
            mainController.getCartHM().get(session).values().forEach(issue -> Mockito
                    .doReturn(null)
                    .when(restTemplate).getForEntity(Utils.getDataURL("issues/sellcomic/" + issue.getId() + "/" + issue.getAmount()), String.class));

            Mockito
                    .doNothing()
                    .when(ordersRepository).save("", "", Utils.getIssues(mainController.getCartHM().get(session)), Utils.getAmounts(mainController.getCartHM().get(session)), (float) 2.79);

            String actual = mainController.buyCart(session, "", "");

            Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(new DecimalFormat("#.##").format(12.24))),
                    actual);
            Assert.assertFalse(mainController.getCartHM().containsKey(session));
        }

        @Test
        public void removeTest() {
            mainController.removeIssueFromCart(session, id);

            Assert.assertEquals(1, mainController.getCartHM().get(session).get(id).getAmount());
        }
    }

}
