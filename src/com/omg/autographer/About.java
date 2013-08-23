package com.omg.autographer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ViewFlipper;

import com.omg.autographer.util.ALog;

public class About extends Activity implements OnClickListener {
	String TAG = "About";

	View supportEntry;

	// -------- Lifecycle Methods --------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);

		(supportEntry = findViewById(R.id.supportEntry)).setOnClickListener(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.Autographer_about);
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
		overridePendingTransition(R.anim.still, R.anim.bottom_out);
	}

	@Override
	public void onClick(View v) {
		if (v == supportEntry) {
			Intent intent = new Intent(this, AutographerSupport.class);
			this.startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}

	}
}
