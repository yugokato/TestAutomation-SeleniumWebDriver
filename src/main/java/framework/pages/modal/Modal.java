package framework.pages.modal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import framework.pages.BasePage;

public class Modal extends BasePage {
    private final WebElement currentModal;
    
    public Modal(WebElement currentModal){
        this.currentModal = currentModal;
    }
    
    private String getLastUpdated(){
        String selector = "div.modal-body > div.table-responsive > div:nth-child(2)";
        String lastUpdated = currentModal.findElement(By.cssSelector(selector)).getText();
        return lastUpdated;
    }
    
    private String getHostName(){
        String hostname = currentModal.findElement(By.cssSelector("div.modal-header h4")).getText();
        return hostname;
    }
    
    private String getStatus(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(2) > td:nth-child(2)";
        String status = currentModal.findElement(By.cssSelector(selector)).getText();
        return status;
    }
    
    private String getStatusImgName(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(2) > td:nth-child(2) > img";
        String statusImgName = currentModal.findElement(By.cssSelector(selector)).getAttribute("src");
        Pattern p = Pattern.compile("^(http://localhost:5000/static/images/)(.*)");
        Matcher m = p.matcher(statusImgName);
        if (m.find()){
            return m.group(2);
        }
        return null;
    }

    private String getOSDistribution(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(3) > td:nth-child(2)";
        String osDistribution = currentModal.findElement(By.cssSelector(selector)).getText();
        return osDistribution;
    }
    
    private String getRelease(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(4) > td:nth-child(2)"; 
        String release = currentModal.findElement(By.cssSelector(selector)).getText();
        return release;
    }
    
    private String getIpAddress(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(5) > td:nth-child(2)";
        String ipaddr = currentModal.findElement(By.cssSelector(selector)).getText(); 
        return ipaddr;
    }
    
    private String getMacAddress(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(6) > td:nth-child(2)";
        String macAddress = currentModal.findElement(By.cssSelector(selector)).getText();
        return macAddress;
    }
    
    private String getUptime(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(7) > td:nth-child(2)";
        String uptime = currentModal.findElement(By.cssSelector(selector)).getText();
        return uptime;
    }
    
    private String getCPULoadAvg(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(8) > td:nth-child(2)";
        String cpuLoadAvg = currentModal.findElement(By.cssSelector(selector)).getText();
        return cpuLoadAvg;
    }
    
    private String getMemoryUsage(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(9) > td:nth-child(2)";
        String memoryUsage = currentModal.findElement(By.cssSelector(selector)).getText();
        return memoryUsage;
    }
    
    private String getDiskUsage(){
        String selector = "div.modal-body > div.table-responsive > table > tbody > tr:nth-child(10) > td:nth-child(2)";
        String diskUsage = currentModal.findElement(By.cssSelector(selector)).getText();
        return diskUsage;
    }
    
    private WebElement getOpenSSHButton(){
        String selector = "div.modal-body  form > button";
        WebElement openSSHButton = currentModal.findElement(By.cssSelector(selector));
        return openSSHButton;
    }
    
    private WebElement getExportJsonFileButton(){
        String selector = "div.modal-footer > a";
        WebElement exportJsonFileButton = currentModal.findElement(By.cssSelector(selector));
        return exportJsonFileButton;
    }
    
    public Map<String, String> getModalContents(){
        Map<String, String> modalContents = new HashMap<>();
        modalContents.put("LAST_UPDATED", getLastUpdated());
        modalContents.put("HOST_NAME", getHostName());
        modalContents.put("IP_ADDRESS", getIpAddress());
        modalContents.put("STATUS", getStatus());
        modalContents.put("STATUS_IMG_NAME", getStatusImgName());
        modalContents.put("OS_DISTRIBUTION", getOSDistribution());
        modalContents.put("RELEASE", getRelease());
        modalContents.put("MAC_ADDRESS", getMacAddress());
        modalContents.put("UPTIME", getUptime());
        modalContents.put("CPU_LOAD_AVG", getCPULoadAvg());
        modalContents.put("MEMORY_USAGE", getMemoryUsage());
        modalContents.put("DISK_USAGE", getDiskUsage());        
        
        return modalContents;
    }
    
    public WebElement getJsonFileNameField(){
        String selector = "input.form-control";
        WebElement jsonFileNameField = currentModal.findElement(By.cssSelector(selector));
        return jsonFileNameField;
    }
    
    public void clickCloseButton(){
        String selector = "div.modal-footer > button";
        WebElement closeButton = currentModal.findElement(By.cssSelector(selector));
        closeButton.click();
    }
    
    public void clickExportJsonFileButton(){
        getExportJsonFileButton().click();
    }
    
    public void enterJsonFileName(String filename){
        getJsonFileNameField().clear();
        getJsonFileNameField().sendKeys(filename);
        wait.until(ExpectedConditions.attributeToBe(getJsonFileNameField(), "value", filename));
    }
    
    public void clickExportButton(){
        String selector = "div.modal-body > form > button";
        WebElement exportButton = currentModal.findElement(By.cssSelector(selector));
        exportButton.click();
    }

    public void clickOpenSSHButton(){
        getOpenSSHButton().click();
    }
    
    public boolean isExportJsonFileButtonExists(){
        try{
            getExportJsonFileButton();
            return true;
        }catch(NoSuchElementException e){
            return false;
        }
    }
    
    public boolean isOpenSSHButtonExists(){
        try{
            getOpenSSHButton();
            return true;
        }catch(NoSuchElementException e){
            return false;
        }
    }
    
    public boolean isButterflyRunning(){
        String[] cmd = { "/bin/sh", "-c", "ps aux | grep butterfly" };
        String line;
        try{
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((line = is.readLine()) != null) {
                if (line.contains("butterfly.server.py")){
                    return true;
                }
            }
            return false;
        }
        catch(IOException e){
            System.out.println(e);
            return false;
        }
    }
}
