package com.omg.autographer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.omg.autographer.util.ALog;

public class AutographerSupport extends Activity implements OnClickListener {
	String TAG = "AutographerSupport";

	WebView webView;

	View backButton, forwardButton;
	View loading;

	// -------- Lifecycle Methods --------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.autographer_support);

		(backButton = findViewById(R.id.bottomWebBar).findViewById(R.id.backButton)).setOnClickListener(this);
		(forwardButton = findViewById(R.id.bottomWebBar).findViewById(R.id.forwardButton)).setOnClickListener(this);
		loading = findViewById(R.id.bottomWebBar).findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		
		webView = (WebView) findViewById(R.id.webView);

		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);

		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				loading.setVisibility(View.INVISIBLE);
				webView.setVisibility(View.VISIBLE);
				updateButtonStatus();
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon){
				loading.setVisibility(View.VISIBLE);
			}
		});
		webView.loadUrl("http://support.autographer.com");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.Autographer_autographer_support);
		
		updateButtonStatus();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			setOutTransition();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void finish() {
		ALog.d(TAG, "finish");
		super.finish();
		setOutTransition();
	}

	private void setOutTransition() {
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	public void updateButtonStatus(){
		backButton.setEnabled( webView.canGoBack() );
		forwardButton.setEnabled( webView.canGoForward() );
	}
	
	@Override
	public void onClick(View v) {
		ALog.d(TAG,"OnClick");
		if (v == backButton) {
			webView.goBack();
		}

		if (v == forwardButton) {
			webView.goForward();
		}
	}
}

