import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class CodeJudge {
    WebDriver driver;

    @BeforeEach
    public void setUp(){
        driver = new FirefoxDriver();
        driver.get("http://211.87.224.235:8800/loginpage.php");
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("202100300063");
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("P20021209");
        WebElement submit = driver.findElement(By.name("submit"));
        submit.click();
        Set<Cookie> cookies = driver.manage().getCookies();
        for (Cookie cookie : cookies) {
            driver.manage().addCookie(cookie);
        }
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }

    private String readCode(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        List<String> lines = Files.readAllLines(path);
        return String.join(System.lineSeparator(),lines);
    }

    private void getPage(String id){
        driver.get("http://211.87.224.235:8800/submitpage.php?id="+id);
    }

    private WebElement[] lang_code_submit(){
        WebElement[] res = new WebElement[3];
        res[0] = driver.findElement(By.id("language"));
        res[1] = driver.findElement(By.tagName("textarea"));
        res[2] = driver.findElement(By.id("Submit"));
        return res;
    }

    private void fillAndSubmit(String pageId,String lang,String code_Path,String purpose){
        System.out.println(purpose);
        getPage(pageId);

        WebElement[] res = lang_code_submit();
        WebElement langSelector = res[0];
        WebElement textarea = res[1];
        WebElement submit = res[2];

        langSelector.sendKeys(lang);

        try {
            String text = readCode(code_Path);
            text = text.replaceAll("[\r\n]", "");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.opacity = 1;",textarea); // 神之一手
            textarea.sendKeys(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        submit.click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> result = driver.findElements(By.className("label"));
        System.out.println(result.get(0).getText());
        System.out.println("------------------------------------");
    }

    @Test
    public void CE(){
        fillAndSubmit("1000","C","D:\\workSpace\\st\\lab3_selenium_webdriver\\src\\test\\java\\source\\1000_CE","CE");
    }

    @Test
    public void MLE(){
        fillAndSubmit("1000","Java","D:\\workSpace\\st\\lab3_selenium_webdriver\\src\\test\\java\\source\\1000_MLE","MLE");
    }

    @Test
    public void TLE(){
        fillAndSubmit("1003","Python","D:\\workSpace\\st\\lab3_selenium_webdriver\\src\\test\\java\\source\\1003_TLE","TLE");
    }

    @Test
    public void RE(){
        fillAndSubmit("1004","Python","D:\\workSpace\\st\\lab3_selenium_webdriver\\src\\test\\java\\source\\1004_RE","RE");
    }

    @Test
    public void AC(){
        fillAndSubmit("1008","Java","D:\\workSpace\\st\\lab3_selenium_webdriver\\src\\test\\java\\source\\1008_AC","AC");
    }

    @Test
    public void PE(){
        fillAndSubmit("1006","Java","D:\\workSpace\\st\\lab3_selenium_webdriver\\src\\test\\java\\source\\1006_PE","PE");
    }

    @Test
    public void WA(){
        fillAndSubmit("1005","Java","D:\\workSpace\\st\\lab3_selenium_webdriver\\src\\test\\java\\source\\1005_WA","WA");
    }


}
