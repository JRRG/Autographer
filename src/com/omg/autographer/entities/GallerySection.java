package com.omg.autographer.entities;

import java.util.ArrayList;

import com.omg.autographer.R;

import android.content.Context;
import android.widget.ImageView;

public class GallerySection {

	private ArrayList<ImageReference> references;
	private ArrayList<Boolean> selectedReferences;
	private int selectedCount = 0;
	
	public GallerySection(Context c, int n) {
		references = new ArrayList<ImageReference>();
		selectedReferences = new ArrayList<Boolean>();
		
		// DEV foo items
		for (int i = 0; i < n; i++) {
			add(new ImageReference( R.drawable.lenna ) ); // DEV the resource id will be the image id code
		}
	}
	
	public int size(){
		return references.size();
	}
	
	public void add(ImageReference ref){
		references.add(ref );
		selectedReferences.add(false);
	}
	
	public boolean isSelected(int position){
		return selectedReferences.get(position);
	}
	
	public void setSelected(int position, Boolean selected){
		if( isSelected(position) != selected ) {
			selectedReferences.set(position, selected);
			if( selected ) {
				selectedCount++;
			} else {
				selectedCount--;
			}
		}
	}
	
	public void clearSelected(){
		for( int i = 0; i<selectedReferences.size(); i++ ){
			setSelected(i,false);
		}
	}
	
	public int getSelectedCount(){
		return selectedCount;
	}
	
	public ImageReference get(int position){
		return references.get(position);
	}
}
