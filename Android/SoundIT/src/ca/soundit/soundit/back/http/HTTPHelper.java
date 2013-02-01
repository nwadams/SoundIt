package ca.soundit.soundit.back.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class HTTPHelper {

	public static String HTTPGetRequest(String url, Hashtable<String,String> params) {
		
		url = addParametersToURL(url, params);
		String result = null;
		
		HttpURLConnection urlConnection = null;
		
		try {
			urlConnection = (HttpURLConnection) new URL(url).openConnection();
			urlConnection.setRequestProperty("content-type", "application/JSON");
			
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{	
				InputStream in = urlConnection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
	
				String line;
				StringBuilder sb = new StringBuilder();
	
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				
				result = sb.toString();
				
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			urlConnection.disconnect();
		}
		
		return result;
	}
	
	private static String addParametersToURL(String url, Hashtable<String,String> params) {
		if(!url.endsWith("?"))
	        url += "?";

		List<NameValuePair> URLParams = new LinkedList<NameValuePair>();
		
		Iterator<String> iterator = params.keySet().iterator();
		
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = params.get(key);
			
			URLParams.add(new BasicNameValuePair(key, value));
		}
		
		String paramString = URLEncodedUtils.format(URLParams, "utf-8");

	    url += paramString;
		
		return url;
	}
}
