import com.Project.UserService.Controllers.ClientsController;

import com.Project.UserService.Database.Client;
import com.Project.UserService.Database.ClientsRepository;
import com.Project.UserService.Utilities.Password;
import com.Project.UserService.Utilities.ResourceNotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RunWith(MockitoJUnitRunner.class)
public class ClientsControllerTest {


    @Mock
    private ClientsRepository clientsRepository;

    @InjectMocks
    private final ClientsController clientsController = new ClientsController();


    @Test
    public void getAllClientsTest() {
        List<Client> clients = Stream.of("razvandumm@gmail.com",
        "idk@idk.com",
        "smecher@gmail.com",
        "altsmecher@gmail.com",
        "celmaibomba@bomba.com",
        "idk@gmail.com",
        "milearares@gmail.com",
        "oire@idk.com").map(Client::new).collect(Collectors.toList());

        Mockito.doReturn(clients).when(clientsRepository).findAll();
        String actual = clientsController.getAllClients();

        Assert.assertEquals(clients.stream().map(Client::getEmail).collect(Collectors.toList()), getEmails(actual));
        Mockito.verify(clientsRepository).findAll();
    }

    private List<String> getEmails(String clients) {
        List<String> clientsEmails = new ArrayList<>();
        JsonArray clientsJson = (JsonArray) new JsonParser().parse(clients);
        clientsJson.forEach(j -> clientsEmails.add(((JsonObject)j).get("email").getAsString()));
        return clientsEmails;
    }

