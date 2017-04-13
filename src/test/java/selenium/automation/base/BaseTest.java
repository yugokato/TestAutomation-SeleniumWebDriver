package selenium.automation.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.spotify.docker.client.DockerClient;

import framework.api.RestAPI;
import framework.driver.DriverInit;
import framework.pages.LoginPage;
import framework.pages.TopPage;
import framework.docker.DockerManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class BaseTest {
    protected final static Logger logger = Logger.getLogger(BaseTest.class);;
    protected WebDriver driver;
    protected RestAPI restAPI;
    protected LoginPage loginPage;
    protected TopPage topPage;
    protected DockerManager containerManager;
    protected DockerClient docker;
    private static final String TEST_ADMIN_USER = "ADMIN_USER";
    private static final String TEST_ADMIN_PASSWORD = "ADMIN_PASSWORD";
    private static final String[] INITIAL_CONTAINERS = 
        {"/vm01", "/vm02", "/vm03", "/vm04", "/vm05", "/vm06", 
                "/vm07", "/vm08", "/vm09", "/vm10", "/mongo"};

    
    @BeforeClass(alwaysRun=true)
    public void beforeClassBase() {
        PropertyConfigurator.configure(System.getProperty("user.dir") + "/" + "lib/log4j.property");
        System.setProperty("webdriver.firefox.marionette","lib");
        DriverInit.setDriver(new FirefoxDriver());
        driver = DriverInit.getDriver();
        restAPI = new RestAPI();
        loginPage = new LoginPage();
        topPage = new TopPage();
        docker = DockerManager.getDockerClient();
        restAPI.addUser(TEST_ADMIN_USER, TEST_ADMIN_PASSWORD);
    }
    
    @BeforeMethod(alwaysRun=true)
    public void beforeMethodBase() throws Exception{
        DockerManager.startTestContainers(INITIAL_CONTAINERS);
        driver.get("http://localhost:5000");
        driver.manage().window().maximize();
        driver.navigate().refresh();
        loginPage.doLogin(TEST_ADMIN_USER, TEST_ADMIN_PASSWORD);
    }

    @AfterClass(alwaysRun=true)
    public void afterClassBase() {
        restAPI.deleteAllUnknownMachines();
        restAPI.deleteUser(TEST_ADMIN_USER);
        driver.get("http://localhost:5000/logout");
        driver.quit();
    }
}
