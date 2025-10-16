

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

public class Utils
{
	public static final String FMT_MS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String FMT_MST = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String FMT = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String SHORT_FMT = "yyyy-MM-dd";
	public static final String FMT_UID = "yyyyMMddHHmmssSSS";
	
	public static final SimpleDateFormat DATE_FMT_MS = new SimpleDateFormat(FMT_MS);
	public static final SimpleDateFormat DATE_FMT_MST = new SimpleDateFormat(FMT_MST);
	public static final SimpleDateFormat DATE_FMT = new SimpleDateFormat(FMT);
	public static final SimpleDateFormat SHORT_DATE_FMT = new SimpleDateFormat(SHORT_FMT);
	public static final SimpleDateFormat DATE_FMT_UID = new SimpleDateFormat(FMT_UID);
	
	
	public static StringBuilder readURL(URLConnection con) throws Exception
	{
		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		StringBuilder buff = new StringBuilder();
		while((line = br.readLine()) != null)
		{
			buff.append(line);
		}
		is.close();
		return buff;
	}
	
	public static String sendPostData(String url, Param params) throws Exception
	{
		return sendData(url, "POST", params);
	}
	public static String sendGetData(String url, Param... params) throws Exception
	{
		url = url + "?";
		String sep = "";
		for (Param param : params) {
			url = url + sep + param.getKey() + "=" + param.getValue();
			sep = "&";
		}
		System.out.println(url);
		return sendData(url, "GET");
	}
	public static String sendData(String url, String tipo, Param... params) throws Exception
	{
		return sendData((HttpURLConnection)new URL(url).openConnection(), tipo, params);
	}
	public static String sendData(HttpURLConnection con, String tipo, Param... params) throws Exception
	{
		//System.out.println(url);
		
		con.setRequestMethod(tipo);
		if (tipo.equals("POST"))
		{
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
		}
		if (params != null && params.length > 0)
		{
			try(OutputStream os = con.getOutputStream()) {
				JSONObject obj = new JSONObject();
				for (Param param : params) {
					obj.put(param.getKey(), param.getValue());
				}
			    byte[] output = obj.toString().getBytes("utf-8");
			    os.write(output, 0, output.length);			
				
				//obj.write(new OutputStreamWriter(os, "UTF-8"));			
			}
		}
		return readURL(con).toString();
	}	
	
}
