package com.example.michaelkibenko.ballaba.Views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.hrskrs.instadotlib.AnimationListener;
import com.hrskrs.instadotlib.AnimatorListener;
import com.hrskrs.instadotlib.Dot;
import com.hrskrs.instadotlib.InstaDotView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 12/06/2018.
 */

public class InstaDotViewRTL extends View {
    final private String TAG = InstaDotViewRTL.class.getSimpleName();

    private static final int MIN_VISIBLE_DOT_COUNT = 6;
    private static final int DEFAULT_VISIBLE_DOTS_COUNT = 6;
    private int activeDotSize;
    private int inactiveDotSize;
    private int mediumDotSize;
    private int smallDotSize;
    private int dotMargin;
    private Paint activePaint = new Paint(1);
    private Paint inactivePaint = new Paint(1);
    private int startPosX;
    private int posY = 0;
    private int previousPage = 0;
    private int currentPage = 0;
    private ValueAnimator translationAnim;
    private List<Dot> dotsList = new ArrayList();
    private int noOfPages = 0;
    private int visibleDotCounts = 6;

    public InstaDotViewRTL(Context context) {
        super(context);
        this.setup(context, (AttributeSet)null);
    }

    public InstaDotViewRTL(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setup(context, attrs);
    }

    public InstaDotViewRTL(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setup(context, attrs);
    }

    public InstaDotViewRTL(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setup(context, attrs);
    }

    private void setup(Context context, AttributeSet attributeSet) {
        Resources resources = this.getResources();
        if(attributeSet != null) {
            TypedArray ta = context.obtainStyledAttributes(attributeSet, com.hrskrs.instadotlib.R.styleable.InstaDotView);
            this.activePaint.setStyle(Paint.Style.FILL);
            this.activePaint.setColor(ta.getColor(com.hrskrs.instadotlib.R.styleable.InstaDotView_dot_activeColor, resources.getColor(com.hrskrs.instadotlib.R.color.active)));
            this.inactivePaint.setStyle(Paint.Style.FILL);
            this.inactivePaint.setColor(ta.getColor(com.hrskrs.instadotlib.R.styleable.InstaDotView_dot_inactiveColor, resources.getColor(com.hrskrs.instadotlib.R.color.inactive)));
            this.activeDotSize = ta.getDimensionPixelSize(com.hrskrs.instadotlib.R.styleable.InstaDotView_dot_activeSize, resources.getDimensionPixelSize(com.hrskrs.instadotlib.R.dimen.dot_active_size));
            this.inactiveDotSize = ta.getDimensionPixelSize(com.hrskrs.instadotlib.R.styleable.InstaDotView_dot_inactiveSize, resources.getDimensionPixelSize(com.hrskrs.instadotlib.R.dimen.dot_inactive_size));
            this.mediumDotSize = ta.getDimensionPixelSize(com.hrskrs.instadotlib.R.styleable.InstaDotView_dot_mediumSize, resources.getDimensionPixelSize(com.hrskrs.instadotlib.R.dimen.dot_medium_size));
            this.smallDotSize = ta.getDimensionPixelSize(com.hrskrs.instadotlib.R.styleable.InstaDotView_dot_smallSize, resources.getDimensionPixelSize(com.hrskrs.instadotlib.R.dimen.dot_small_size));
            this.dotMargin = ta.getDimensionPixelSize(com.hrskrs.instadotlib.R.styleable.InstaDotView_dot_margin, resources.getDimensionPixelSize(com.hrskrs.instadotlib.R.dimen.dot_margin));
            this.setVisibleDotCounts(ta.getInteger(com.hrskrs.instadotlib.R.styleable.InstaDotView_dots_visible, 6));
            ta.recycle();
        }

        this.posY = this.activeDotSize / 2;
        this.initCircles();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (this.activeDotSize + this.dotMargin) * (this.dotsList.size() + 1);
        int desiredHeight = this.activeDotSize;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        if(widthMode == 1073741824) {
            width = widthSize;
        } else if(widthMode == -2147483648) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        int height;
        if(heightMode == 1073741824) {
            height = heightSize;
        } else if(heightMode == -2147483648) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        this.setMeasuredDimension(width, height);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.drawCircles(canvas);
    }

