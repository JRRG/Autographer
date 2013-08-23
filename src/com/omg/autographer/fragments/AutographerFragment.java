package com.omg.autographer.fragments;

import com.omg.autographer.MainActivity;
import com.omg.autographer.util.ALog;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class AutographerFragment extends Fragment implements OnClickListener {
	String TAG = "AutographerFragment";
	Activity activity;

	@Override
	public void onAttach(Activity activity) {
		ALog.d(TAG, "onAttach");
		super.onAttach(activity);
		this.activity = activity;
	}

	public String getRString(int resId) {
		return getResources().getString(resId);
	}

}
