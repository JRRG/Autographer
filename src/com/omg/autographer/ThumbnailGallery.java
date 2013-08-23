package com.omg.autographer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

import com.google.android.maps.MapActivity;
import com.omg.autographer.fragments.Chapters;
import com.omg.autographer.gui.NonSwipeableViewPager;
import com.omg.autographer.util.ALog;

public class ThumbnailGallery extends FragmentActivity implements OnClickListener{
	String TAG = "ThumbnailGallery";

	public final static int MODE_CHAPTERS = 0;
	public final static int MODE_STREAM = 1;

	private int mode = MODE_CHAPTERS;

	private boolean selectionEnabled = false;

	private int defaultActionBarColor;

	Chapters chaptersFragment, streamFragment;

	Menu menu;

	NonSwipeableViewPager galleryFlipper;
	
	View scrollDownButton, scrollUpButton;
	View actionButton, favouriteButton, tagButton, deleteButton;
	
	View scrollBottomBar,selectionBottomBar;

	// -------- Lifecycle Methods --------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.thumbnail_gallery);
		
		selectionBottomBar = findViewById(R.id.selectionBottomBar);
		(actionButton = selectionBottomBar.findViewById(R.id.actionButton) ).setOnClickListener(this);
		(favouriteButton = selectionBottomBar.findViewById(R.id.favouriteButton) ).setOnClickListener(this);
		(tagButton = selectionBottomBar.findViewById(R.id.tagButton) ).setOnClickListener(this);
		(deleteButton = selectionBottomBar.findViewById(R.id.deleteButton) ).setOnClickListener(this);
		
		selectionBottomBar.setVisibility(View.GONE);
		
		scrollBottomBar = findViewById(R.id.scrollBottomBar);
		(scrollUpButton = scrollBottomBar.findViewById(R.id.scrollUpButton) ).setOnClickListener(this);
		(scrollDownButton = scrollBottomBar.findViewById(R.id.scrollDownButton)).setOnClickListener(this);
		
		galleryFlipper = (NonSwipeableViewPager) findViewById(R.id.galleryFlipper);
		
		galleryFlipper.setAdapter(new GalleryFlipperAdapter( getSupportFragmentManager() ) );
		
		galleryFlipper.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				switch( position ){
				case 0:
					getActionBar().setTitle(R.string.Autographer_chapters);
					hideOption(R.id.action_select);
					showOption(R.id.action_settings);
					mode = MODE_CHAPTERS;
					break;
				case 1:
					getActionBar().setTitle(R.string.Autographer_stream);
					hideOption(R.id.action_settings);
					showOption(R.id.action_select);
					mode = MODE_STREAM;
					break;
				}
			}});
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.Autographer_chapters);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.thumbnail_gallery_menu, menu);
		hideOption(R.id.action_select);
		hideOption(R.id.action_done);
		hideOption(R.id.action_clear);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			setOutTransition();
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, Settings.class);
			startActivity(intent);
			overridePendingTransition(R.anim.bottom_in, R.anim.still);
			return true;
		case R.id.action_select:
			enableSelection(true);
			switchBottomBar();
			return true;
		case R.id.action_done:
			enableSelection(false);
			streamFragment.clearSelection();
			switchBottomBar();
			getActionBar().setTitle(R.string.Autographer_stream);
			return true;
		case R.id.action_clear:
			streamFragment.clearSelection();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void enableSelection(boolean enable) {
		selectionEnabled = enable;
		if (enable) {
			getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.autographer_main_yellow)));
			showOption(R.id.action_done);
			showOption(R.id.action_clear);
			hideOption(R.id.action_select);
			streamFragment.updateSelectionViews();
		} else {
			getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.autographer_black_gray)));
			hideOption(R.id.action_done);
			hideOption(R.id.action_clear);
			showOption(R.id.action_select);
			streamFragment.clearSelection();
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
	public void onBackPressed() {
		ALog.d(TAG,"onBackPressed " + getMode() );
		if( selectionEnabled ){
			enableSelection(false);
			switchBottomBar();
			getActionBar().setTitle(R.string.Autographer_stream);
		} else {
			if (getMode() == MODE_STREAM) {
				switchMode();
			} else {
				super.onBackPressed();
			}
		}
	}
	
	public void switchBottomBar(){
		if( scrollBottomBar.getVisibility() != View.VISIBLE ){
			selectionBottomBar.setVisibility(View.GONE);
			scrollBottomBar.setVisibility(View.VISIBLE);
		} else {
			selectionBottomBar.setVisibility(View.VISIBLE);
			scrollBottomBar.setVisibility(View.GONE);
		}
	}
	
	public void enableSelectionBottomBarButtons(boolean enable){
		actionButton.setEnabled(enable);
		favouriteButton.setEnabled(enable);
		tagButton.setEnabled(enable);
		deleteButton.setEnabled(enable);
	}

	public int getMode(){
		return galleryFlipper.getCurrentItem();
	}
	
	public void switchMode() {
		switch (getMode()) {
		case MODE_CHAPTERS:
			galleryFlipper.setCurrentItem(MODE_STREAM);
			break;
		case MODE_STREAM:
			galleryFlipper.setCurrentItem(MODE_CHAPTERS);
			break;
		}
	}

	private void hideOption(int id) {
		MenuItem item = menu.findItem(id);
		item.setVisible(false);
	}

	private void showOption(int id) {
		MenuItem item = menu.findItem(id);
		item.setVisible(true);
	}

	public void enableOption(int id, boolean enable ){
		MenuItem item = menu.findItem(id);
		item.setEnabled(enable);
	}
	
	public boolean isSelectionEnabled() {
		return selectionEnabled;
	}
	
	 public class GalleryFlipperAdapter extends FragmentPagerAdapter {
	        public GalleryFlipperAdapter(FragmentManager fm) {
	            super(fm);
	        }
	 
	        @Override
	        public int getCount() {
	            return 2;
	        }
	 
	        @Override
	        public Fragment getItem(int position) {
	            switch (position) {
	            case 0:
	            	chaptersFragment = new Chapters();
	        		chaptersFragment.setMode(MODE_CHAPTERS);
	                return chaptersFragment;
	            case 1:
	            	streamFragment = new Chapters();
	        		streamFragment.setMode(MODE_STREAM);
	                return streamFragment;
	            default:
	                return null;
	            }
	        }
	    }

	@Override
	public void onClick(View v) {
		
		if (v == scrollUpButton) {
			switch (mode) {
			case MODE_CHAPTERS:
				chaptersFragment.scrollUp();
				break;
			case MODE_STREAM:
				streamFragment.scrollUp();
				break;
			}
		}
		
		if( v == scrollDownButton ){
			switch (mode) {
			case MODE_CHAPTERS:
				chaptersFragment.scrollDown();
				break;
			case MODE_STREAM:
				streamFragment.scrollDown();
				break;
			}
		}
		

		if (v == actionButton) {
		}

		if (v == favouriteButton) {
		}
		
		if (v == tagButton) {
			Intent intent = new Intent(this, Tags.class);
			this.startActivity(intent);
			overridePendingTransition(R.anim.bottom_in, R.anim.still);
		}
		
		if (v == deleteButton) {
		}
	}
}