    private void initCircles() {
        int viewCount = Math.min(this.getNoOfPages(), this.getVisibleDotCounts());
        if(viewCount >= 1) {
            this.setStartPosX(this.noOfPages > this.visibleDotCounts?this.getSmallDotStartX():0);
            this.dotsList = new ArrayList(viewCount);

            for(int i = 0; i < viewCount; ++i) {
                Dot dot = new Dot();
                State state;
                if(this.noOfPages > this.visibleDotCounts) {
                    if(i == this.getVisibleDotCounts() - 1) {
                        state = State.SMALL;
                    } else if(i == this.getVisibleDotCounts() - 2) {
                        state = State.MEDIUM;
                    } else {
                        state = i == 0? State.ACTIVE: State.INACTIVE;
                    }
                } else {
                    state = i == 0? State.ACTIVE: State.INACTIVE;
                }

                dot.setState(state);
                this.dotsList.add(dot);
            }

            this.invalidate();
        }
    }

    private void drawCircles(Canvas canvas) {
        int posX = this.getStartPosX();

        for(int i = 0; i < this.dotsList.size(); ++i) {
            Dot d = (Dot)this.dotsList.get(i);
            Paint paint = this.inactivePaint;
            int radius;
            switch(d.getState().ordinal()){//null.$SwitchMap$com$hrskrs$instadotlib$Dot$State[d.getState().ordinal()]) {
                case 3:
                    paint = this.activePaint;
                    radius = this.getActiveDotRadius();
                    posX += this.getActiveDotStartX();
                    break;
                case 2:
                    radius = this.getInactiveDotRadius();
                    posX += this.getInactiveDotStartX();
                    break;
                case 1:
                    radius = this.getMediumDotRadius();
                    posX += this.getMediumDotStartX();
                    break;
                case 4:
                    radius = this.getSmallDotRadius();
                    posX += this.getSmallDotStartX();
                    break;
                default:
                    radius = 0;
                    posX = 0;
            }

            canvas.drawCircle((float)posX, (float)this.posY, (float)radius, paint);
        }

    }

