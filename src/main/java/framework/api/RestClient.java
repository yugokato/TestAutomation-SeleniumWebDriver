package framework.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class RestClient {
	private static final Logger logger = Logger.getLogger(RestClient.class);
	
	public RestClient(){
		logger.setLevel(Level.ALL);
	}
	
	protected String requestPOST(String urlStr, String data){
		StringBuffer output = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			if (data != null){
				OutputStream os = conn.getOutputStream();
				os.write(data.getBytes());
				os.flush();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String line;
			while ((line = br.readLine()) != null) {
				output.append(line.trim());
			}
			conn.disconnect();

		  } catch (MalformedURLException e) {
			e.printStackTrace();
		  } catch (IOException e) {
			e.printStackTrace();
		  }
		
		if (data != null){
			logger.info("POST: " + urlStr + " with data " + data);
		}
		else{
			logger.info("POST: " + urlStr);
		}
		
		return output.toString();
	}
	
	protected String requestDELETE(String urlStr, String data){
		StringBuffer output = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("Content-Type", "application/json");
			
			if (data != null){
				OutputStream os = conn.getOutputStream();
				os.write(data.getBytes());
				os.flush();
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String line;
			while ((line = br.readLine()) != null) {
				output.append(line.trim());
			}
			conn.disconnect();

		  } catch (MalformedURLException e) {
			e.printStackTrace();
		  } catch (IOException e) {
			e.printStackTrace();
		  }
		
		if (data != null){
			logger.info("POST: " + urlStr + " with data " + data);
		}
		else{
			logger.info("POST: " + urlStr);
		}
		
		return output.toString();
	}
}
