import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

public class Label_Classify {
    WebDriver driver;

    @BeforeEach
    public void setUp(){
        driver = new FirefoxDriver();
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }

    private void classify(int index){
        driver.get("http://211.87.224.235:8800/category.php");
        List<WebElement> labels = driver.findElements(By.className("label"));

        labels.get(index).click();
        List<WebElement> td = driver.findElements(By.tagName("td"));
        int i = 0;
        while(i<td.size()){
            System.out.println(td.get(i).getText());
            i+=4;
        }
        System.out.println("------------------------------------");
    }

    @Test
    public void ds(){
        System.out.println("数据结构标签:");
        classify(0);
    }

    @Test
    public void a(){
        System.out.println("算法标签:");
        classify(1);
    }
}
