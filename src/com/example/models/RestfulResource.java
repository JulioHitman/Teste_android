package com.healclick.healclick.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.healclick.healclick.resourcelisteners.RestfulResourceListener;
import com.healclick.healclick.utils.CustomMultiPartEntity;
import com.healclick.healclick.utils.CustomMultiPartEntity.ProgressListener;
import com.healclick.healclick.utils.CustomSslSocketFactory;

public abstract class RestfulResource {

	public static final Boolean IS_PRODUCTION = false;
	public static final Boolean DEBUG = true;
	
	protected String localError;
	protected String remoteError;
	protected final String REMOTE_IP = "192.168.1.40:3000";
	protected final String REMOTE_FILE_IP = "192.168.1.40:3000";
	protected final String REMOTE_URL = "http" + "://" + REMOTE_IP +"/";
	protected final String REMOTE_FILE_URL = "http" + "://" + REMOTE_FILE_IP + "/";
	protected DefaultHttpClient httpClient;
	protected JsonObject json;
	protected ArrayList<RestfulResourceListener> resourceListeners;
	protected User user;
	protected String userId;
	protected HashMap<String,String> attributesMap;
	protected String startDateFilter;
	protected String endDateFilter;
	protected ArrayList<RestfulResource> batch;
	protected boolean isBatch = false;
	protected String fileUploadPath;
	protected boolean header = false;
	protected String headerName;
	protected long fileUploadTotalSize; 
	
	/* Abstract Methods */
	public abstract HashMap<String,String> getAttributesMap();
	public abstract String primaryKeyName();
	public abstract String remotePrimaryKeyName();
	public abstract String loadUri();
	public abstract String loadJsonKeyName();
	public abstract String getAllUri();
	public abstract String getAllUri(String term);
	public abstract String createUri();
	public abstract String createBatchUri();
	public abstract String updateUri();
	public abstract String updateBatchUri();
	public abstract String destroyUri();
	public abstract String destroyBatchUri();
	public abstract String listJsonKeyName();
	public abstract RestfulResource getNewInstance();

