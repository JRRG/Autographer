package com.omg.autographer.fragments;

import com.omg.autographer.Detail;
import com.omg.autographer.GetStarted;
import com.omg.autographer.MainActivity;
import com.omg.autographer.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Stream extends Fragment implements OnClickListener {
	private final static String TAG = "Chapters";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View mainView = inflater.inflate(R.layout.gallery2, container, false);
		
		mainView.setOnClickListener(this);
		
		return mainView;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent( getActivity() , Detail.class);
		this.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.bottom_in,R.anim.still);
	}
}
