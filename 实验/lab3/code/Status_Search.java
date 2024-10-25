import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

public class Status_Search {
    WebDriver driver;

    @BeforeEach
    public void setUp(){
        driver = new FirefoxDriver();
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }

    private void s_search(String[] args){
        driver.get("http://211.87.224.235:8800/status.php");
        String[] name = new String[]{"problem_id","user_id","language","jresult"};
        for (int i = 0; i < name.length; i++) {
            WebElement we = driver.findElement(By.name(name[i]));
            we.sendKeys(args[i]);
        }

        WebElement btn = driver.findElement(By.tagName("button"));
        btn.click();

        List<WebElement> tds = driver.findElements(By.tagName("td"));
        int i = 0;
        System.out.println("提交编号(本页):");
        while(i<tds.size()){
            System.out.println(tds.get(i).getText());
            i+=10;
        }
        System.out.println("------------------------------------");
    }

    @Test
    public void id_error(){
        System.out.println("999题202100300063用户用All All");
        s_search(
                new String[]{
                        "999","202100300063","All","All"
                }
        );
    }

    @Test
    public void uid_error(){
        System.out.println("1000题202100300063用户用Java AC");
        s_search(
                new String[]{
                        "1000","202100300062","Java","正确"
                }
        );
    }

    @Test
    public void by_id(){
        System.out.println("1001题用Java WA");
        s_search(
                new String[]{
                        "1001","","Java","答案错误"
                }
        );
    }

    @Test
    public void by_uid(){
        System.out.println("202100300063用户用Java AC");
        s_search(
                new String[]{
                        "","202100300063","Java","正确"
                }
        );
    }
}