    private ValueAnimator getTranslationAnimation(int from, int to, final AnimationListener listener) {
        if(this.translationAnim != null) {
            this.translationAnim.cancel();
        }

        this.translationAnim = ValueAnimator.ofInt(new int[]{from, to});
        this.translationAnim.setDuration(120L);
        this.translationAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        this.translationAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = ((Integer)valueAnimator.getAnimatedValue()).intValue();
                if(getStartPosX() != val) {
                    setStartPosX(val);
                    invalidate();
                }

            }
        });
        this.translationAnim.addListener(new AnimatorListener() {
            public void onAnimationEnd(Animator animator) {
                if(listener != null) {
                    listener.onAnimationEnd();
                }

            }
        });
        return this.translationAnim;
    }

    public void setNoOfPages(int noOfPages) {
        this.setVisibility(noOfPages <= 1? GONE:VISIBLE);
        this.noOfPages = noOfPages;
        this.recreate();
    }

    public int getNoOfPages() {
        return this.noOfPages;
    }

    public void setVisibleDotCounts(int visibleDotCounts) {
        if(visibleDotCounts < 5) {
            throw new RuntimeException("Visible Dot count cannot be smaller than 5");
        } else {
            this.visibleDotCounts = visibleDotCounts;
            this.recreate();
        }
    }

    private void recreate() {
        this.initCircles();
        this.requestLayout();
        this.invalidate();
    }

    public int getVisibleDotCounts() {
        return this.visibleDotCounts;
    }

    public void setStartPosX(int startPosX) {
        this.startPosX = startPosX;
    }

    public int getStartPosX() {
        return this.startPosX;
    }

    public int getActiveDotStartX() {
        return this.activeDotSize + this.dotMargin;
    }

    private int getInactiveDotStartX() {
        return this.inactiveDotSize + this.dotMargin;
    }

    private int getMediumDotStartX() {
        return this.mediumDotSize + this.dotMargin;
    }

    private int getSmallDotStartX() {
        return this.smallDotSize + this.dotMargin;
    }

    private int getActiveDotRadius() {
        return this.activeDotSize / 2;
    }

    private int getInactiveDotRadius() {
        return this.inactiveDotSize / 2;
    }

    private int getMediumDotRadius() {
        return this.mediumDotSize / 2;
    }

    private int getSmallDotRadius() {
        return this.smallDotSize / 2;
    }

    public void onPageChange(int page) {
        this.currentPage = page;
        if(page != this.previousPage && page >= 0 && page <= this.getNoOfPages() - 1) {
            this.updateDots();
            this.previousPage = this.currentPage;
        }

    }

    private void updateDots() {
        if(this.noOfPages <= this.visibleDotCounts) {
            this.setupNormalDots();
        } else {
            for(int i = 0; i < this.dotsList.size(); ++i) {
                Dot currentDot = (Dot)this.dotsList.get(i);
                if(currentDot.getState().equals(State.ACTIVE)) {
                    currentDot.setState(State.INACTIVE);
                    if(this.currentPage > this.previousPage) {
                        this.setupFlexibleCirclesRight(i);
                    } else {
                        this.setupFlexibleCirclesLeft(i);
                    }

                    return;
                }
            }

        }
    }

    private void setupNormalDots() {
        ((Dot)this.dotsList.get(this.currentPage)).setState(State.ACTIVE);
        ((Dot)this.dotsList.get(this.previousPage)).setState(State.INACTIVE);
        this.invalidate();
    }

    private void setupFlexibleCirclesRight(int position) {
        if(position >= this.getVisibleDotCounts() - 3) {
            if(this.currentPage == this.getNoOfPages() - 1) {
                ((Dot)this.dotsList.get(this.dotsList.size() - 1)).setState(State.ACTIVE);
                this.invalidate();
            } else if(this.currentPage == this.getNoOfPages() - 2) {
                ((Dot)this.dotsList.get(this.dotsList.size() - 1)).setState(State.MEDIUM);
                ((Dot)this.dotsList.get(this.dotsList.size() - 2)).setState(State.ACTIVE);
                this.invalidate();
            } else {
                this.removeAddRight(position);
            }
        } else {
            ((Dot)this.dotsList.get(position + 1)).setState(State.ACTIVE);
            this.invalidate();
        }

    }

    private void removeAddRight(final int position) {
        this.dotsList.remove(0);
        this.setStartPosX(this.getStartPosX() + this.getSmallDotStartX());
        this.getTranslationAnimation(this.getStartPosX(), this.getSmallDotStartX(), new AnimationListener() {
            public void onAnimationEnd() {
                ((Dot)dotsList.get(0)).setState(State.SMALL);
                ((Dot)dotsList.get(1)).setState(State.MEDIUM);
                Dot newDot = new Dot();
                newDot.setState(State.ACTIVE);
                dotsList.add(position, newDot);
                invalidate();
            }
        }).start();
    }

    private void setupFlexibleCirclesLeft(int position) {
        if(position <= 2) {
            if(this.currentPage == 0) {
                ((Dot)this.dotsList.get(0)).setState(State.ACTIVE);
                this.invalidate();
            } else if(this.currentPage == 1) {
                ((Dot)this.dotsList.get(0)).setState(State.MEDIUM);
                ((Dot)this.dotsList.get(1)).setState(State.ACTIVE);
                this.invalidate();
            } else {
                this.removeAddLeft(position);
            }
        } else {
            ((Dot)this.dotsList.get(position - 1)).setState(State.ACTIVE);
            this.invalidate();
        }

    }

    private void removeAddLeft(final int position) {
        this.dotsList.remove(this.dotsList.size() - 1);
        this.setStartPosX(0);
        this.getTranslationAnimation(this.getStartPosX(), this.getSmallDotStartX(), new AnimationListener() {
            public void onAnimationEnd() {
                ((Dot)dotsList.get(dotsList.size() - 1)).setState(State.SMALL);
                ((Dot)dotsList.get(dotsList.size() - 2)).setState(State.MEDIUM);
                Dot newDot = new Dot();
                newDot.setState(State.ACTIVE);
                dotsList.add(position, newDot);
                invalidate();
            }
        }).start();
    }

    public class Dot {
        private State state;

        public Dot() {
        }

        public State getState() {
            return this.state;
        }

        public void setState(State state) {
            this.state = state;
        }


    }

    enum State {
        SMALL,
        MEDIUM,
        INACTIVE,
        ACTIVE;

        private State() {
        }
    }
}
