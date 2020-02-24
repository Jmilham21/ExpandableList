package com.jmilham.expandinglist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ExpandingList extends LinearLayout {
    boolean expanded = false;
    String title = "";
    LinearLayout childLayout;
    LinearLayout parentLayout;
    TextView lblTitle;
    ImageView imgSpinner;


    public ExpandingList(Context context) {
        super(context);
        initView(context);
    }

    public ExpandingList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ExpandingList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        inflate(context, R.layout.expanding_list, this);
        imgSpinner = findViewById(R.id.imgSpinner);
        lblTitle = findViewById(R.id.lblTitle);
        childLayout = findViewById(R.id.childLayout);
        childLayout.removeAllViews();
        childLayout.setVisibility(GONE);
        parentLayout = findViewById(R.id.parentLayout);
        parentLayout.setOnClickListener(getClick());
    }

    // region Listener meant for the parent layout
    private View.OnClickListener getClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expanded) {
                    AnimationSet animSet = new AnimationSet(true);
                    animSet.setInterpolator(new DecelerateInterpolator());
                    animSet.setFillAfter(true);
                    animSet.setFillEnabled(true);
                    final RotateAnimation animRotate = new RotateAnimation(180f, 360f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    animRotate.setDuration(500);
                    animRotate.setFillAfter(true);
                    animSet.addAnimation(animRotate);
                    imgSpinner.startAnimation(animRotate);
                    expanded = false;
                    collapseChild();
                } else {
                    AnimationSet animSet = new AnimationSet(true);
                    animSet.setInterpolator(new DecelerateInterpolator());
                    animSet.setFillAfter(true);
                    animSet.setFillEnabled(true);
                    final RotateAnimation animRotate = new RotateAnimation(0f, 180f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    animRotate.setDuration(500);
                    animRotate.setFillAfter(true);
                    animSet.addAnimation(animRotate);
                    imgSpinner.startAnimation(animRotate);
                    expanded = true;
                    expandChild();
                }
            }
        };
    }

    // endregion

    // region Methods for collapsing and expanding child view
    private void collapseChild() {
        final int initialHeight = childLayout.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    childLayout.setVisibility(View.GONE);
                }else{
                    childLayout.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    childLayout.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
//        a.setDuration((int)(initialHeight / childLayout.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(500);
        childLayout.startAnimation(a);
    }

    private void expandChild() {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) childLayout.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        childLayout.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = childLayout.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        childLayout.getLayoutParams().height = 1;
        childLayout.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                childLayout.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                childLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
//        a.setDuration((int)(targetHeight / childLayout.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(500);
        childLayout.startAnimation(a);
    }

    // endregion

    public void setTitle(String title) {
        this.title = title;
        this.lblTitle.setText(title);
    }

    public void addLayout(ViewGroup layout){
        childLayout.addView(layout);
    }

}
