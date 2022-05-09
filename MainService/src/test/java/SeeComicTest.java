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


public class SeeComicTest {

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
  public void seeComicAsClientTest() {
    driver.get("http://localhost:1111/");
    driver.manage().window().setSize(new Dimension(1552, 849));
    {
      List<WebElement> elements = driver.findElements(By.linkText("Login"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("Login")).click();
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(1) .text-white")).getText(), is("Email:"));
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(2) .text-white")).getText(), is("Password:"));
    {
      List<WebElement> elements = driver.findElements(By.id("submit"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.id("signup"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("notsel@ymail.com");
    driver.findElement(By.id("pwd")).sendKeys("12345678");
    driver.findElement(By.id("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".navbar")).getText(), is("Cosmics Store\nCart Logout Hello client not sell"));
    {
      List<WebElement> elements = driver.findElements(By.linkText("See the comic"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("See the comic")).click();
    assertThat(driver.findElement(By.cssSelector("b:nth-child(4) .text-white")).getText(), is("Written by:"));
    assertThat(driver.findElement(By.cssSelector("b:nth-child(6) .text-white")).getText(), is("Series:"));
    assertThat(driver.findElement(By.cssSelector("b:nth-child(8) .text-white")).getText(), is("Price:"));
    assertThat(driver.findElement(By.cssSelector("h4")).getText(), is("Add to cart"));
    {
      List<WebElement> elements = driver.findElements(By.cssSelector("h6:nth-child(5) span"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.cssSelector("h6:nth-child(7) span"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".text-white > span"));
      assert(elements.size() > 0);
    }
    vars.put("writer", driver.findElement(By.cssSelector("h6:nth-child(5) span")).getText());
    vars.put("series", driver.findElement(By.cssSelector("h6:nth-child(7) span")).getText());
    vars.put("issue", driver.findElement(By.cssSelector("h1")).getText());
    driver.findElement(By.cssSelector("h6:nth-child(5) span")).click();
    assertThat(driver.findElement(By.cssSelector("h1")).getText(), is(vars.get("writer").toString()));
    {
      List<WebElement> elements = driver.findElements(By.linkText(vars.get("issue").toString()));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText(vars.get("issue").toString())).click();
    driver.findElement(By.cssSelector("h6:nth-child(7) span")).click();
    assertThat(driver.findElement(By.cssSelector("h1")).getText(), is(vars.get("series").toString()));
    {
      List<WebElement> elements = driver.findElements(By.linkText(vars.get("issue").toString()));
      assert(elements.size() > 0);
    }
    assertThat(driver.findElement(By.cssSelector("b:nth-child(4) .text-white")).getText(), is("Publisher:"));
    driver.findElement(By.linkText("Logout")).click();
  }

  @Test
  public void seeComicNotLoggedTest() {
    driver.get("http://localhost:1111/");
    driver.manage().window().setSize(new Dimension(1552, 849));
    {
      List<WebElement> elements = driver.findElements(By.linkText("See the comic"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("See the comic")).click();
    assertThat(driver.findElement(By.cssSelector("b:nth-child(4) .text-white")).getText(), is("Written by:"));
    assertThat(driver.findElement(By.cssSelector("b:nth-child(6) .text-white")).getText(), is("Series:"));
    assertThat(driver.findElement(By.cssSelector("b:nth-child(8) .text-white")).getText(), is("Price:"));
    {
      List<WebElement> elements = driver.findElements(By.cssSelector("h6:nth-child(5) span"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.cssSelector("h6:nth-child(7) span"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".text-white > span"));
      assert(elements.size() > 0);
    }
    vars.put("writer", driver.findElement(By.cssSelector("h6:nth-child(5) span")).getText());
    vars.put("series", driver.findElement(By.cssSelector("h6:nth-child(7) span")).getText());
    vars.put("issue", driver.findElement(By.cssSelector("h1")).getText());
    driver.findElement(By.cssSelector("h6:nth-child(5) span")).click();
    assertThat(driver.findElement(By.cssSelector("h1")).getText(), is(vars.get("writer").toString()));
    {
      List<WebElement> elements = driver.findElements(By.linkText(vars.get("issue").toString()));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText(vars.get("issue").toString())).click();
    driver.findElement(By.cssSelector("h6:nth-child(7) span")).click();
    assertThat(driver.findElement(By.cssSelector("h1")).getText(), is(vars.get("series").toString()));
    {
      List<WebElement> elements = driver.findElements(By.linkText(vars.get("issue").toString()));
      assert(elements.size() > 0);
    }
    assertThat(driver.findElement(By.cssSelector("b:nth-child(4) .text-white")).getText(), is("Publisher:"));
  }
}
