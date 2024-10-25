import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Register {

    WebDriver driver;

    @BeforeEach
    public void setUp(){
        driver = new FirefoxDriver();
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }

    private void register(String[] args,String purpose){
        driver.get("http://211.87.224.235:8800/registerpage.php");

        WebElement[] WE = new WebElement[6];
        String[] name = new String[]{"user_id","nick","password","rptpassword","school","email"};
        for (int i = 0; i < WE.length; i++) {
            WE[i] = driver.findElement(By.name(name[i]));
            WE[i].sendKeys(args[i]);
        }

        WebElement submit = driver.findElement(By.name("submit"));
        submit.click();

        String currentUrl;
        try{
            currentUrl = driver.getCurrentUrl();
            System.out.println("注册成功");
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }

        System.out.println("------------------------------------");
    }

    @Test
    public void success(){
        register(
                new String[]{"135859","zhl","123456","123456","sdu","2761991408@qq.com"},"success"
        );
    }

    @Test
    public void id_too_short(){
        register(
                new String[]{"8u","zhl","123456","123456","sdu","2761991408@qq.com"},"id_too_short"
        );
    }

    @Test
    public void user_exist(){
        register(
                new String[]{"1358598","zhl","123456","123456","sdu","2761991408@qq.com"},"user_exist"
        );
    }

    @Test
    public void pwd_too_short(){
        register(
                new String[]{"13585984","zhl","12345","12345","sdu","2761991408@qq.com"},"pwd_too_short"
        );
    }

    @Test
    public void rpt_not_consistent(){
        register(
                new String[]{"13585985","zhl","123456","123457","sdu","2761991408@qq.com"},"rpt_not_consistent"
        );
    }

    @Test
    public void school_too_long(){
        register(
                new String[]{"13585986","zhl","123456","123456","sdusdusdusdusdusdusdu","2761991408@qq.com"},"school_too_long"
        );
    }
}
