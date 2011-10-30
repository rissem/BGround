package com.jukejuice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

public class Util {

	private static final Logger log = Logger.getLogger(Util.class);
	
	public static String exec(String command) throws IOException
	{
		Process process = null;
		process = Runtime.getRuntime().exec(command);
		return convertStreamToString(process.getInputStream());
	}
	
	/**
	 * taken from http://www.kodejava.org/examples/266.html
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String convertStreamToString(InputStream is)
			throws IOException {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}
	
	public static String fetchUrl (String urlString) throws IOException
	{
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		String content = Util.convertStreamToString(conn.getInputStream());
		return content;
	}
	
	public static String resource (String filename)
	{
		return ClassLoader.getSystemClassLoader().getResource(filename).getFile();
	}
	
	public static Properties getEnvProperties() {
		Properties env = new Properties();
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("env.properties");
		if (stream != null) {
			try {
				env.load(stream);
			} catch (IOException e) {
				log.error("", e);
			}
		}		
		return env;
	}
	
	public static String postUrl(String urlString, Map<String,String> data) throws ClientProtocolException, IOException {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (String key: data.keySet())
			formparams.add(new BasicNameValuePair(key, data.get(key)));
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			HttpPost httpPost = new HttpPost(urlString);
			httpPost.setEntity(entity);
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.execute(httpPost);
		} catch (UnsupportedEncodingException e) {
			log.error("", e);
		}
		return null;
	}
}
