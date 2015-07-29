package ca.on.conestogac;

import java.io.*;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WebClient {

	private DefaultHttpClient httpClient;

	public WebClient()
	{
		this.httpClient = new DefaultHttpClient();
	}

	public void dispose()
	{
		this.httpClient.getConnectionManager().shutdown();
	}

	public String downloadString(String sUrl) throws ClientProtocolException,
			IOException {

		// put the URL in a HttpGet
		HttpGet getRequest = new HttpGet(sUrl);

		// Create a response handler that will create a String on our behalf
		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		// Execute the request with the responseHandler
		return this.httpClient.execute(getRequest,
				responseHandler);
	}

	private Object handleJsonResponse(HttpResponse response)  throws IOException, ParseException
	{
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode() + " Message : " +
					response.getStatusLine().getReasonPhrase());
		}
		return new JSONParser().parse(br);

	}

	public Object downloadJson(String sUrl, List<NameValuePair> nameValuePairs) throws IOException, ParseException
	{
	    HttpPost postRequest = new HttpPost(sUrl);
		postRequest.addHeader("accept", "application/json");
		postRequest.addHeader("User-Agent", "JSONURLParser/1.0");
		postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = this.httpClient.execute(postRequest);
		return this.handleJsonResponse(response);
	}

	public Object downloadJson(String sUrl) throws IOException, ParseException
	{
		HttpGet getRequest = new HttpGet(
				sUrl);
		getRequest.addHeader("accept", "application/json");
		getRequest.addHeader("User-Agent", "JSONURLParser/1.0");

		HttpResponse response = this.httpClient.execute(getRequest);
		return this.handleJsonResponse(response);
	}


}
