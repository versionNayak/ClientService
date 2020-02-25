package com.finlabs.finexa.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class RestClient {
	
	private static Logger log = LoggerFactory.getLogger(RestClient.class);
	
	public String get(String restURL) {
		String returnResult = "";
		  try {

			URL url = new URL(restURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			
			log.debug("Output from Server .... \n");
			String output;
			while ((output = br.readLine()) != null) {
				log.debug(output);
				returnResult = output;
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		  }
		  log.debug("returnResult: " +returnResult);
		return returnResult;

		}
}
