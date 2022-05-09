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


public class AddAndDeleteComicTest {

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
  public void addAndDeleteComicTest() {
    driver.get("http://localhost:1111/");
    driver.manage().window().setSize(new Dimension(1536, 835));
    assertThat(driver.getTitle(), is("Cosmics Store"));
    {
      List<WebElement> elements = driver.findElements(By.linkText("Login"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("Login")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("eu@gmail.com");
    driver.findElement(By.id("pwd")).sendKeys("12345678");
    driver.findElement(By.id("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".navbar")).getText(), is("Cosmics Store\nAdd comic\nCart Logout Hello euu nueu"));
    {
      List<WebElement> elements = driver.findElements(By.linkText("Add comic"));
      assert(elements.size() > 0);
    }
    vars.put("comics", driver.findElement(By.cssSelector(".row")).getText());
    driver.findElement(By.linkText("Add comic")).click();
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(1) .text-white")).getText(), is("Title:"));
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(2) .text-white")).getText(), is("Select series:"));
    {
      List<WebElement> elements = driver.findElements(By.id("sel1"));
      assert(elements.size() > 0);
    }
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(3) .text-white")).getText(), is("Issue number:"));
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(4) .text-white")).getText(), is("Price:"));
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(5) .text-white")).getText(), is("Select writer:"));
    {
      List<WebElement> elements = driver.findElements(By.id("writer"));
      assert(elements.size() > 0);
    }
    assertThat(driver.findElement(By.cssSelector(".form-group:nth-child(6) .text-white")).getText(), is("Preview link:"));
    assertThat(driver.findElement(By.cssSelector(".form-label > .text-white")).getText(), is("Choose cover:"));
    {
      List<WebElement> elements = driver.findElements(By.id("cover"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.id("submit"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.id("title")).click();
    driver.findElement(By.id("title")).sendKeys("seleniumTest");
    driver.findElement(By.id("sel1")).click();
    {
      WebElement dropdown = driver.findElement(By.id("sel1"));
      dropdown.findElement(By.xpath("//option[. = 'Future state']")).click();
    }
    driver.findElement(By.id("issue")).click();
    driver.findElement(By.id("issue")).sendKeys("5");
    driver.findElement(By.id("price")).click();
    driver.findElement(By.id("price")).sendKeys("2.22");
    driver.findElement(By.id("writer")).click();
    {
      WebElement dropdown = driver.findElement(By.id("writer"));
      dropdown.findElement(By.xpath("//option[. = 'Joshua Williamson']")).click();
    }
    driver.findElement(By.id("preview")).click();
    driver.findElement(By.id("preview")).click();
    driver.findElement(By.id("preview")).sendKeys("localhost:1111/addcomic");
    driver.findElement(By.id("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".row")).getText(), is(vars.get("comics").toString() + "\nseleniumTest\nJoshua Williamson\nPrice: $2.22\nSee the comic"));
    driver.findElement(By.cssSelector(".card:nth-last-child(1) .btn")).click();
    assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("seleniumTest"));
    assertThat(driver.findElement(By.cssSelector("b:nth-child(4) .text-white")).getText(), is("Written by:"));
    assertThat(driver.findElement(By.cssSelector("b:nth-child(6) .text-white")).getText(), is("Series:"));
    assertThat(driver.findElement(By.cssSelector("b:nth-child(8) .text-white")).getText(), is("Price:"));
    assertThat(driver.findElement(By.cssSelector(".text-white > span")).getText(), is("2.22"));
    assertThat(driver.findElement(By.cssSelector("h6:nth-child(5) span")).getText(), is("Joshua Williamson"));
    assertThat(driver.findElement(By.cssSelector("h6:nth-child(7) span")).getText(), is("Future state"));
    driver.findElement(By.cssSelector("h6:nth-child(5) span")).click();
    {
      List<WebElement> elements = driver.findElements(By.linkText("seleniumTest"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("seleniumTest")).click();
    driver.findElement(By.cssSelector("h6:nth-child(7) span")).click();
    {
      List<WebElement> elements = driver.findElements(By.linkText("seleniumTest"));
      assert(elements.size() > 0);
    }
    driver.findElement(By.linkText("seleniumTest")).click();
    driver.findElement(By.linkText("Logout")).click();
    driver.findElement(By.cssSelector(".navbar")).click();
    driver.findElement(By.linkText("Login")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("duck@gmail.com");
    driver.findElement(By.id("pwd")).sendKeys("12345678");
    driver.findElement(By.id("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".navbar")).getText(), is("Cosmics Store\nCart Logout Hello Admin Admin"));
    assertThat(driver.findElement(By.cssSelector(".card:nth-last-child(1) > .card-body > div > .btn")).getText(), is("Remove the comic"));
    driver.findElement(By.cssSelector(".card:nth-last-child(1) > .card-body > div > .btn")).click();
    driver.findElement(By.linkText("Logout")).click();
    assertThat(driver.findElement(By.cssSelector(".row")).getText(), is(vars.get("comics").toString()));
    driver.get("http://localhost:1111/writers?id=7");
    {
      List<WebElement> elements = driver.findElements(By.linkText("seleniumTest"));
      assert(elements.size() == 0);
    }
    driver.get("http://localhost:1111/series?id=5");
    {
      List<WebElement> elements = driver.findElements(By.linkText("seleniumTest"));
      assert(elements.size() == 0);
    }
  }
}
