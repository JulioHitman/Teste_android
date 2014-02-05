package com.example.resourcelistener;

import java.util.ArrayList;

import com.example.models.RestfulResource;

public interface RestfulResourceListener {
	
	public void error(RestfulResource resource);
	public void loaded(RestfulResource resource);
	public void batchLoaded(RestfulResource resource, ArrayList<RestfulResource> batch);
	public void created(RestfulResource resource);
	public void batchCreated(RestfulResource resource, ArrayList<RestfulResource> batch);
	public void updated(RestfulResource resource);
	public void batchUpdated(RestfulResource resource, ArrayList<RestfulResource> batch);
	public void destroyed(RestfulResource resource);
	public void batchDestroyed(RestfulResource resource, ArrayList<RestfulResource> batch);
	public void searched(RestfulResource resource, ArrayList<RestfulResource> batch);
	public void upgraded();
	public void updateFileUploadProgressDialog(String percentage);
	public void synced(RestfulResource resource);
	public void authorized(RestfulResource resource);
	public void authorizedUserService(RestfulResource resource);

}
