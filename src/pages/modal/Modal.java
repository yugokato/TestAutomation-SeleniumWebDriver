package pages.modal;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pages.BasePage;

public class Modal extends BasePage {
	private WebElement currentModal;
	
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
	
    public void clickCloseButton() throws Exception{
    	String selector = "div.modal-footer > button";
    	WebElement closeButton = currentModal.findElement(By.cssSelector(selector));
    	closeButton.click();
    	Thread.sleep(300);
    }
}


