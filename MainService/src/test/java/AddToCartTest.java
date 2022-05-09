import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.*;


public class AddToCartTest {

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
  public void addToCartTest() {
    driver.get("http://localhost:1111/");
    driver.manage().window().setSize(new Dimension(1552, 849));
    assertThat(driver.getTitle(), is("Cosmics Store"));
    {
      List<WebElement> elements = driver.findElements(By.linkText("Login"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("Login")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("notsel@ymail.com");
    driver.findElement(By.id("pwd")).sendKeys("12345678");
    driver.findElement(By.id("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".navbar")).getText(), is("Cosmics Store\nCart Logout Hello client not sell"));
    {
      List<WebElement> elements = driver.findElements(By.linkText("See the comic"));
      assert(elements.size() > 0);
    }
    vars.put("comics", driver.findElement(By.cssSelector(".row")).getText());
    driver.findElement(By.linkText("See the comic")).click();
    vars.put("comic1", driver.findElement(By.cssSelector("h1")).getText());
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".text-white > span"));
      assert(elements.size() > 0);
    }
    vars.put("price1", driver.findElement(By.cssSelector(".text-white > span")).getText());
    {
      List<WebElement> elements = driver.findElements(By.id("cart"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.id("cart")).click();
    driver.findElement(By.id("cart")).click();
    driver.findElement(By.linkText("Cosmics Store")).click();
    driver.findElement(By.cssSelector(".card:nth-child(3) .btn")).click();
    vars.put("comic2", driver.findElement(By.cssSelector("h1")).getText());
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".text-white > span"));
      assert(elements.size() > 0);
    }
    vars.put("price2", driver.findElement(By.cssSelector(".text-white > span")).getText());
    {
      List<WebElement> elements = driver.findElements(By.linkText("Add to cart"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("Add to cart")).click();
    driver.findElement(By.linkText("Cart")).click();
    assertThat(driver.findElement(By.cssSelector(".card:nth-child(7) .card-title")).getText(), is(vars.get("comic1").toString()));
    assertThat(driver.findElement(By.cssSelector(".card:nth-child(8) .card-title")).getText(), is(vars.get("comic2").toString()));
    assertThat(driver.findElement(By.cssSelector(".card:nth-child(7) h8:nth-child(2) h8")).getText(), is(vars.get("price1").toString()));
    assertThat(driver.findElement(By.cssSelector(".card:nth-child(8) h8:nth-child(2) h8")).getText(), is(vars.get("price2").toString()));
    assertThat(driver.findElement(By.cssSelector(".card:nth-child(7) h8:nth-child(3) h8")).getText(), is("2"));
    assertThat(driver.findElement(By.cssSelector(".card:nth-child(8) h8:nth-child(3) h8")).getText(), is("1"));
    vars.put("total", js.executeScript("return (parseFloat(arguments[0]) + parseFloat(2 * arguments[1])).toFixed(2)", vars.get("price2"),vars.get("price1")));
    assertThat(driver.findElement(By.cssSelector(".text-white:nth-child(1)")).getText(), is("Total price: $" + vars.get("total").toString()));
    {
      List<WebElement> elements = driver.findElements(By.linkText("See comic"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.linkText("Add more"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.linkText("Remove one"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.id("buy"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.cssSelector(".card:nth-child(8) .btn-danger")).click();
    assertThat(driver.findElement(By.cssSelector(".card:nth-child(7) .card-title")).getText(), is(not(vars.get("comic2").toString())));
    vars.put("total", js.executeScript("return (2 * arguments[0]).toFixed(2)", vars.get("price1")));
    assertThat(driver.findElement(By.cssSelector(".text-white:nth-child(1)")).getText(), is("Total price: $" + vars.get("total").toString()));
    driver.findElement(By.linkText("Add more")).click();
    assertThat(driver.findElement(By.cssSelector("h8:nth-child(3) h8")).getText(), is("3"));
    vars.put("total", js.executeScript("return (3 * arguments[0]).toFixed(2)", vars.get("price1")));
    assertThat(driver.findElement(By.cssSelector(".text-white:nth-child(1)")).getText(), is("Total price: $" + vars.get("total").toString()));
    driver.findElement(By.linkText("See comic")).click();
    assertThat(driver.findElement(By.cssSelector("h1")).getText(), is(vars.get("comic1").toString()));
    driver.findElement(By.linkText("Cart")).click();
    driver.findElement(By.id("buy")).click();

    //driver.findElement(By.id("address")).click();
    //driver.findElement(By.id("address")).sendKeys("aa");
    execById("address", "null");

    //driver.findElement(By.id("card")).click();
    //driver.findElement(By.id("card")).sendKeys("aa");
    execById("card", "null");

    //driver.findElement(By.id("cvc")).click();
    //driver.findElement(By.id("cvc")).sendKeys("aa");
    execById("cvc", "null");

    //driver.findElement(By.id("message")).click();
    //driver.findElement(By.id("message")).sendKeys("aa");
    execById("message", "null");

    //driver.findElement(By.cssSelector(".form-group > .btn-success")).click();
    execByCss(".form-group > .btn-success");

    driver.findElement(By.cssSelector(".btn-secondary:nth-child(1)")).click();
    driver.findElement(By.linkText("Cart")).click();
    assertThat(driver.findElement(By.cssSelector("html")).getText(), is("Cosmics Store\nCart Logout Hello client not sell"));
    driver.findElement(By.linkText("Logout")).click();
  }

  public void execById(String id, String inputText) {
    WebElement myElement = driver.findElement(By.id(id));
    String js = "arguments[0].setAttribute('value','"+inputText+"')";
    ((JavascriptExecutor) driver).executeScript(js, myElement);
  }

  public void execByCss(String id) {
    WebElement myElement = driver.findElement(By.cssSelector(id));
    String js = "arguments[0].click();";
    ((JavascriptExecutor) driver).executeScript(js, myElement);
  }

}
