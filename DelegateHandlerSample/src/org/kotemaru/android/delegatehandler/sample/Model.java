package org.kotemaru.android.delegatehandler.sample;

public class Model {
	private String mUrl;
	private String mText;
	private Throwable mError;

	// @formatter:off
	public String getUrl() {return mUrl;}
	public void setUrl(String url) {mUrl = url;}
	public String getText() {return mText;}
	public void setText(String text) {mText = text;}
	public Throwable getError() {return mError;}
	public void setError(Throwable error) {mError = error;}
	// @formatter:on
}
