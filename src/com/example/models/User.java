package com.example.models;

import java.util.HashMap;

import com.example.resourcelistener.RestfulResourceListener;
import com.example.resourcelistener.UserListener;
import com.google.gson.JsonObject;

public class User extends RestfulResource {
	private String email;
	private String password;
	private String passwordConfirmation;
	
	private String token;
	
	public static final String USER_SIGNIN_URI = "/api/v1/tokens.json";	
	
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
		
	public boolean signIn(){
		boolean success = false;
		
		JsonObject signInUserData = new JsonObject();
		signInUserData.addProperty("email", this.email);
		signInUserData.addProperty("password", this.password);
		
		success = post(USER_SIGNIN_URI, signInUserData.toString());

		if (success) {
			this.token = json.get("token").toString().replace("\"", "");
			
		}

		if (null != resourceListeners) {
			for (RestfulResourceListener resourceListener : resourceListeners) {
				if (success) {
					((UserListener) resourceListener).signedIn(this);
				} else {
					resourceListener.error(this);
				}
			}
		}
		return success;
	}
	@Override
	public HashMap<String, String> getAttributesMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String primaryKeyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String remotePrimaryKeyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String loadUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String loadJsonKeyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllUri(String term) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createBatchUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateBatchUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String destroyUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String destroyBatchUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String listJsonKeyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestfulResource getNewInstance() {
		// TODO Auto-generated method stub
		return null;
	}
}
