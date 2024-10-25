import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Login {
    WebDriver driver;

    @BeforeEach
    public void setUp(){
        driver = new FirefoxDriver();
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }

    private void login(String[] args){
        driver.get("http://211.87.224.235:8800/loginpage.php");
        String[] input = new String[]{"username","password"};
        for (int i = 0; i < input.length; i++) {
            WebElement we = driver.findElement(By.id(input[i]));
            we.sendKeys(args[i]);
        }

        WebElement su = driver.findElement(By.name("submit"));
        su.click();

        try{
            driver.getCurrentUrl();
            System.out.println("登录成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("------------------------------------");
    }

    @Test
    public void success(){
        login(
                new String[]{
                        "202100300063","P20021209"
                }
        );
    }

    @Test
    public void pwd_error(){
        login(
                new String[]{
                        "202100300063","123456"
                }
        );
    }

    @Test
    public void user_not_exist(){
        login(
                new String[]{
                        "bucunzaizhiren","P2002129"
                }
        );
    }

}
