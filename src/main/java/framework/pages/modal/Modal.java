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
    
    public String getLastUpdated(){
        String selector = "div.modal-body > div:nth-child(2)";
        String lastUpdated = currentModal.findElement(By.cssSelector(selector)).getText();
        return lastUpdated;
    }
    
    private String getHostName(){
        String hostname = currentModal.findElement(By.cssSelector("div.modal-header h4")).getText();
        return hostname;
    }
    
    private String getStatus(){
        clickBasicTab();
        String selector = "div.modal-body div.table-responsive > table > tbody > tr:nth-child(2) > td:nth-child(2)";
        String status = currentModal.findElement(By.cssSelector(selector)).getText();
        return status;
    }
    
    private String getStatusImgName(){
        clickBasicTab();
        String selector = "div.modal-body div.table-responsive > table > tbody > tr:nth-child(2) > td:nth-child(2) > img";
        String statusImgName = currentModal.findElement(By.cssSelector(selector)).getAttribute("src");
        Pattern p = Pattern.compile("^(.*/images/)(.*)");
        Matcher m = p.matcher(statusImgName);
        if (m.find()){
            return m.group(2);
        }
        return null;
    }

    private String getOSDistribution(){
        clickBasicTab();
        String selector = "div.modal-body div.table-responsive > table > tbody > tr:nth-child(3) > td:nth-child(2)";
        String osDistribution = currentModal.findElement(By.cssSelector(selector)).getText();
        return osDistribution;
    }
    
    private String getRelease(){
        clickBasicTab();
        String selector = "div.modal-body div.table-responsive > table > tbody > tr:nth-child(4) > td:nth-child(2)"; 
        String release = currentModal.findElement(By.cssSelector(selector)).getText();
        return release;
    }
    
    private String getIpAddress(){
        clickBasicTab();
        String selector = "div.modal-body div.table-responsive > table > tbody > tr:nth-child(5) > td:nth-child(2)";
        String ipaddr = currentModal.findElement(By.cssSelector(selector)).getText(); 
        return ipaddr;
    }
    
    private String getMacAddress(){
        clickBasicTab();
        String selector = "div.modal-body div.table-responsive > table > tbody > tr:nth-child(6) > td:nth-child(2)";
        String macAddress = currentModal.findElement(By.cssSelector(selector)).getText();
        return macAddress;
    }
    
    private String getUptime(){
        clickBasicTab();
        String selector = "div.modal-body div.table-responsive > table > tbody > tr:nth-child(7) > td:nth-child(2)";
        String uptime = currentModal.findElement(By.cssSelector(selector)).getText();
        return uptime;
    }
    
    private String getCPUInfo(){
        clickCpuTab();
        String selector = "div.tab-content div:nth-child(2) .table-responsive";
        String cpuInfo = currentModal.findElements(By.cssSelector(selector)).get(0).getText();
        return cpuInfo;
    }
    
    private String getCPULoadAvg(){
        clickCpuTab();
        String selector = "div.tab-content div:nth-child(2) .table-responsive";
        String cpuLoadAvg = currentModal.findElements(By.cssSelector(selector)).get(1).getText();
        return cpuLoadAvg;
    }
    
    private String getMemoryUsage(){
        clickMemoryTab();
        String selector = "div.tab-content div:nth-child(3) .table-responsive";
        String memoryUsage = currentModal.findElement(By.cssSelector(selector)).getText();
        return memoryUsage;
    }
    
    private String getDiskUsage(){
        clickDiskTab();
        String selector = "div.tab-content div:nth-child(4) .table-responsive";
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
        clickBasicTab();
        modalContents.put("LAST_UPDATED", getLastUpdated());
        modalContents.put("HOST_NAME", getHostName());
        modalContents.put("IP_ADDRESS", getIpAddress());
        modalContents.put("STATUS", getStatus());
        modalContents.put("STATUS_IMG_NAME", getStatusImgName());
        modalContents.put("OS_DISTRIBUTION", getOSDistribution());
        modalContents.put("RELEASE", getRelease());
        modalContents.put("MAC_ADDRESS", getMacAddress());
        modalContents.put("UPTIME", getUptime());
        clickCpuTab();
        modalContents.put("CPU_INFO", getCPUInfo());
        modalContents.put("CPU_LOAD_AVG", getCPULoadAvg());
        clickMemoryTab();
        modalContents.put("MEMORY_USAGE", getMemoryUsage());
        clickDiskTab();
        modalContents.put("DISK_USAGE", getDiskUsage());        
        
        return modalContents;
    }
    
    public WebElement getJsonFileNameField(){
        String selector = "input.form-control";
        WebElement jsonFileNameField = currentModal.findElement(By.cssSelector(selector));
        return jsonFileNameField;
    }
    
    public WebElement getEC2ControlButton(){
        String selector = "a[id^=aws-ec2-button]";
        WebElement EC2ControlButton = currentModal.findElement(By.cssSelector(selector));
        return EC2ControlButton;
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
    
    public void clickBasicTab(){
        String selector = ".nav.nav-tabs > li:nth-child(1)";
        WebElement basicTab = currentModal.findElement(By.cssSelector(selector));
        basicTab.click();
    }
    
    public void clickCpuTab(){
        String selector = ".nav.nav-tabs > li:nth-child(2)";
        WebElement cpuTab = currentModal.findElement(By.cssSelector(selector));
        cpuTab.click();
    }
    
    public void clickMemoryTab(){
        String selector = ".nav.nav-tabs > li:nth-child(3)";
        WebElement memoryTab = currentModal.findElement(By.cssSelector(selector));
        memoryTab.click();
    }
    
    public void clickDiskTab(){
        String selector = ".nav.nav-tabs > li:nth-child(4)";
        WebElement diskTab = currentModal.findElement(By.cssSelector(selector));
        diskTab.click();
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
            Thread.sleep(500);
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
        }
        catch (InterruptedException e){}
        return false;
    }
    
    public boolean isEC2ControlButtonExists(){
        try{
            getEC2ControlButton();
            return true;
        }catch(NoSuchElementException e){
            return false;
        }
    }
}
