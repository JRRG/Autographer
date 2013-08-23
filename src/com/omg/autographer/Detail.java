package com.omg.autographer;

import com.google.android.maps.MapActivity;
import com.omg.autographer.R;
import com.omg.autographer.fragments.AutographerFragment;
import com.omg.autographer.fragments.SlidePageFragment;
import com.omg.autographer.util.ALog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class Detail extends FragmentActivity implements OnClickListener {
	String TAG = "Detail";

	private ViewPager pager;

	boolean front = true;
	
	View shareButton, favouriteButton, playButton, tagButton, deleteButton;
	
	View captureInfo;
	View slideShadowTop, mapShadowTop;

	// -------- Lifecycle Methods --------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detail);

		View bottomPhotoBar = findViewById(R.id.bottomPhotoBar);
		(shareButton = bottomPhotoBar.findViewById(R.id.shareButton)).setOnClickListener(this);
		(favouriteButton = bottomPhotoBar.findViewById(R.id.favouriteButton)).setOnClickListener(this);
		(playButton = bottomPhotoBar.findViewById(R.id.playButton)).setOnClickListener(this);
		(tagButton = bottomPhotoBar.findViewById(R.id.tagButton)).setOnClickListener(this);
		(deleteButton = bottomPhotoBar.findViewById(R.id.deleteButton)).setOnClickListener(this);
		
		slideShadowTop = findViewById(R.id.slideShadowTop);
		mapShadowTop = findViewById(R.id.mapShadowTop);

		// enableBottomBarButtons(true);
		
		(captureInfo = findViewById(R.id.captureInfo)).setOnClickListener(this);

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new SlidePagerAdapter(getSupportFragmentManager()));
		pager.setPageTransformer(false, new ZoomOutPageTransformer());

		final GestureDetector tapGestureDetector = new GestureDetector(this, new TapGestureListener());

		pager.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				tapGestureDetector.onTouchEvent(event);
				return false;
			}
		});

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("34 of 123");
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

	private void enableBottomBarButtons(boolean enable) {
		shareButton.setEnabled(enable);
		favouriteButton.setEnabled(enable);
		playButton.setEnabled(enable);
		tagButton.setEnabled(enable);
		deleteButton.setEnabled(enable);
	}

	@Override
	public void onClick(View v) {
		if (v == shareButton) {
			
		}
		if (v == favouriteButton) {

		}
		if (v == playButton) {

		}
		if (v == tagButton) {
			Intent intent = new Intent(this, Tags.class);
			this.startActivity(intent);
			overridePendingTransition(R.anim.bottom_in, R.anim.still);
		}
		if (v == deleteButton) {

		}

		if (v == pager) {
			ALog.d(TAG, "pager click");
			pager.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.left_out_layout));
			// pager.getAnimation().reset();
			pager.getAnimation().start();
		}
		
		if( v == captureInfo){
			//flipCard();
			startFlip();
		}

	}

	private class SlidePagerAdapter extends FragmentStatePagerAdapter {
		public SlidePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			return new SlidePageFragment();
		}

		@Override
		public int getCount() {
			return 5;
		}
	}

	public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

		private static final float MAX_ROTATION = 8.0f;

		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			int pageHeight = view.getHeight();

			if (position <= -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);
			}
			if (position >= 1) { // [1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}

			if (position < 1 && position > -1) { // (-1,1)
				view.setAlpha(1);

				if (position <= 0) { // [1,0]
					view.setTranslationX(-position * pageWidth);
					view.setRotation(0);
				}

				if (position > 0) { // (0,1)
					view.setPivotX(pageWidth/4);
					view.setPivotY(pageHeight/4);
					view.setRotation((float) (position * MAX_ROTATION));
					view.setTranslationX(position* pageWidth/10);
				}
			}
		}
	}

	class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			ALog.d(TAG, "pager singletap");
			
			startFlip();

			return false;
		}
	}

	private void startFlip(){
		//((ViewGroup)slideShadowTop).setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.fade_out));
		
		Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		
		((ViewGroup)slideShadowTop).startAnimation(fadeOutAnimation);
		((ViewGroup)mapShadowTop).startAnimation(fadeOutAnimation);
		
		((ViewGroup)slideShadowTop).getAnimation().setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				ALog.d(TAG,"onAnimationEnd");
				slideShadowTop.setVisibility(View.INVISIBLE);
				mapShadowTop.setVisibility(View.INVISIBLE);
				flipCard();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
	}
	
	private void flipCard() {
		View rootLayout = (View) findViewById(R.id.slidePageContainer);
		View cardFace = (View) findViewById(R.id.pager);
		View cardBack = (View) findViewById(R.id.captureInfo);

		FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

		if (!front) {
			flipAnimation.reverse();
			front = true;
		} else {
			front = false;
		}
		rootLayout.startAnimation(flipAnimation);
		flipAnimation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				Animation fadeInAnimation = AnimationUtils.loadAnimation(Detail.this, R.anim.fade_in);
				((ViewGroup)slideShadowTop).startAnimation(fadeInAnimation);
				((ViewGroup)mapShadowTop).startAnimation(fadeInAnimation);
				slideShadowTop.setVisibility(View.VISIBLE);
				mapShadowTop.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}});
		
	}
	
	
	public class FlipAnimation extends Animation {
		private Camera camera;

		private View fromView;
		private View toView;

		private float centerX;
		private float centerY;

		private boolean forward = true;

		/**
		 * Creates a 3D flip animation between two views.
		 * 
		 * @param fromView
		 *            First view in the transition.
		 * @param toView
		 *            Second view in the transition.
		 */
		public FlipAnimation(View fromView, View toView) {
			this.fromView = fromView;
			this.toView = toView;

			setDuration(500);
			setFillAfter(false);
			setInterpolator(new AccelerateDecelerateInterpolator());
		}

		public void reverse() {
			forward = false;
			View switchView = toView;
			toView = fromView;
			fromView = switchView;
		}

		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			centerX = width / 2;
			centerY = height / 2;
			camera = new Camera();
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			// Angle around the y-axis of the rotation at the given time
			// calculated both in radians and degrees.
			final double radians = Math.PI * interpolatedTime;
			float degrees = (float) (180.0 * radians / Math.PI);

			// Once we reach the midpoint in the animation, we need to hide the
			// source view and show the destination view. We also need to change
			// the angle by 180 degrees so that the destination does not come in
			// flipped around
			if (interpolatedTime >= 0.5f) {
				degrees -= 180.f;
				fromView.setVisibility(View.INVISIBLE);
				toView.setVisibility(View.VISIBLE);
			}

			if (forward)
				degrees = -degrees; // determines direction of rotation when
									// flip begins

			final Matrix matrix = t.getMatrix();
			camera.save();
			camera.translate(0, 0, (float) (200*Math.sin(radians)));
			camera.rotateY(degrees);
			camera.getMatrix(matrix);
			camera.restore();
			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);

		}
	}
}