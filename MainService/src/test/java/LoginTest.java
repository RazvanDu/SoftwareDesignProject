import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.*;


public class LoginTest {

  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;

  @Before
  public void setUp() {
    WebDriverManager.chromedriver().setup();

    ChromeOptions options = new ChromeOptions();
    options.setHeadless(true);

    driver = new ChromeDriver(options);

    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void loginFailureTest() {
    driver.get("http://localhost:1111/");
    driver.manage().window().setSize(new Dimension(1552, 849));
    assertThat(driver.findElement(By.linkText("Login")).getText(), is("Login"));
    driver.findElement(By.linkText("Login")).click();
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(1) .text-white")).getText(), is("Email:"));
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(2) .text-white")).getText(), is("Password:"));
    assertThat(driver.findElement(By.id("signup")).getText(), is("Sign-Up"));
    assertThat(driver.findElement(By.id("submit")).getText(), is("Login"));
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("idk@idk.com");
    driver.findElement(By.id("pwd")).click();
    driver.findElement(By.id("pwd")).sendKeys("1232");
    driver.findElement(By.id("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".text-danger")).getText(), is("Invalid email or password"));
  }

  @Test
  public void loginSuccessSellerTest() {
    driver.get("http://localhost:1111/");
    driver.manage().window().setSize(new Dimension(1552, 849));
    assertThat(driver.findElement(By.linkText("Login")).getText(), is("Login"));
    driver.findElement(By.linkText("Login")).click();
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(1) .text-white")).getText(), is("Email:"));
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(2) .text-white")).getText(), is("Password:"));
    assertThat(driver.findElement(By.id("submit")).getText(), is("Login"));
    assertThat(driver.findElement(By.id("signup")).getText(), is("Sign-Up"));
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("eu@gmail.com");
    driver.findElement(By.id("pwd")).click();
    driver.findElement(By.id("pwd")).sendKeys("12345678");
    driver.findElement(By.id("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".justify-content-end > div")).getText(), is("Cart Logout Hello euu nueu"));
    List<WebElement> elements = driver.findElements(By.linkText("Add comic"));
    assert(elements.size() > 0);
    driver.findElement(By.linkText("Logout")).click();
    assertThat(driver.findElement(By.linkText("Login")).getText(), is("Login"));
  }

  @Test
  public void loginSuccessClientTest() {
    driver.get("http://localhost:1111/");
    driver.manage().window().setSize(new Dimension(1552, 849));
    assertThat(driver.findElement(By.linkText("Login")).getText(), is("Login"));
    driver.findElement(By.linkText("Login")).click();
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(1) .text-white")).getText(), is("Email:"));
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(2) .text-white")).getText(), is("Password:"));
    assertThat(driver.findElement(By.id("submit")).getText(), is("Login"));
    assertThat(driver.findElement(By.id("signup")).getText(), is("Sign-Up"));
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("notsel@ymail.com");
    driver.findElement(By.id("pwd")).click();
    driver.findElement(By.id("pwd")).sendKeys("12345678");
    driver.findElement(By.id("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".text-white:nth-child(4)")).getText(), is("client"));
    assertThat(driver.findElement(By.linkText("Logout")).getText(), is("Logout"));
    assertThat(driver.findElement(By.linkText("Cart")).getText(), is("Cart"));
    assertThat(driver.findElement(By.cssSelector(".navbar")).getText(), is("Cosmics Store\nCart Logout Hello client not sell"));
    {
      List<WebElement> elements = driver.findElements(By.linkText("Add comic"));
      assert(elements.size() == 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.linkText("Remove the comic"));
      assert(elements.size() == 0);
    }
    assertThat(driver.findElement(By.linkText("Logout")).getText(), is("Logout"));
    driver.findElement(By.linkText("Logout")).click();
  }

  @Test
  public void loginSuccessAdminTest() {
    driver.get("http://localhost:1111/");
    driver.manage().window().setSize(new Dimension(1552, 849));
    assertThat(driver.getTitle(), is("Cosmics Store"));
    {
      List<WebElement> elements = driver.findElements(By.linkText("Login"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("Login")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("duck@gmail.com");
    driver.findElement(By.id("pwd")).sendKeys("12345678");
    driver.findElement(By.id("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".navbar")).getText(), is("Cosmics Store\nCart Logout Hello Admin Admin"));
    {
      List<WebElement> elements = driver.findElements(By.linkText("Remove the comic"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("Logout")).click();
  }

}