    @Test
    public void getClientByIdTest() throws ResourceNotFoundException {
        int id = 1;

        Optional<Client> client = Optional.of(new Client(id, "eu@gmail.com"));
        Mockito.doReturn(client).when(clientsRepository).findById(id);

        String actual = clientsController.getClientById(id);
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(new Client(id, "eu@gmail.com"))), actual);
        Mockito.verify(clientsRepository).findById(id);
    }

    @Test
    public void getClientByEmailTest() throws ResourceNotFoundException {
        String email = "eu@gmail.com";

        Optional<Client> client = Optional.of(new Client(1, email));
        Mockito.doReturn(client).when(clientsRepository).findByEmail(email);

        String actual = clientsController.getClientByEmail(email);
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(new Client(1, email))), actual);
        Mockito.verify(clientsRepository).findByEmail(email);
    }

    @Test
    public void getClientByIdThrowExceptionTest() {
        int id = 1;

        Mockito.doReturn(Optional.empty()).when(clientsRepository).findById(id);

        try {
            clientsController.getClientById(id);
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getClientByEmailThrowExceptionTest() {
        String email = "eu@gmail.com";

        Mockito.doReturn(Optional.empty()).when(clientsRepository).findByEmail(email);

        try {
            clientsController.getClientByEmail(email);
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void signUpInvalidLastNameTest() {
        String response = clientsController.signUp(18, "", "eu", "eu@gmail.com", "12345678");
        response = new Gson().fromJson(response, String.class);
        Assert.assertEquals("Invalid name!", response);
    }

    @Test
    public void signUpInvalidFirstNameTest() {
        String response = clientsController.signUp(18, "euu", null, "eu@gmail.com", "12345678");
        response = new Gson().fromJson(response, String.class);
        Assert.assertEquals("Invalid name!", response);
    }

    @Test
    public void signUpInvalidAgeTest() {
        String response = clientsController.signUp(17, "euu", "eu", "eu@gmail.com", "12345678");
        response = new Gson().fromJson(response, String.class);
        Assert.assertEquals("Invalid age!", response);
    }

    @Test
    public void signUpInvalidEmailTest() {
        String response = clientsController.signUp(18, "euu", "eu", "eu@gmail.", "12345678");
        response = new Gson().fromJson(response, String.class);
        Assert.assertEquals("Invalid email!", response);
    }

    @Test
    public void signUpInvalidPswdTest() {
        String response = clientsController.signUp(18, "euu", "eu", "eu@gmail.com", "1234567");
        response = new Gson().fromJson(response, String.class);
        Assert.assertEquals("Invalid password!", response);
    }

    @Test
    public void PasswordGetSaltedHashTest() throws Exception {
        Password.getSaltedHash("123456");
        Assert.assertTrue(true);
    }

    @Test
    public void PasswordGetSaltedHashEmptyTest() throws Exception {
        try {
            Password.getSaltedHash("");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid password!", e.getMessage());
        }
    }

    @Test
    public void signUpEmailAlreadyInUseTest() {
        Mockito.doReturn(Optional.of(new Client(1, "eu@gmail.com"))).when(clientsRepository).findByEmail("eu@gmail.com");

        String response = clientsController.signUp(18, "euu", "eu", "eu@gmail.com", "12345678");
        response = new Gson().fromJson(response, String.class);

        Assert.assertEquals("Email already in use!", response);
    }

    @Test
    public void signUpSuccessTest() {
        Mockito.doReturn(Optional.empty()).when(clientsRepository).findByEmail("eu@gmail.com");
        //Mockito.doNothing().when(clientsRepository).insertClient("eu", "euu", 18, "eu@gmail.com", "12345678", false);

        String response = clientsController.signUp(18, "euu", "eu", "eu@gmail.com", "12345678");
        response = new Gson().fromJson(response, String.class);

        Assert.assertEquals("Success!", response);
    }

    @Test
    public void loginInvalidEmailTest() {
        String response = clientsController.login("", "12345678", "1");
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(new Client(false, "Invalid email!"))), response);
    }

    @Test
    public void loginInvalidPswdTest() {
        String response = clientsController.login("eu@gmail.com", null, "1");
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(new Client(false, "Invalid password!"))), response);
    }

    @Test
    public void loginWrongEmailTest() {
        Mockito.doReturn(Optional.empty()).when(clientsRepository).findByEmail("eu@gmail.com");

        String response = clientsController.login("eu@gmail.com", "12345678", "1");
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(new Client(false, "Wrong Email/password!"))), response);
    }

    @Test
    public void loginWrongPswdTest() {
        Mockito.doReturn(Optional.of(new Client(1, "eu@gmail.com", "lud8o54rGBuw7E+PK/fQmRmmRC2WwVTtjSkOqxvwzl0=$/t0d8p/MX/POMHLOwDFd4iCjUOXSMGWS3hT7NrBdalM="))).when(clientsRepository).findByEmail("eu@gmail.com");

        String response = clientsController.login("eu@gmail.com", "123456oi", "1");
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(new Client(false, "Wrong Email/password!"))), response);
    }

    @Test
    public void PasswordCheckTest() throws Exception {
        try {
            Password.check("12345678", "12345678");
        } catch (IllegalStateException e) {
            Assert.assertEquals("The stored password must have the form 'salt$hash'!", e.getMessage());
        }
    }

    @Test
    public void loginWrongPswdCheckTest() {
        Mockito.doReturn(Optional.of(new Client(1, "eu@gmail.com", "12345678"))).when(clientsRepository).findByEmail("eu@gmail.com");

        String response = "";

        try {
            response = clientsController.login("eu@gmail.com", "12345678", "1");
        } catch (Exception e) {
            //Assert.assertEquals("The stored password must have the form 'salt$hash'!", e.getMessage());
        }

        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(new Client(false, "Wrong Email/password!"))), response);

    }

    @Test
    public void loginSuccessfulTest() {
        Client client = new Client(1, "eu@gmail.com", "lud8o54rGBuw7E+PK/fQmRmmRC2WwVTtjSkOqxvwzl0=$/t0d8p/MX/POMHLOwDFd4iCjUOXSMGWS3hT7NrBdalM=");
        Mockito.doReturn(Optional.of(client)).when(clientsRepository).findByEmail("eu@gmail.com");

        String response = clientsController.login("eu@gmail.com", "12345678", "1");
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(client)), response);
        Assert.assertEquals(new Gson().toJson(ResponseEntity.ok().body(client)),
                new Gson().toJson(ResponseEntity.ok().body(clientsController.getLoggedUsers().get("1"))));
    }
}
