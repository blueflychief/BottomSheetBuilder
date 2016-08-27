package com.github.rubensousa.bottomsheetbuilder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.FrameLayout;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetMenuItem;
import com.github.rubensousa.bottomsheetbuilder.util.BottomSheetBuilderUtils;

public class BottomSheetMenuDialog extends BottomSheetDialog implements BottomSheetItemClickListener {

    private BottomSheetBehavior.BottomSheetCallback mCallback;
    private BottomSheetBehavior mBehavior;
    private BottomSheetItemClickListener mClickListener;
    private boolean mExpandOnStart;
    private boolean mDelayDismiss;
    private boolean mClicked;

    public BottomSheetMenuDialog(Context context) {
        super(context);
    }

    public BottomSheetMenuDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void dismiss() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FrameLayout sheet = (FrameLayout) findViewById(R.id.design_bottom_sheet);
        if (sheet != null) {
            mBehavior = BottomSheetBehavior.from(sheet);
            mBehavior.setBottomSheetCallback(mBottomSheetCallback);

            if (mExpandOnStart) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }

    public void expandOnStart(boolean expand) {
        mExpandOnStart = expand;
    }

    public void delayDismiss(boolean dismiss) {
        mDelayDismiss = dismiss;
    }

    public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback callback) {
        mCallback = callback;
    }

    public void setBottomSheetItemClickListener(BottomSheetItemClickListener listener) {
        mClickListener = listener;
    }

    public BottomSheetBehavior getBehavior() {
        return mBehavior;
    }

    @Override
    public void onBottomSheetItemClick(BottomSheetMenuItem item) {
        if (!mClicked) {

            if (mBehavior != null) {
                if (mDelayDismiss) {
                    BottomSheetBuilderUtils.delayDismiss(mBehavior);
                } else {
                    mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            if (mClickListener != null) {
                mClickListener.onBottomSheetItemClick(item);
            }

            mClicked = true;
        }
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {

            if (mCallback != null) {
                mCallback.onStateChanged(bottomSheet, newState);
            }

            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                BottomSheetMenuDialog.super.dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (mCallback != null) {
                mCallback.onSlide(bottomSheet, slideOffset);
            }
        }
    };

}
