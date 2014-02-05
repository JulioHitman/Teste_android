package com.example.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.models.RestfulResource;
import com.example.models.User;
import com.example.resourcelistener.RestfulResourceListener;

public class BaseView extends Activity implements RestfulResourceListener{

		public User user;
		
	@Override
	public void onCreate(Bundle savedInstancesState){
		super.onCreate(savedInstancesState);
		
		user = new User();
		user.setEmail(restorePreferences("email"));
		user.setToken(restorePreferences("token"));
	}
	public void storePreferences(String preferenceName, String preferenceValue) {
		SharedPreferences settings = getSharedPreferences("user", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(preferenceName, preferenceValue);
		editor.commit();
	}

	protected void storePreferences(String preferenceName,
			Boolean preferenceValue) {
		SharedPreferences settings = getSharedPreferences("user", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(preferenceName, preferenceValue);
		editor.commit();
	}

	public String restorePreferences(String preferenceName) {
		try {
			SharedPreferences settings = getSharedPreferences("user", 0);
			String resp = settings.getString(preferenceName, "");
			return resp;
		} catch (Exception e) {
			return null;
		}
	}

	public Boolean restorePreferencesBoolean(String preferenceName) {
		try {
			SharedPreferences settings = getSharedPreferences("user", 0);
			Boolean resp = settings.getBoolean(preferenceName, false);
			return resp;
		} catch (Exception e) {
			return false;
		}
	}

	protected void clearPreferences() {
		storePreferences("token", "");
		storePreferences("email", "");
		
	}
	@Override
	public void error(RestfulResource resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loaded(RestfulResource resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchLoaded(RestfulResource resource,
			ArrayList<RestfulResource> batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void created(RestfulResource resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchCreated(RestfulResource resource,
			ArrayList<RestfulResource> batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updated(RestfulResource resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchUpdated(RestfulResource resource,
			ArrayList<RestfulResource> batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyed(RestfulResource resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchDestroyed(RestfulResource resource,
			ArrayList<RestfulResource> batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searched(RestfulResource resource,
			ArrayList<RestfulResource> batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void upgraded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFileUploadProgressDialog(String percentage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void synced(RestfulResource resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authorized(RestfulResource resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authorizedUserService(RestfulResource resource) {
		// TODO Auto-generated method stub
		
	}

}