	public RestfulResource(){
		this.header = false;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.userId = user.getUserId();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public ArrayList<RestfulResource> getBatch() {
		return batch;
	}
	
	public void setBatch(ArrayList<RestfulResource> batch) {
		this.batch = batch;
	}
	
	public boolean isBatch() {
		return isBatch;
	}
	
	public void setBatch(boolean isBatch) {
		this.isBatch = isBatch;
	}
	
	public String getLocalError() {
		return localError;
	}
	
	public void setLocalError(String localError) {
		this.localError = localError;
	}
	
	public String getRemoteError() {
		return remoteError;
	}
	
	public void setRemoteError(String remoteError) {
		this.remoteError = remoteError;
	}
	
	public void addResourceListener(RestfulResourceListener resourceListener) {
		if(null == resourceListeners) {
			resourceListeners = new ArrayList<RestfulResourceListener>();
		}
		resourceListeners.add(resourceListener);
	}
	
	public void clearResourceListeners() {
		this.resourceListeners = new ArrayList<RestfulResourceListener>();
	}
	
	public String getStartDateFilter() {
		return startDateFilter;
	}
	
	public void setStartDateFilter(String startDateFilter) {
		this.startDateFilter = startDateFilter;
	}
	
	public String getEndDateFilter() {
		return endDateFilter;
	}
	
	public void setEndDateFilter(String endDateFilter) {
		this.endDateFilter = endDateFilter;
	}
	
	public JsonObject getJson() {
		return json;
	}
	public void setJson(JsonObject json) {
		this.json = json;
	}

	public boolean isHeader() {
		return header;
	}
	public void setHeader(boolean header) {
		this.header = header;
	}
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	protected DefaultHttpClient getThreadSafeClient() {
		 
		CustomSslSocketFactory sf = null;
		KeyStore trustStore = null;
        try {
        	trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
   		 	sf = new CustomSslSocketFactory(trustStore);
			trustStore.load(null, null);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        
		DefaultHttpClient client = new DefaultHttpClient(ccm, params);
		return client;
	}
	
	public boolean get(String uri) {
		boolean success = false;
		String returnString = "";
		HttpResponse response;

		try {
			httpClient = getThreadSafeClient();
			httpClient.getConnectionManager().closeExpiredConnections();
			HttpGet request = new HttpGet(REMOTE_URL + uri);
			request.addHeader("accept", "application/json");
			
			response = httpClient.execute(request);
			BufferedReader buffer = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			
			StringBuffer contentBuffer = new StringBuffer();
			
			while ((returnString = buffer.readLine()) != null) {
				contentBuffer.append(returnString);
			}
			
			if(DEBUG){
				System.out.println("HealClick GET");
				System.out.println(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTimeInMillis()));
				System.out.println("REQUEST URL: " + request.getURI());
				System.out.println("RESPONSE: " + contentBuffer.toString());
			}
			if(!"".equals(contentBuffer.toString()) && !"Records not found.".equals(contentBuffer.toString()) && !"No records found.".equals(contentBuffer.toString()) && !"No records found".equals(contentBuffer.toString())) {
				JsonElement jsonElement = null;
				try {
					jsonElement = new JsonParser().parse(contentBuffer.toString());
					json = jsonElement.getAsJsonObject();
				} catch (Exception e) {
					this.remoteError = "Invalid username or password";
					success = false;
					e.printStackTrace();
				}
			} else if (("Records not found.".equals(contentBuffer.toString())) || ("No records found.".equals(contentBuffer.toString())) || ("No records found".equals(contentBuffer.toString()))) {
				success = false;
				this.remoteError = contentBuffer.toString();
				httpClient.getConnectionManager().shutdown();
				response.getEntity().consumeContent();
				return success;
			}
			
			if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 201) {
				if(null != json) {
					this.remoteError = json.get("message").toString().replace("\"", "");	
				}
				success = false;
			} else {
				success = true;
			}
			response.getEntity().consumeContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		httpClient.getConnectionManager().shutdown();
		
		return success;
	}	
	
	public boolean post(String uri, String jsonString) {
		boolean success = false;
		String returnString = "";
		StringEntity input;

		try {

			input = new StringEntity(jsonString, HTTP.UTF_8);
			httpClient = getThreadSafeClient();
			HttpPost request;
			if (this instanceof FileUpload) {
				request = new HttpPost(REMOTE_FILE_URL + uri);
			}else{
				request = new HttpPost(REMOTE_URL + uri);
			}
			
			request.addHeader("accept", "application/json");
			input.setContentType("application/json");
			request.setEntity(input);
			
			if (this instanceof FileUpload) {
				String file = this.fileUploadPath;
				FileBody bin = new FileBody(new File(file));
				
				CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(new ProgressListener()
				{
					@Override
					public void transferred(long num)
					{
						for (RestfulResourceListener resourceListener : resourceListeners) {
							String percentage = (int) ((num / (float) fileUploadTotalSize) * 100)+"";
							resourceListener.updateFileUploadProgressDialog(percentage);
						}
					}
				});
				
				// We use FileBody to transfer an image
				multipartContent.addPart("file", bin);
				fileUploadTotalSize = multipartContent.getContentLength();
				
				request.setEntity(multipartContent);
			}
			
			HttpResponse response = httpClient.execute(request);
			BufferedReader buffer = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			
			StringBuffer contentBuffer = new StringBuffer();        
			
			while ((returnString = buffer.readLine()) != null) {
				contentBuffer.append(returnString);
			}
			
			if(DEBUG){
				System.out.println("HealClick POST");
				System.out.println(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTimeInMillis()));
				System.out.println("REQUEST URL: " + request.getURI());
				System.out.println("REQUEST JSON: " + jsonString);
				System.out.println("RESPONSE: " + contentBuffer.toString());
			}
			try {
				JsonElement jsonElement = new JsonParser().parse(contentBuffer.toString());
				if(jsonElement != null && !jsonElement.isJsonNull()) {
					json = jsonElement.getAsJsonObject();
				}
			} catch (IllegalStateException e) {
				if(isBatch) {
					String[] elementIds = contentBuffer.toString().replace("]", "").replace("[", "").split(",");
					loadIdsFromElementIds(elementIds);
				} else {
					e.printStackTrace();
				}
			} catch (Exception e) {
				System.out.println(contentBuffer.toString());
			}
			
			if (response.getStatusLine().getStatusCode() != 201 && response.getStatusLine().getStatusCode() != 200) {
				if(null != json) {
					this.remoteError = json.get("message").toString().replace("\"", "");	
				}
				success = false;
			} else {
				success = true;
			}

			httpClient.getConnectionManager().shutdown();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return success;
	}
	
	public boolean put(String uri, String jsonString) {
		boolean success = false;
		String returnString = "";
		StringEntity input;
		
		try {

			input = new StringEntity(jsonString);
			httpClient = getThreadSafeClient();
			HttpPut request = new HttpPut(REMOTE_URL + uri);
			
			request.addHeader("accept", "application/json");
			input.setContentType("application/json");
			request.setEntity(input);
			
			HttpResponse response = httpClient.execute(request);
			BufferedReader buffer = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			
			StringBuffer contentBuffer = new StringBuffer();
			while ((returnString = buffer.readLine()) != null) {
				contentBuffer.append(returnString);
			}
			
			JsonElement jsonElement = new JsonParser().parse(contentBuffer.toString());
			json = jsonElement.getAsJsonObject();

			if (response.getStatusLine().getStatusCode() != 201 && response.getStatusLine().getStatusCode() != 200) {
				if(null != json) {
					this.remoteError = json.get("message").toString().replace("\"", "");	
				}
				success = false;
			} else {
				success = true;
			}

			httpClient.getConnectionManager().shutdown();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}
	
	public boolean delete(String uri) {
		boolean success = false;
		return success;
	}
	
	public String getJsonString(RestfulResource resource) {
		String jsonString = "{";
		
		for (Entry<String, String> attribute : getAttributesMap().entrySet()) {
			String remoteAttributeName = attribute.getKey();
			String localAttributeName = attribute.getValue();
			String methodName = "get" + localAttributeName.substring(0, 1).toUpperCase() + localAttributeName.substring(1);
			
			Method method;
			String localAttributeValue = "";
			
			try {
				method = resource.getClass().getMethod(methodName, new Class[] {});
				localAttributeValue = (String) method.invoke(resource);	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(remoteAttributeName.contains("reminder") || remoteAttributeName.contains("extra")){
				jsonString += "\"" + remoteAttributeName + "\": " + localAttributeValue + ", ";
			}else{
				jsonString += "\"" + remoteAttributeName + "\": \"" + localAttributeValue + "\", ";
			}
			
		}
		
		jsonString += "}";
		jsonString = jsonString.replace(", }", "}").replace("null", "");
		
		return jsonString;
	}
	
	public String getJsonString() {
		return getJsonString(this);
	}
	
	public String getBatchJsonString(String userId) {
		String jsonString = "{\"userID\": \"" + userId + "\", " +
								"\"" + this.listJsonKeyName() + "\": [";
		
		for(int i = 0; i < batch.size(); i++) {
			jsonString += getJsonString(batch.get(i));
			jsonString += ", ";
		}
		
		jsonString += "]}";
		jsonString = jsonString.replace(", ]", "]").replace("null", "");
		
		return jsonString;
	}
	
	public String getBatchJsonIds(String userId) {
		String jsonIds = "[";
		for(int i = 0; i < batch.size(); i++) {
			String methodName = "get" + batch.get(i).primaryKeyName().substring(0, 1).toUpperCase() + batch.get(i).primaryKeyName().substring(1);
			
			Method method;
			
			try {
				method = batch.get(i).getClass().getMethod(methodName, new Class[] {});
				jsonIds += (String) method.invoke(batch.get(i));
				jsonIds += ",";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		jsonIds += "]";
		jsonIds = jsonIds.replace(",]", "]").replace("null", "");		

		String batchJsonIds = "{\"userID\":\"" + userId + "\",\"medicationIDList\":" + jsonIds + "}";
		return batchJsonIds;
	}
	
	public boolean isNewRecord() {
		boolean isNewRecord = true;
		String methodName = "get" + primaryKeyName().substring(0, 1).toUpperCase() + primaryKeyName().substring(1);
		Method method;
		
		try {
			method = this.getClass().getMethod(methodName, new Class[] {});
			String primaryKeyValue = (String) method.invoke(this);
			if(null != primaryKeyValue && !"".equals(primaryKeyValue)) {
				isNewRecord = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isNewRecord;
	}
	
	public void loadFromComingData() {
		if(json == null) {
			return;
		}
		loadFromJson(json);
	}
	
	public void loadFromJson(JsonObject jsonObject) {
		for (Entry<String, String> attribute : getAttributesMap().entrySet()) {
			String remoteAttributeName = attribute.getKey();
			String localAttributeName = attribute.getValue();
			String methodName = "set" + localAttributeName.substring(0, 1).toUpperCase() + localAttributeName.substring(1);
			
			Method method;
			
			try {
				method = this.getClass().getMethod(methodName, new Class[] {String.class});
				String attributeValue = "";

				if(null != jsonObject.get(remoteAttributeName)) {
					attributeValue = jsonObject.get(remoteAttributeName).toString().replace("\"", "");
					method.invoke(this, attributeValue.contains("null") ? "" : attributeValue);	
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

	public void loadIdsFromElementIds(String[] elementIds) {
		for(int i = 0; i < elementIds.length; i++) {
			String methodName = "set" + batch.get(i).primaryKeyName().substring(0, 1).toUpperCase() + batch.get(i).primaryKeyName().substring(1);
			Method method;
			
			try {
				method = batch.get(i).getClass().getMethod(methodName, new Class[] {String.class});
				method.invoke(batch.get(i), elementIds[i]);	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unused")
	public ArrayList<RestfulResource> loadFromJsonList() {
		if(json == null) {
			return new ArrayList<RestfulResource>();
		}

		ArrayList<RestfulResource> resources = new ArrayList<RestfulResource>();
		
		try{
			JsonArray elementsArray = json.getAsJsonArray(listJsonKeyName());
		}catch(Exception e){
			return new ArrayList<RestfulResource>();
		}
		
		JsonArray elementsArray = json.getAsJsonArray(listJsonKeyName());
		
		if(elementsArray == null) {
			return new ArrayList<RestfulResource>();
		}

		for (int i = 0; i < elementsArray.size(); i++) {
			RestfulResource resource = this.getNewInstance();
			JsonObject jsonObject = elementsArray.get(i).getAsJsonObject();
			resource.loadFromJson(jsonObject);
			resources.add(resource);
		}
		
		return resources;
	}
	
	public ArrayList<RestfulResource> all() {
		this.batch = new ArrayList<RestfulResource>();
		
		boolean success = get(getAllUri());
		if (success) {
			this.batch = loadFromJsonList();
		}
		notifyBatchLoaded(success);
		return batch;
	}
	
	public ArrayList<RestfulResource> search(String term) {
		boolean success = get(getAllUri(term.replace(" ", "%20")));
		
		if (success) {
			this.batch = loadFromJsonList();
		}

		notifyBatchLoaded(success);
		return batch;
	}
	
	public boolean load() { 
		boolean success = get(loadUri());
		
		if(success) {
			loadFromJson(json.getAsJsonObject(loadJsonKeyName()));
		}
		notifyLoaded(success);
		return success;
	}
	
	public boolean save() {
		boolean success = false;
		if(isNewRecord()) {
			success = create();
		} else {
			success = update();
		}
		
		/* Populating model with coming attributes */
		if(success) {
			loadFromComingData();		
		}
		
		return success;
	}
	
	public boolean create() {
		boolean success = false;
		success = post(createUri(), getJsonString());
		notifyCreated(success);
		return success;		
	}
	
	public boolean createBatch() {
		boolean success = false;
		isBatch = true;
		success = post(createBatchUri(), getBatchJsonString(userId));
		notifyBatchCreated(success);
		return success;
	}
	
	public boolean update() {
		boolean success = false;
		success = post(updateUri(), getJsonString());
		notifyUpdated(success);
		return success;
	}
	
	public boolean updateBatch() {
		boolean success = false;
		isBatch = true;
		success = post(updateBatchUri(), getBatchJsonString(userId));
		notifyBatchUpdated(success);
		return success;
	}
	
	public boolean destroy() {
		boolean success = false;
		success = post(destroyUri(), getJsonString());
		
		/* Primary key value should now be null */
		if(success) {
			String methodName = "set" + primaryKeyName().substring(0, 1).toUpperCase() + primaryKeyName().substring(1);
			Method method;
			
			try {
				method = this.getClass().getMethod(methodName, new Class[] {String.class});
				method.invoke(this, (Object) null);	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		notifyDestroyed(success);
		return success;		
	}
	
	public boolean destroyBatch() {
		boolean success = false;
		success = post(destroyBatchUri(), getBatchJsonIds(this.userId));

		if(success) {
			batch = new ArrayList<RestfulResource>();
		}
		
		notifyBatchDestroyed(success);
		return success;		
	}
	
	public void notifyLoaded(boolean success) {
		if(null != resourceListeners) {
			for (RestfulResourceListener resourceListener : resourceListeners) {
				if (success) {
					resourceListener.loaded(this);
				} else {
					resourceListener.error(this);
				}
			}		
		}		
	}
	
	public void notifyBatchLoaded(boolean success) {
		if(null != resourceListeners) {
			for (RestfulResourceListener resourceListener : resourceListeners) {
				if (success) {
					resourceListener.batchLoaded(this, batch);
				} else {
					resourceListener.error(this);
				}
			}
		}
	}
	
	public void notifyCreated(boolean success) {
		if(null != resourceListeners) {
			for (RestfulResourceListener resourceListener : resourceListeners) {
				if (success) {
					resourceListener.created(this);
				} else {
					resourceListener.error(this);
				}
			}
		}
	}
	
	public void notifyBatchCreated(boolean success) {
		if(null != resourceListeners) {
			for (RestfulResourceListener resourceListener : resourceListeners) {
				if (success) {
					resourceListener.batchCreated(this, batch);
				} else {
					resourceListener.error(this);
				}
			}
		}
	}	
	
	public void notifyUpdated(boolean success) {
		if(null != resourceListeners) {
			for (RestfulResourceListener resourceListener : resourceListeners) {
				if (success) {
					resourceListener.updated(this);
				} else {
					resourceListener.error(this);
				}
			}		
		}		
	}
	
	public void notifyBatchUpdated(boolean success) {
		if(null != resourceListeners) {
			for (RestfulResourceListener resourceListener : resourceListeners) {
				if (success) {
					resourceListener.batchUpdated(this, batch);
				} else {
					resourceListener.error(this);
				}
			}
		}
	}	
	
	public void notifyDestroyed(boolean success) {
		if(null != resourceListeners) {
			for (RestfulResourceListener resourceListener : resourceListeners) {
				if (success) {
					resourceListener.destroyed(this);
				} else {
					resourceListener.error(this);
				}
			}		
		}		
	}
	
	public void notifyBatchDestroyed(boolean success) {
		if(null != resourceListeners) {
			for (RestfulResourceListener resourceListener : resourceListeners) {
				if (success) {
					resourceListener.batchDestroyed(this, batch);
				} else {
					resourceListener.error(this);
				}
			}
		}
	}	
	
	public JsonObject getJsonResponse(){
		return (JsonObject) (this.json != null ? json : null);
	}
	
	protected ArrayList<RestfulResource> getResourcesByIndex(String indexName) {
		if (json == null) {
			return new ArrayList<RestfulResource>();
		}

		ArrayList<RestfulResource> resources = new ArrayList<RestfulResource>();

		JsonArray elementsArray = json.getAsJsonArray(indexName);

		if (elementsArray == null) {
			return new ArrayList<RestfulResource>();
		}

		for (int i = 0; i < elementsArray.size(); i++) {
			RestfulResource resource = this.getNewInstance();
			JsonObject jsonObject = elementsArray.get(i).getAsJsonObject();
			resource.loadFromJson(jsonObject);
			resources.add(resource);
		}

		return resources;
	}

}