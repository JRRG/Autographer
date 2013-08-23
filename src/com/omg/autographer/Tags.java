package com.omg.autographer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ViewFlipper;

import com.omg.autographer.util.ALog;

public class Tags extends Activity implements OnClickListener {
	String TAG = "About";

	View supportEntry;
	LinearLayout tagLinearLayout;
	EditText tagEditText;

	// -------- Lifecycle Methods --------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tags);

		tagLinearLayout = (LinearLayout) findViewById(R.id.tagLinearLayout);
		tagEditText = (EditText) findViewById(R.id.tagEditText);

		tagEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		tagEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					addTag(v.getText().toString());
					v.setText("");
					return true;
				}
				return false;
			}
		});

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.Autographer_tags);
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

	private void addTag(String tag) {
		TextView tagView = (TextView) getLayoutInflater().inflate(R.layout.tag_text_view, null);
		tagView.setText(tag);
		tagLinearLayout.addView(tagView, tagLinearLayout.getChildCount() - 1);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(5, 5, 5, 5);
		tagView.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {

	}
}
