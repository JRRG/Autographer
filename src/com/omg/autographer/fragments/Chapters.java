package com.omg.autographer.fragments;

import java.util.ArrayList;
import java.util.Random;

import com.omg.autographer.Detail;
import com.omg.autographer.R;
import com.omg.autographer.ThumbnailGallery;
import com.omg.autographer.entities.GallerySection;
import com.omg.autographer.gui.NonScrollableGridView;
import com.omg.autographer.util.ALog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Chapters extends Fragment implements OnClickListener {
	private final static String TAG = "Chapters";

	private final int SCROLL_DISTANCE = 300;
	private final int SCROLL_DURATION = 300;
	
	LinearLayout scrollContainer;

	ArrayList<GallerySection> sections;

	private int mode;
	
	ListView list;
	
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ALog.d(TAG, mode + " onCreateView");
		// Inflate the layout for this fragment
		View mainView = inflater.inflate(R.layout.gallery, container, false);
		list = (ListView) mainView.findViewById(R.id.list);
		sections = new ArrayList<GallerySection>();
		
		
		
		// DEV
		for (int i = 0; i < 5; i++) {
			sections.add(new GallerySection(getActivity(), new Random().nextInt(12) + 3));
		}

		GallerySectionListAdapter adapter = new GallerySectionListAdapter(getActivity(), sections);
		list.setAdapter(adapter);

		list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				// Get the actual view of the first visible item
				int wantedPosition = firstVisibleItem;
				int firstPosition = view.getFirstVisiblePosition() - ((ListView) view).getHeaderViewsCount();
				int wantedChild = wantedPosition - firstPosition;
				// Say, first visible position is 8, you want position 10,
				// wantedChild will now be 2
				// So that means your view is child #2 in the ViewGroup:
				if (wantedChild < 0 || wantedChild >= view.getChildCount()) {
					ALog.d(TAG, "Unable to get view for desired position, because it's not being displayed on screen.");
				} else {
					// adjust the header of the first visible child
					View firstView = view.getChildAt(wantedChild);
					View header = firstView.findViewById(R.id.header);
					View shadowHeader = header.findViewById(R.id.headerShadow);
					int translationY = (int) Math.min(-firstView.getY(), firstView.getHeight() - header.getHeight() + shadowHeader.getHeight());
					header.setY(translationY);
				}

				// restore the headers for the other non first visible children
				for (int i = 0; i < view.getChildCount(); i++) {
					if (i != wantedChild) {
						View nonFirstView = view.getChildAt(i);
						View header = nonFirstView.findViewById(R.id.header);
						header.setY(0);
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});

		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		ALog.d(TAG, mode + " onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	public void setMode(int m) {
		mode = m;
	}

	@Override
	public void onClick(View v) {
		/*
		ALog.d("onClick", "onClick");
		Intent intent = new Intent(getActivity(), Detail.class);
		this.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.still);
		*/

	}

	public void clearSelection(){
		for( GallerySection currentSection:sections ){
			currentSection.clearSelected();
			ALog.d(TAG,"currentSection selected count: " + currentSection.getSelectedCount());
		}
		updateSelectionViews();
		
		((GallerySectionListAdapter) list.getAdapter() ).notifyDataSetChanged();
	}
	
	public int getSelectionCount(){
		int count = 0;
		for( GallerySection currentSection:sections ){
			count += currentSection.getSelectedCount();
		}
		return count;
	}
	
	public void updateSelectionViews(){
		int selectionCount = getSelectionCount();
		String title;
		if( selectionCount == 0 ){
			title = getResources().getString(R.string.Autographer_select_images);
			((ThumbnailGallery) getActivity()).enableOption(R.id.action_clear, false);
			((ThumbnailGallery) getActivity()).enableSelectionBottomBarButtons(false);
			
		} else {
			title = getResources().getQuantityString(R.plurals.Autographer_d_images, selectionCount ,selectionCount);
			((ThumbnailGallery) getActivity()).enableOption(R.id.action_clear, true);
			((ThumbnailGallery) getActivity()).enableSelectionBottomBarButtons(true);
		}
		((ThumbnailGallery) getActivity()).getActionBar().setTitle( title );
	}
	
	public void scrollDown(){
		list.smoothScrollBy(SCROLL_DISTANCE, SCROLL_DURATION);
	}
	
	public void scrollUp(){
		list.smoothScrollBy(-SCROLL_DISTANCE, SCROLL_DURATION);
	}
	
	
	
	
	public class GallerySectionListAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		ArrayList<GallerySection> gallerySectionList;

		public GallerySectionListAdapter(Context context, ArrayList<GallerySection> gallerySectionList) {
			inflater = LayoutInflater.from(context);
			this.gallerySectionList = gallerySectionList;
		}

		@Override
		public int getCount() {
			return gallerySectionList.size();
		}

		@Override
		public GallerySection getItem(int position) {
			return gallerySectionList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.gallery_section, parent, false);
			}

			((NonScrollableGridView) convertView.findViewById(R.id.grid)).setAdapter(new GallerySectionAdapter(getActivity(), gallerySectionList.get(position)));
			convertView.setTag(position);
			return convertView;
		}

		public class GallerySectionAdapter extends BaseAdapter {
			private Context mContext;
			private GallerySection gallerySection;

			public GallerySectionAdapter(Context c, GallerySection gallerySection) {
				mContext = c;
				this.gallerySection = gallerySection;
			}

			public int getCount() {
				return gallerySection.size();
			}

			public Object getItem(int position) {
				return gallerySection.get(position);
			}

			public long getItemId(int position) {
				return 0;
			}

			public View getView(final int position, View convertView, ViewGroup parent) {
				ImageView imageView;
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.image_thumbnail, parent, false);
					int childSize = parent.getWidth() / 4;
					convertView.setLayoutParams(new NonScrollableGridView.LayoutParams(childSize, childSize));
					imageView = (ImageView) convertView.findViewById(R.id.image);
				}
				
				imageView = (ImageView) convertView.findViewById(R.id.image);
				imageView.setImageResource(gallerySection.get(position).getId());
				
				if (gallerySection.isSelected(position)) {
					convertView.findViewById(R.id.selector).setVisibility(View.VISIBLE);
				} else {
					convertView.findViewById(R.id.selector).setVisibility(View.GONE);
				}
				
				if (mode == ThumbnailGallery.MODE_CHAPTERS) {
					convertView.findViewById(R.id.time).setVisibility(View.VISIBLE);
				}
				if (mode == ThumbnailGallery.MODE_STREAM) {
					convertView.findViewById(R.id.time).setVisibility(View.GONE);
				}

				// to change mode or enter image details
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!((ThumbnailGallery) getActivity()).isSelectionEnabled()) {
							if (mode == ThumbnailGallery.MODE_CHAPTERS) {
								((ThumbnailGallery) getActivity()).switchMode();
							}

							if (mode == ThumbnailGallery.MODE_STREAM) {
								Intent intent = new Intent(getActivity(), Detail.class);
								Chapters.this.startActivity(intent);
								getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.still);
							}
						}
					}
				});
				
				// long click to enable selection mode
				convertView.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						if (!((ThumbnailGallery) getActivity()).isSelectionEnabled()) {
							if (mode == ThumbnailGallery.MODE_STREAM) {
								((ThumbnailGallery) getActivity()).enableSelection(true);
								gallerySection.setSelected(position, true );
								updateSelectionViews();
							}
						}
						return true;
					}
				});

				// to select an image
				convertView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (((ThumbnailGallery) getActivity()).isSelectionEnabled()) {
							if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
								gallerySection.setSelected(position, !gallerySection.isSelected(position)); // switch
								updateSelectionViews();
								notifyDataSetChanged();
								return true;
							}
						}
						return false;
					}
				});

				return convertView;
			}
		}
	}
}
