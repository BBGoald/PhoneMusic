package phone.wobo.music.util;

import java.io.IOException;
import java.io.InputStream;
 
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse; 
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;  
import org.apache.http.protocol.HttpContext;  
import org.apache.http.util.EntityUtils;

import android.util.Log;


public class WoboHttpClient implements HttpClient {
	
	private static final int defaultTimeout = 30; 
	private static final int defaultSoTimeout = 10; 
	private int mTimeout;
	private int mSoTimeout;
	private String mUrl; 
	private HttpClient delegate;  
	private long mContentLength;
	
	private String TAG = "WoboHttpClient";
	
	public WoboHttpClient() {
		this("");
	} 
	public WoboHttpClient(String url) {
		this(url,defaultTimeout);
	} 
	public WoboHttpClient(String url,int timeout) {
		this(url,timeout,defaultSoTimeout);
	}
	public WoboHttpClient(String url,int timeout,int soTimeout) {
		System.out.println("liangbang WoboHttpClient ");
		this.mUrl = url;
		this.mTimeout = timeout;
		this.mSoTimeout = soTimeout;
		this.delegate = new DefaultHttpClient() {
			protected HttpParams createHttpParams() {
				HttpParams params = super.createHttpParams();
				HttpConnectionParams.setConnectionTimeout(params, mTimeout * 1000); 
				HttpConnectionParams.setSoTimeout(params, mSoTimeout * 1000);
				return params;
			}
		};
	}
	public HttpParams getParams() {
		return delegate.getParams();
	} 
	public ClientConnectionManager getConnectionManager() {
		return delegate.getConnectionManager();
	}
	public String downloadString() throws IOException{
		return downloadString(this.mUrl,"UTF-8");
	}
	public String downloadString(String url) throws IOException{
		return downloadString(url,"UTF-8");
	}

	/**
	 * download string from server
	 * @param url
	 * @param charset String encoded with "UTF-8"
	 * @return
	 * @throws IOException
	 * @author root liangbang
	 */
	public String downloadString(String url, String charset) throws IOException {
		HttpUriRequest request = new HttpGet(url);
		HttpResponse response = execute(request);
		Log.d(TAG, "liangbang response= " + response);
		if (null != response && response.getStatusLine().getStatusCode() == 200) {
			try {
				return EntityUtils.toString(response.getEntity(), charset);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	
	public byte[] downloadByte(String url) throws IOException {
		HttpUriRequest request = new HttpGet(url);
		HttpResponse response = execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			return EntityUtils.toByteArray(response.getEntity());
		}
		return null;
	}
	
    public long getContentLength() {
    	return mContentLength;
    }
    public InputStream getContent() throws Exception { 
		return getContent(this.mUrl);
	}
	public InputStream getContent(String url) throws Exception {
		HttpUriRequest request = new HttpGet(url);
	//	request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		
		HttpResponse response = execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			mContentLength = response.getEntity().getContentLength();
		return response.getEntity().getContent();
		}
		return null;
	}
	
	public String getContentString() throws Exception {
		HttpUriRequest request = new HttpGet(this.mUrl);		
		HttpResponse response = execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			//mContentLength = response.getEntity().getContentLength();
			 return EntityUtils.toString( response.getEntity(), "UTF-8" );  
		}
		return null;
	}
	public HttpResponse execute() throws IOException {
		HttpUriRequest request = new HttpGet(this.mUrl);
		return execute(request);
	}
	public HttpResponse post() throws IOException { 
		HttpUriRequest request = new HttpPost(this.mUrl);
		return execute(request);
	}
	// 
	public HttpResponse execute(HttpUriRequest request) throws IOException {
		return delegate.execute(request);
	}
	public void close() {
		 getConnectionManager().shutdown();
	}
	public HttpResponse execute(HttpUriRequest request, HttpContext context)
			throws IOException {  
		return delegate.execute(request, context);
	}

	public HttpResponse execute(HttpHost target, HttpRequest request)
			throws IOException {
		return delegate.execute(target, request);
	}

	public HttpResponse execute(HttpHost target, HttpRequest request,
			HttpContext context) throws IOException {
		return delegate.execute(target, request, context);
	}
	public <T> T execute(HttpUriRequest request,
			ResponseHandler<? extends T> responseHandler) throws IOException,
			ClientProtocolException { 
		return delegate.execute(request, responseHandler);
	}

	public <T> T execute(HttpUriRequest request,
			ResponseHandler<? extends T> responseHandler, HttpContext context)
			throws IOException, ClientProtocolException {
		return delegate.execute(request, responseHandler, context);
	}

	public <T> T execute(HttpHost target, HttpRequest request,
			ResponseHandler<? extends T> responseHandler) throws IOException,
			ClientProtocolException {
		return delegate.execute(target, request, responseHandler);
	}

	public <T> T execute(HttpHost target, HttpRequest request,
			ResponseHandler<? extends T> responseHandler, HttpContext context)
			throws IOException, ClientProtocolException {
		return delegate.execute(target, request, responseHandler, context);
	}
}