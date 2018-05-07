import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DMSTest {
    protected static WebDriver driver;
    protected static String baseUrl;
    public static Properties properties = TestProperties.getInstance().getProperties();
    private WebDriverWait wait = new WebDriverWait(driver, 3);
    private WebElement webElement;

    @BeforeClass
    public static void setUp() throws Exception {
        switch (properties.getProperty("browser")) {
            case "firefox":
                System.setProperty("webdriver.gecko.driver", properties.getProperty("webdriver.gecko.driver"));
                driver = new FirefoxDriver();
                break;
            case "chrome":
                System.setProperty("webdriver.chrome.driver", properties.getProperty("webdriver.chrome.driver"));
                driver = new ChromeDriver();
                break;
            default:
                System.setProperty("webdriver.chrome.driver", properties.getProperty("webdriver.chrome.driver"));
                driver = new ChromeDriver();
        }
        baseUrl = properties.getProperty("app.url");
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void DMStest() throws InterruptedException {
        driver.get(baseUrl);

        //Waiting loading page and clicking on the menu
        wait.until(ExpectedConditions.visibilityOf(webElement = driver.findElement(By.xpath("//div[@id=\"main-navbar-collapse\"]/ol/li[2]/a"))));
        webElement.click();


        //Clicking on the field
        webElement = driver.findElement(By.xpath("//li[@class=\"adv-analytics-navigation-line3-link\"]/a[@href=\"https://www.rgs.ru/products/private_person/health/dms/generalinfo/index.wbp\"]"));
        webElement.click();

        //Waiting while page is loading and choosing element for checking
        Thread.sleep(5000);
        webElement = driver.findElement(By.xpath("//div[@class=\"page-header\"]/span"));

        //Checking if this page is about DMS
        String checkStr = webElement.getText();
        Assert.assertTrue(checkStr.contains("Добровольное медицинское страхование"));

        //Clicking button for filling form
        webElement = driver.findElement(By.xpath("//a[contains(.,\"Отправить заявку\")]"));
        webElement.click();

        inputData();

        checkFields();

        //Submit
        driver.findElement(By.xpath("//div[@class=\"form-footer\"]/button")).click();

        checkEmail();
    }

    /**
     *  Typing data in different fields
     */
    private void inputData() throws InterruptedException {
        webElement = driver.findElement(By.xpath("//label[contains(.,\"Фамилия\")]/../input"));
        webElement.sendKeys("Петров");
        webElement = driver.findElement(By.xpath("//label[contains(.,\"Имя\")]/../input"));
        webElement.sendKeys("Василий");
        Thread.sleep(1000);
        webElement = driver.findElement(By.xpath("//label[contains(.,\"Отчество\")]/../input"));
        webElement.sendKeys("Магомедович");
        webElement = driver.findElement(By.xpath("//label[contains(.,\"Регион\")]/../select"));
        new Select(webElement).selectByVisibleText("Москва");
        webElement = driver.findElement(By.xpath("//label[contains(.,\"Телефон\")]/../input"));
        webElement.sendKeys("9273034527");
        webElement = driver.findElement(By.xpath("//label[contains(.,\"Эл. почта\")]/../input"));
        webElement.sendKeys("qwertyqwerty");
        webElement = driver.findElement(By.xpath("//label[contains(.,\"Комментарии\")]/../textarea"));
        webElement.sendKeys("12345");
        webElement = driver.findElement(By.xpath("//label[contains(.,\"Я согласен на обработку моих персональных данных, указанных выше и/или содержащихся в приложенных файлах, в целях расчета страховой премии.\")]/../input"));
        webElement.click();
        Thread.sleep(10000);
    }
    /**
     *  Checking fields
     */
    private void checkFields(){
        Assert.assertEquals("Петров",driver.findElement(By.xpath("//label[contains(.,\"Фамилия\")]/../input")).getAttribute("value"));
        Assert.assertEquals("Василий",driver.findElement(By.xpath("//label[contains(.,\"Имя\")]/../input")).getAttribute("value"));
        Assert.assertEquals("Магомедович",driver.findElement(By.xpath("//label[contains(.,\"Отчество\")]/../input")).getAttribute("value"));
        Assert.assertEquals("Москва",new Select(driver.findElement(By.xpath("//label[contains(.,\"Регион\")]/../select"))).getFirstSelectedOption().getText());
        Assert.assertEquals("+7 (927) 303-45-27",driver.findElement(By.xpath("//label[contains(.,\"Телефон\")]/../input")).getAttribute("value"));
        Assert.assertEquals("qwertyqwerty",driver.findElement(By.xpath("//label[contains(.,\"Эл. почта\")]/../input")).getAttribute("value"));
        Assert.assertEquals("12345",driver.findElement(By.xpath("//label[contains(.,\"Комментарии\")]/../textarea")).getAttribute("value"));
        Assert.assertEquals(true,driver.findElement(By.xpath("//label[contains(.,\"Я согласен на обработку моих персональных данных, указанных выше и/или содержащихся в приложенных файлах, в целях расчета страховой премии.\")]/../input")).isSelected());
    }
    /**
     * Checking email after submitting form
     */
    private void checkEmail(){
        Assert.assertTrue(driver.findElement(By.xpath("//label[contains(.,\"Эл. почта\")]/../div/label/span")).getText().contains("Введите адрес электронной почты"));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
    }
}
