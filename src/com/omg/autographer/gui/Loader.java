package com.omg.autographer.gui;

import com.omg.autographer.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Loader extends ImageView {

	public Loader(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setBackgroundResource(R.drawable.loader);
		((AnimationDrawable) this.getBackground()).start();
	}

}
