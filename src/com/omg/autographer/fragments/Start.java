package com.omg.autographer.fragments;

import com.omg.autographer.MainActivity;
import com.omg.autographer.R;
import com.omg.autographer.GetStarted;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Start extends AutographerFragment implements OnClickListener {
	Button getStartedButton, authenticateButton, settingsButton, mapButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View mainView = inflater.inflate(R.layout.start, container, false);
		
		(authenticateButton = (Button) mainView.findViewById(R.id.authenticateButton)).setOnClickListener(this);
		(settingsButton = (Button) mainView.findViewById(R.id.settingsButton)).setOnClickListener(this);
		//(getStartedButton = (Button) mainView.findViewById(R.id.getStartedButton)).setOnClickListener(this);
		(mapButton = (Button) mainView.findViewById(R.id.mapButton)).setOnClickListener(this);
		
		return mainView;
	}

	@Override
	public void onClick(View v) {
		if( v == authenticateButton ){
			//( (MainActivity) getActivity() ).openFragment(new Authenticate(), "Authenticate" );
		}
		if( v == settingsButton ){
			//( (MainActivity) getActivity() ).openFragment(new Settings(), "Settings" );
		}
		if( v == getStartedButton ){
			Intent intent = new Intent( activity, GetStarted.class);
			this.startActivity(intent);
			activity.overridePendingTransition(R.anim.bottom_in,R.anim.still);
			//( (MainActivity) getActivity() ).openFragment(new Tour(), "Tour" );
		}
		if( v == mapButton ){
			//( (MainActivity) getActivity() ).openFragment(new Map(), "Map" );
		}
	}
}
