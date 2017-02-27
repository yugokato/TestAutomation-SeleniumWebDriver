package selenium.automation.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.messages.Container;

import framework.api.RestAPI;
import framework.driver.DriverInit;
import framework.pages.RegisterPage;
import framework.pages.TopPage;
import framework.pages.modal.Modal;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class BaseTest {
	protected static Logger logger;
    protected WebDriver driver;
    protected JavascriptExecutor jse;
    protected RestAPI restAPI; 
    protected TopPage topPage;
    protected Modal currentModal;
    protected RegisterPage registerPage;
    protected DockerClient docker;
    private static final String[] INITIAL_CONTAINERS = 
    	{"/vm01", "/vm02", "/vm03", "/vm04", "/vm05", "/vm06", 
    			"/vm07", "/vm08", "/vm09", "/vm10", "/mongo"};
    
    @BeforeClass(alwaysRun=true)
    public void beforeClassBase() {
    	logger = Logger.getLogger(getClass());
    	logger.setLevel(Level.ALL);
    	PropertyConfigurator.configure(System.getProperty("user.dir") + "/" + "lib/log4j.property");
    	
    	System.setProperty("webdriver.firefox.marionette","lib");
    	driver = new FirefoxDriver();
    	DriverInit.setDriver(driver);
        topPage = new TopPage();
        restAPI = new RestAPI();
        jse = (JavascriptExecutor)driver;
        try{
        	docker = DefaultDockerClient.fromEnv().build();
        }catch(DockerCertificateException e){
        	e.printStackTrace();
        }
    }
    
    @AfterClass(alwaysRun=true)
    public void afterClassBase() {
        driver.quit();
    }
    
    @BeforeMethod(alwaysRun=true)
    public void beforeMethodBase() throws Exception{
    	startTestContainers();
    	driver.get("http://localhost:5000");
    	driver.manage().window().maximize();
    }

    private void startTestContainers() throws Exception{
    	List<Container> containers = docker.listContainers(ListContainersParam.allContainers());
        for(Container container : containers) {
        	for( String name : container.names()){
        		if (! Arrays.asList(INITIAL_CONTAINERS).contains(name)){
            		docker.killContainer(container.id());
            		docker.removeContainer(container.id());
            		logger.info("Removed a container: " + name);
        		}
        		else if (! container.state().equals("running")){
            		docker.startContainer(container.id());
            		logger.info("Started a container: " + name);
            	}
            }
        }
    }
}
