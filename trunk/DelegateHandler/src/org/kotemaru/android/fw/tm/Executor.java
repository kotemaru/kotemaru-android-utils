package org.kotemaru.android.fw.tm;


public interface Executor {
	public boolean post(Runnable runner, int delay);
}
