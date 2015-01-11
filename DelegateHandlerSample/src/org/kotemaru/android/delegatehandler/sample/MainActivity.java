package org.kotemaru.android.delegatehandler.sample;

import org.kotemaru.android.delegatehandler.sample.MyApplication.Updater;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements Updater {
	private Controller mController;
	public Model mModel;
	public ProgressDialog mProgress;
	public Dialog mErrorDialog;

	private EditText mUrlEdit;
	private TextView mTextView;
	private Button mGoBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication app = (MyApplication) getApplication();
		mController = app.mController;
		mModel = app.mModel;

		mUrlEdit = (EditText) findViewById(R.id.text_url);
		mUrlEdit.addTextChangedListener(new TextWatcher() {
			// @formatter:off
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override public void afterTextChanged(Editable s) {
				mModel.setUrl(s.toString());
			}
			// @formatter:off
		});

		mGoBtn = (Button) findViewById(R.id.btn_go);
		mGoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View btn) {
				mModel.setIsProgress(true);
				mController.mHandler.doGetHtml(mModel.getUrl());
				update();
			}
		});
		mTextView = (TextView) findViewById(R.id.text_html);
	}
	@Override
	public void onResume() {
		super.onResume();
		update();
	}
	@Override
	public void onPause() {
		super.onPause();
		doProgressDialog(false);
		doErrorDialog(null);
	}
	@Override
	public void update() {
		doProgressDialog(mModel.isProgress());
		doErrorDialog(mModel.getError());
		mUrlEdit.setText(mModel.getUrl());
		mTextView.setText(mModel.getText());
	}
	private void doProgressDialog(boolean isProgress) {
		if (isProgress && mProgress == null) {
			mProgress = new ProgressDialog(this);
			mProgress.setMessage("Connecting...");
			mProgress.show();
		} else if (!isProgress && mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}
	private void doErrorDialog(Throwable error) {
		if (error != null && mErrorDialog == null) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this)
					.setTitle("Error!").setMessage(error.getMessage());
			dialog.setCancelable(false);
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mModel.setError(null);
					dialog.dismiss();
				}
			});
			mErrorDialog = dialog.show();
		} else if (error == null && mErrorDialog != null) {
			mErrorDialog.dismiss();
			mErrorDialog = null;
		}
	}
}
