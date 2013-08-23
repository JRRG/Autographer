package com.omg.autographer.gui;

import com.omg.autographer.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsEntry extends LinearLayout {

	String label, value;
	View view;
	
	public SettingsEntry(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = LayoutInflater.from(context).inflate(R.layout.settings_entry, this, true);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SettingsEntry, 0, 0);

		try {
			label = a.getString(R.styleable.SettingsEntry_label);
			value = a.getString(R.styleable.SettingsEntry_value);
		} finally {
			a.recycle();
		}
		
		((TextView)view.findViewById(R.id.label)).setText(label);
		((TextView)view.findViewById(R.id.value)).setText(value);
		view.invalidate();
		
		
	}
	
	@Override
	public void onDraw( Canvas canvas ){
		super.onDraw(canvas);
	}
	
}
