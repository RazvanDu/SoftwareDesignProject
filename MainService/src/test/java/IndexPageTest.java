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


public class IndexPageTest {
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
  public void indexPageTest() {
    driver.get("http://localhost:1111/");
    driver.manage().window().setSize(new Dimension(1552, 849));
    assertThat(driver.getTitle(), is("Cosmics Store"));
    assertThat(driver.findElement(By.linkText("Cosmics Store")).getText(), is("Cosmics Store"));
    assertThat(driver.findElement(By.linkText("Login")).getText(), is("Login"));
    assertThat(driver.findElement(By.linkText("See the comic")).getText(), is("See the comic"));
    {
      List<WebElement> elements = driver.findElements(By.linkText("See the comic"));
      assert(elements.size() > 0);
    }
  }
}
