package framework.driver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverInit {
    private static WebDriver driver;
    
    public static void setDriver(WebDriver tempDriver){
        driver = tempDriver;
    }
    
    public static WebDriver getDriver(){
        return driver;
    }
    
    public static DesiredCapabilities setChromeCapabilities(){
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        capabilities.setCapability("chrome.switches", Arrays.asList("--incognito"));
        
        return capabilities;
    }
    
    
}