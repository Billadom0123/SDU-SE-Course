import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Question_Search {
    WebDriver driver;

    @BeforeEach
    public void setUp(){
        driver = new FirefoxDriver();
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }

    private void q_search(String tid,String name,boolean click){
        driver.get("http://211.87.224.235:8800/problemset.php");

        WebElement search = driver.findElement(By.name("search"));
        WebElement id = driver.findElement(By.name("id"));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        if(click){
            js.executeScript(
                    "localStorage.setItem(arguments[0], arguments[1]);", "show tag", "0"
            );
        }else{
            js.executeScript(
                    "localStorage.setItem(arguments[0], arguments[1]);", "show tag", "1"
            );
        }


        id.sendKeys(tid);
        search.sendKeys(name);
        id.sendKeys(Keys.ENTER);

        try{
            if(driver.findElements(By.tagName("div")).get(4).getText().startsWith("题目当前不可用")){
                System.out.println("检索失败");
            }else{
                System.out.println("检索成功");
            }
        }catch (Exception e){
            System.out.println("检索成功");
        }


        System.out.println("------------------------------------");
    }

    @Test
    public void search_by_id(){
        q_search("1000","迷宫",true);
    }

    @Test
    public void search_invalid_id(){
        q_search("999","迷宫",false);
    }

    @Test
    public void search_invalid_name(){
        q_search("1000","标题",true);
    }
}
