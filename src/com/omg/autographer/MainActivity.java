package com.omg.autographer;

import com.google.android.maps.MapActivity;
import com.omg.autographer.fragments.Start;
import com.omg.autographer.util.ALog;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = "MainActivity";
	
	private static final int STATE_WAITING = 0;
	private static final int STATE_CONNECT = 1;
	
	private int state = STATE_WAITING;
	
	ViewGroup waitingLayout, connectLayout, contentLayout;
	Button mainButton;
	
	// -------- Lifecycle Methods --------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		// setContentView(R.layout.capture_info);

		waitingLayout = (ViewGroup) findViewById(R.id.waitingLayout);
		connectLayout = (ViewGroup) findViewById(R.id.connectLayout);
		(mainButton = (Button) findViewById(R.id.mainButton) ).setOnClickListener(this);
		(contentLayout = (ViewGroup) findViewById(R.id.contentLayout) ).setOnClickListener(this);
		//bindAutographerService();
	}

	@Override
	public void onStart() {
		ALog.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	public void onRestart() {
		ALog.d(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	public void onResume() {
		ALog.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		ALog.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		ALog.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		ALog.d(TAG, "onDestroy");
		super.onDestroy();
		//unbindAutographerService();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_about:
			Intent intent = new Intent( this , About.class);
			this.startActivity(intent);
			overridePendingTransition(R.anim.bottom_in,R.anim.still);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
/*
	// -------- Navigation Methods --------//
	public void openFragment(Fragment f, String tag) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_left_in, R.animator.card_flip_left_out);
		//ft.setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
		ft.replace(body.getId(), f, tag);
		//ft.addToBackstack(body.getId(), f, tag);
		ft.addToBackStack(tag);
		ft.commit();
	}

	@Override
	public void onBackPressed(){
		FragmentManager fm = getFragmentManager();
		if( fm.getBackStackEntryCount() > 0 ) {
			fm.popBackStack();
		} else {
			super.onBackPressed();
		}
	}
*/
	// -------- Service Components --------//
	Messenger autographerService; // for outgoing communications
	private boolean isBound = false;

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AutographerService.SET_VALUE:
				ALog.d(TAG, "Received from service: " + msg.arg1);
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	// for incoming communications
	final Messenger messenger = new Messenger(new IncomingHandler());

	private ServiceConnection autographerConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ALog.d(TAG, "onServiceConnected");
			autographerService = new Messenger(service);
			try {
				// We want to monitor the service for as long as we are
		        // connected to it.
				Message msg = Message.obtain(null, AutographerService.REGISTER_CLIENT);
				msg.replyTo = messenger;
				autographerService.send(msg);

				// Give it some value as an example.
				msg = Message.obtain(null, AutographerService.SET_VALUE, this.hashCode(), 0);
				autographerService.send(msg);
			} catch (RemoteException e) {
				// In this case the service has crashed before we could even
				// do anything with it; we can count on soon being
				// disconnected (and then reconnected if it can be restarted)
				// so there is no need to do anything here.
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			ALog.d(TAG, "onServiceDisconnected");
		}
	};

	void bindAutographerService() {
		bindService(new Intent(MainActivity.this, AutographerService.class), autographerConnection, Context.BIND_AUTO_CREATE);
		isBound = true;
	}

	void unbindAutographerService() {
		if (isBound) {
			// If we have received the service, and hence registered with
			// it, then now is the time to unregister.
			if (autographerService != null) {
				try {
					Message msg = Message.obtain(null, AutographerService.UNREGISTER_CLIENT);
					msg.replyTo = messenger;
					autographerService.send(msg);
				} catch (RemoteException e) {
					// There is nothing special we need to do if the service
					// has crashed.
				}
			}

			// Detach our existing connection.
			unbindService(autographerConnection);
			isBound = false;
		}
	}

	//----------------------------//
	
	private void goToConnectState() {
		connectLayout.setVisibility(View.VISIBLE);
		waitingLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_out));
		connectLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.top_in));
		

		waitingLayout.getAnimation().setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				waitingLayout.setVisibility(View.GONE);
				mainButton.setText(getResources().getString(R.string.Autographer_connect));
				state = STATE_CONNECT;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		if( v == mainButton ){
			Intent intent;
			switch(state){
			case STATE_WAITING:
				intent = new Intent( this , GetStarted.class);
				this.startActivity(intent);
				overridePendingTransition(R.anim.bottom_in,R.anim.still);
				break;
			case STATE_CONNECT:
				intent = new Intent( this , ThumbnailGallery.class);
				this.startActivity(intent);
				overridePendingTransition(R.anim.bottom_in,R.anim.still);
				break;
			}
		}
		
		if( v == contentLayout ){
			goToConnectState();
		}
		
	}
}
