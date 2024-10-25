import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

public class Rank {
    WebDriver driver;

    @BeforeEach
    public void setUp(){
        driver = new FirefoxDriver();
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }

    private void rank_search(String id){
        driver.get("http://211.87.224.235:8800/ranklist.php");
        WebElement prefix = driver.findElement(By.name("prefix"));

        prefix.sendKeys(id);
        WebElement btn = driver.findElement(By.tagName("button"));
        btn.click();
        soutResult();
    }

    private void date_search(int index){
        driver.get("http://211.87.224.235:8800/ranklist.php");
        List<WebElement> as = driver.findElements(By.tagName("a"));
        as.get(index).click();
        soutResult();
    }

    private void soutResult(){
        int i = 1;
        List<WebElement> tds = driver.findElements(By.tagName("td"));
        while(i<tds.size()){
            System.out.println(tds.get(i).getText());
            i += 6;
        }
        System.out.println("------------------------------------");
    }

    @Test
    public void by_id(){
        System.out.println("用户名检索(本页):");
        rank_search("2021");
    }

    @Test
    public void by_day(){
        System.out.println("本日检索(本页):");
        date_search(10);
    }

    @Test
    public void by_week(){
        System.out.println("本周检索(本页):");
        date_search(10);
    }

    @Test
    public void by_month(){
        System.out.println("本月检索(本页):");
        date_search(10);
    }

    @Test
    public void by_year(){
        System.out.println("本年(本页):");
        date_search(10);
    }
}
