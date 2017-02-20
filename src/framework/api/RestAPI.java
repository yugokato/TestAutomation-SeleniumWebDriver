package framework.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RestAPI {
	public RestAPI(){}
	
	public String addMachine(String ipaddr, String username){
		String url = "curl -X 'POST' http://localhost:5000/add/" + ipaddr + ":" + username;
		StringBuffer output = new StringBuffer();
		
	    String[] cmd = { "/bin/sh", "-c", url };
	    String line;
	    try{
		    Process proc = Runtime.getRuntime().exec(cmd);
		    BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    while ((line = is.readLine()) != null) {
		    	output.append(line.trim());
		    }

	    }
	    catch(IOException e){
	    	System.out.println(e);
	    }
	    
	    System.out.printf("Added a machine(%s) via RESUful API: " + output.toString() + "\n", ipaddr);
	    return output.toString();
   
	}
	
	public String deleteMachine(String ipaddr){
		String url = "curl -X 'DELETE' http://localhost:5000/delete/" + ipaddr;
		StringBuffer output = new StringBuffer();
		
	    String[] cmd = { "/bin/sh", "-c", url };
	    String line;
	    try{
		    Process proc = Runtime.getRuntime().exec(cmd);
		    BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    while ((line = is.readLine()) != null) {
		    	output.append(line.trim());
		    }

	    }
	    catch(IOException e){
	    	System.out.println(e);
	    }
	    
	    System.out.printf("Deleted machines(%s) via RESUful API: " + output.toString() + "\n", ipaddr);
	    return output.toString();
   
	}
}
