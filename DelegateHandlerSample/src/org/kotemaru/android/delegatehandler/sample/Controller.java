package org.kotemaru.android.delegatehandler.sample;

import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.kotemaru.android.delegatehandler.annotation.DelegateHandler;
import org.kotemaru.android.delegatehandler.annotation.Handling;
import org.kotemaru.android.delegatehandler.rt.DefaultThreadManager;
import org.kotemaru.android.delegatehandler.rt.OnDelegateHandlerErrorListener;
import org.kotemaru.android.delegatehandler.rt.ThreadManager;

import android.util.Log;

@DelegateHandler
public class Controller implements OnDelegateHandlerErrorListener {
	private static final String TAG = Controller.class.getSimpleName();
	public static final boolean IS_DEBUG = BuildConfig.DEBUG;

	public ControllerHandler mHandler = new ControllerHandler(this, DefaultThreadManager.getInstance());
	private MyApplication mApplication;

	public Controller(MyApplication app) {
		mApplication = app;
		mApplication.mModel.setUrl("http://www.google.com/");
	}
	
	@Handling(thread = ThreadManager.NETWORK, retry = 3)
	public void doGetHtml(String url) throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		try {
			String html = httpClient.execute(request, new BasicResponseHandler());
			mHandler.doGetHtmlFinish(html);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	@Handling(thread = ThreadManager.UI)
	public void doGetHtmlFinish(String html) {
		mApplication.mModel.setText(html);
		mApplication.updateCurrentActivity();
	}
	
	@Override
	@Handling(thread = ThreadManager.UI)
	public void onDelegateHandlerError(Throwable t, String methodName, Object... arguments) {
		Log.e(TAG, "onDelegateHandlerError:" + t, t);
		mApplication.mModel.setError(t);
		mApplication.updateCurrentActivity();
	}
}
