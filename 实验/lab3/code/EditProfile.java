import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Set;

public class EditProfile {
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

    private void editProfile(String[] args){
        driver.get("http://211.87.224.235:8800/modifypage.php");
        String[] name = new String[]{"nick","opassword","npassword","rptpassword","school","email"};
        WebElement submit = driver.findElement(By.name("submit"));

        for (int i = 0; i < name.length; i++) {
            WebElement we = driver.findElement(By.name(name[i]));
            we.sendKeys(args[i]);
        }

        submit.click();

        try{
            driver.getCurrentUrl();
            System.out.println("修改成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("------------------------------------");
    }

    @Test
    public void success(){
        editProfile(
                new String[]{
                        "lyh","P20021209","","","山东小学","3054117680@qq.com"
                }
        );
    }

    @Test
    public void old_password_error(){
        editProfile(
                new String[]{
                        "lyh","P2002120","","","社会主义加拿大美国墨西哥美洲联合洲立大学","3054117680@qq.com"
                }
        );
    }

    @Test
    public void school_name_too_long(){
        editProfile(
                new String[]{
                        "lyh","P20021209","","","社会主义加拿大美国墨西哥美洲联合洲立大学","3054117680@qq.com"
                }
        );
    }

    @Test
    public void success_with_change_pwd(){
        editProfile(
                new String[]{
                        "lyh","P20021209","P20021209","P20021209","山东小学","3054117680@qq.com"
                }
        );
    }

    @Test
    public void new_pwd_too_short(){
        editProfile(
                new String[]{
                        "lyh","P20021209","123","123","山东小学","3054117680@qq.com"
                }
        );
    }

    @Test
    public void repeat_fail(){
        editProfile(
                new String[]{
                        "lyh","P20021209","123456","123457","山东小学","3054117680@qq.com"
                }
        );
    }


}
