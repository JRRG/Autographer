package com.omg.autographer.gui;

import com.omg.autographer.R;
import com.omg.autographer.util.ALog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
 
/**
 * A layout that arranges views into a grid of same-sized squares.
 * 
 * This source code contained in this file is in the Public Domain.
 * 
 * @author Tom Gibara
 *
 */
 
public class AdjustedGridLayout extends ViewGroup {
 
	private final String TAG = "AdjustedGridlayout";
	// fields
	
	/**
	 * Records the number of columns of the grid.
	 */
	private int columns = 1;
	
	private int rows = 1;
	/**
	 * Records the separation between the elements of the grid.
	 */
	private int separation = 0;
	
	/**
	 * Records the size of the square in pixels (excluding padding).
	 * This is set during {@link #onMeasure(int, int)} 
	 */
	
	private int mSquareDimensions;
	
	// constructors
 
	/**
	 * Constructor used to create layout programatically.
	 */
	
	public AdjustedGridLayout(Context context) {
		super(context);
	}
 
	/**
	 * Constructor used to inflate layout from XML. It extracts the size from
	 * the attributes and sets it.
	 */
	
	/* This requires a resource to be defined like this:
	 * 
	 * <resources>
	 *   <declare-styleable name="SquareGridLayout">
	 *     <attr name="size" format="integer"/>
	 *   </declare-styleable>
	 * </resources>
	 * 
	 * So that the attribute can be set like this:
	 * 
	 * <com.tomgibara.android.util.SquareGridLayout
	 *   xmlns:android="http://schemas.android.com/apk/res/android"
	 *   xmlns:util="http://schemas.android.com/apk/res/com.tomgibara.android.background"
	 *   util:size="3"
	 *   />
	 */
	
	public AdjustedGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AdjustedGridLayout);
		setColumns(a.getInt(R.styleable.AdjustedGridLayout_columns, 1));
		a.recycle();
	}
 
	// accessors
	
	/**
	 * Sets the number of views on each side of the square.
	 * 
	 * @param size the size of grid (at least 1)
	 */
	
	public void setColumns(int columns) {
		if (columns < 1)
			throw new IllegalArgumentException("size must be positive");
		if (this.columns != columns) {
			this.columns = columns;
			requestLayout();
		}
	}
	
	// View methods
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 
		// breakdown specs
		final int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
		final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		// compute padding
		final int paddingWidth = getPaddingLeft() + getPaddingRight();
		final int paddingHeight = getPaddingTop() + getPaddingBottom();
		
		// compute largest size of square (both with and without padding)
		final int largestSize;
		final int sp;
		if (modeWidth == MeasureSpec.UNSPECIFIED && modeHeight == MeasureSpec.UNSPECIFIED) {
			throw new IllegalArgumentException("Layout must be constrained on at least one axis");
		} else if (modeWidth == MeasureSpec.UNSPECIFIED) {
			ALog.d(TAG,"width unspecified");
			largestSize = sizeHeight;
			sp = largestSize - paddingHeight;
		} else if (modeHeight == MeasureSpec.UNSPECIFIED) {
			ALog.d(TAG,"height unspecified");
			largestSize = sizeWidth;
			sp = largestSize - paddingWidth;
		} else {
			ALog.d(TAG,"width and height specified");
			if (sizeWidth - paddingWidth < sizeHeight - paddingHeight) {
				largestSize = sizeWidth;
				sp = largestSize - paddingWidth;
			} else {
				largestSize = sizeHeight;
				sp = largestSize - paddingHeight;
			}
		}
 
		// guard against giving the children a -ve measure spec due to excessive padding
		//final int spp = Math.max(sp, 0);
		final int spp = Math.max(sp, 0);
		
		
		// pass on our rigid dimensions to our children
		ALog.d(TAG, "Children: " + getChildCount());
		ALog.d(TAG, "columns: " + columns);
		rows = columns%getChildCount() + 1;
		ALog.d(TAG, "Dimensions: " + rows + " " + columns);
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				final View child = getChildAt(y * rows + x);
				if (child == null) continue;
				// measure each child
				// we could try to accommodate oversized children, but we don't
				measureChildWithMargins(child,
					MeasureSpec.makeMeasureSpec( (spp + x) / columns , MeasureSpec.EXACTLY), 0,
					MeasureSpec.makeMeasureSpec((spp + x) / columns, MeasureSpec.EXACTLY), 0
				);
			}
		}
		
		//record our dimensions
		setMeasuredDimension(
			modeWidth == MeasureSpec.EXACTLY ? sizeWidth : sp + paddingWidth,
			//modeHeight == MeasureSpec.EXACTLY ? sizeHeight : ( sp / columns ) * rows
			( sp / columns ) * rows
		);
		mSquareDimensions = sp;
	}
	
	// ViewGroup methods
	
	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new MarginLayoutParams(p);
	}
 
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		ALog.d(TAG,"onLayout");
		// recover the previously computed square dimensions for efficiency
		final int s = mSquareDimensions;
		
		{
			// adjust for our padding
			final int pl = getPaddingLeft();
			final int pt = getPaddingTop();
			final int pr = getPaddingRight();
			final int pb = getPaddingBottom();
			
			// allocate any extra spare space evenly
			l = pl + (r - pr - l - pl - s) / 2;
			t = pt + (b - pb - t - pb - s) / 2;
		}
		
		
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				ALog.d(TAG, "Position: " + x + " " + y);
				View child = getChildAt(y * rows + x);
				// optimization: we are moving through the children in order
				// when we hit null, there are no more children to layout so return
				if (child == null) return;
				// get the child's layout parameters so that we can honour their margins
				MarginLayoutParams lps = (MarginLayoutParams) child.getLayoutParams();
				// we don't support gravity, so the arithmetic is simplified
				child.layout(
					(s *  x   ) / columns,// + lps.leftMargin,
					(s *  y   ) / rows,//+ lps.topMargin,
					(s * (x+1)) / columns,// - lps.rightMargin,
					 (s * (y+1)) / rows// - lps.bottomMargin
				);
			}
		}
	}
 
}
