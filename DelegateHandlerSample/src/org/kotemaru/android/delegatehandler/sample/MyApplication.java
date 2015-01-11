package org.kotemaru.android.delegatehandler.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyApplication extends Application {
	public Model mModel;
	public Controller mController;
	public Activity mCurrentActivity;

	public interface Updater {
		public void update();
	}

	@Override
	public void onCreate() {
		mModel = new Model();
		mController = new Controller(this);
		registerActivityLifecycleCallbacks(mActivityMonitor);
	}
	public void updateCurrentActivity() {
		if (mCurrentActivity != null) {
			((Updater) mCurrentActivity).update();
		}
	}

	ActivityLifecycleCallbacks mActivityMonitor = new ActivityLifecycleCallbacks() {
		@Override
		public void onActivityResumed(Activity activity) {
			mCurrentActivity = activity;
		}
		@Override
		public void onActivityPaused(Activity activity) {
			if (mCurrentActivity == activity) mCurrentActivity = null;
		}

		// @formatter:off
		@Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
		@Override public void onActivityStarted(Activity activity) {}
		@Override public void onActivityStopped(Activity activity) {}
		@Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
		@Override public void onActivityDestroyed(Activity activity) {}
		// @formatter:on
	};
}
