package io.github.rhildred;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.http.*;

public class Oauth2 {

	private WebClient conn = null;
	private String sKey = null, sSecretToken = null, sRedirect = null;
	private HttpSession Session = null;

	public Oauth2(String sClientId, String sSecret, String sReturnUrl,
			HttpSession sess) {
		this.conn = new WebClient();
		this.sKey = sClientId;
		this.sSecretToken = sSecret;

		this.sRedirect = sReturnUrl;
		if (!this.sRedirect.contains("localhost")) {
			String sPattern = ":\\d\\d*";
			this.sRedirect = this.sRedirect.replaceAll(sPattern, "");
		}
		this.Session = sess;
	}

	public void redirect(HttpServletResponse res) throws IOException {
		String sAuthUrl = "https://accounts.google.com/o/oauth2/auth?redirect_uri=%s&client_id=%s&scope=https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email&response_type=code&max_auth_age=0";
		String sRedirectToGoogle = String.format(sAuthUrl, this.sRedirect,
				this.sKey);
		res.sendRedirect(sRedirectToGoogle);

	}

	public void handleCode(String sCode) throws IOException, ParseException
    {
        if (this.Session.getAttribute("sGoogleId") == null)
        {
            //step 5
            // then google has redirected to us so build up query for 2nd phase of authentication
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
            nameValuePairs.add(new BasicNameValuePair("client_id", this.sKey));
            nameValuePairs.add(new BasicNameValuePair("client_secret", this.sSecretToken));
            nameValuePairs.add(new BasicNameValuePair("code", sCode));
            nameValuePairs.add(new BasicNameValuePair("redirect_uri", this.sRedirect));

            JSONObject oResult = (JSONObject) this.conn.downloadJson("https://accounts.google.com/o/oauth2/token", nameValuePairs);
            String sAccessToken = (String)oResult.get("access_token");

            // step 7
            // now we can get the user info
            String sUserInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=%s";
            JSONObject oInfo = (JSONObject) conn.downloadJson(String.format(sUserInfoUrl, sAccessToken));
            this.Session.setAttribute("sName", (String)oInfo.get("name"));
            this.Session.setAttribute("sGoogleId", (String)oInfo.get("id"));
            this.Session.setAttribute("sEmail", (String)oInfo.get("email"));

        }

    }

	public String getName()
	{
		return (String)this.Session.getAttribute("sName");
	}

	public String getGoogleId()
	{
		return (String)this.Session.getAttribute("sGoogleId");
	}

	public String getEmail()
	{
		return (String)this.Session.getAttribute("sEmail");
	}

	public void close() {
		this.conn.dispose();
	}

}
