package com.omg.autographer;

import com.omg.autographer.R;
import com.omg.autographer.util.ALog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class GetStarted extends Activity {
	String TAG = "GetStarted";

	int initialX;
	final static int MINIMUM_DISTANCE = 50;

	Menu menu;
	ViewFlipper pictureFlipper, titleFlipper, descriptionFlipper;

	// -------- Lifecycle Methods --------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.get_started);

		pictureFlipper = (ViewFlipper) findViewById(R.id.pictureFlipper);

		titleFlipper = (ViewFlipper) findViewById(R.id.titleFlipper);
		titleFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
		titleFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

		descriptionFlipper = (ViewFlipper) findViewById(R.id.descriptionFlipper);
		descriptionFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
		descriptionFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

		pictureFlipper.setOnTouchListener(new PictureOnTouchListener());

		((ImageView) findViewById(R.id.dot1)).setImageResource(R.drawable.darkdot);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.Autographer_get_started);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.get_started_menu, menu);
		return true;
	}

	// listener to implement slide between pictures
	private class PictureOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // user starts touching the screen
				initialX = (int) event.getX();
				return true;
			case MotionEvent.ACTION_UP: // user stops touching the screen
				float distance = initialX - event.getX();

				if (distance < 0 && distance < -MINIMUM_DISTANCE && pictureFlipper.getDisplayedChild() > 0) {
					showPrevious();
				}

				if (distance > 0 && distance > MINIMUM_DISTANCE && pictureFlipper.getDisplayedChild() < pictureFlipper.getChildCount() - 1) {
					showNext();
				}
			default:
				break;
			}
			return false;
		}
	}

	private void setActiveDot(int active) {
		((ImageView) findViewById(R.id.dot1)).setImageResource(R.drawable.lightdot);
		((ImageView) findViewById(R.id.dot2)).setImageResource(R.drawable.lightdot);
		((ImageView) findViewById(R.id.dot3)).setImageResource(R.drawable.lightdot);
		((ImageView) findViewById(R.id.dot4)).setImageResource(R.drawable.lightdot);
		((ImageView) findViewById(R.id.dot5)).setImageResource(R.drawable.lightdot);
		((ImageView) findViewById(R.id.dot6)).setImageResource(R.drawable.lightdot);

		switch (active) {
		case 0:
			((ImageView) findViewById(R.id.dot1)).setImageResource(R.drawable.darkdot);
			break;
		case 1:
			((ImageView) findViewById(R.id.dot2)).setImageResource(R.drawable.darkdot);
			break;
		case 2:
			((ImageView) findViewById(R.id.dot3)).setImageResource(R.drawable.darkdot);
			break;
		case 3:
			((ImageView) findViewById(R.id.dot4)).setImageResource(R.drawable.darkdot);
			break;
		case 4:
			((ImageView) findViewById(R.id.dot5)).setImageResource(R.drawable.darkdot);
			break;
		case 5:
			((ImageView) findViewById(R.id.dot6)).setImageResource(R.drawable.darkdot);
			break;
		}
	}

	private void showNext() {
		pictureFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
		pictureFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
		pictureFlipper.getOutAnimation().setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				updateView();
			}
		});
		pictureFlipper.showNext();
		titleFlipper.showNext();
		descriptionFlipper.showNext();
	}

	private void showPrevious() {
		pictureFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
		pictureFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));

		pictureFlipper.getOutAnimation().setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				updateView();

			}
		});

		pictureFlipper.showPrevious();
		titleFlipper.showPrevious();
		descriptionFlipper.showPrevious();
	}

	private int getCurrentSection() {
		return pictureFlipper.indexOfChild(pictureFlipper.getCurrentView());
	}

	private int sectionCount() {
		return pictureFlipper.getChildCount();
	}

	private void updateView() {
		int currentSection = getCurrentSection();
		setActiveDot(currentSection);

		if (sectionCount() == currentSection + 1) {
			setOptionTitle(R.id.action_next, getResources().getString(R.string.Autographer_done));
		} else {
			setOptionTitle(R.id.action_next, getResources().getString(R.string.Autographer_next));
		}
	}

	private void setOptionTitle(int id, String title) {
		MenuItem item = menu.findItem(id);
		item.setTitle(title);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_next:
			if (sectionCount() == getCurrentSection() + 1) {
				finish();
			} else {
				showNext();
			}
			return true;
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