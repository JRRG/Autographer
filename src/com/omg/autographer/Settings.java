package com.omg.autographer;

import com.omg.autographer.R;
import com.omg.autographer.fragments.AutographerFragment;
import com.omg.autographer.util.ALog;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class Settings extends Activity {
	String TAG = "Settings";

	// -------- Lifecycle Methods --------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.Autographer_settings);
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

}