package com.omg.autographer.fragments;

import com.omg.autographer.MainActivity;
import com.omg.autographer.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Authenticate extends Fragment implements OnClickListener {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View mainView = inflater.inflate(R.layout.authenticate, container, false);

		return mainView;
	}

	@Override
	public void onClick(View v) {

	}
}
