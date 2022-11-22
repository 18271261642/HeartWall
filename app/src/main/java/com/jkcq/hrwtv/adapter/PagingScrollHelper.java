package com.jkcq.hrwtv.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.jkcq.hrwtv.service.OnScrollListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 实现RecycleView分页滚动的工具类
 * Created by zhuguohui on 2016/11/10.
 */

public class PagingScrollHelper {

    RecyclerView mRecyclerView = null;

    private MyOnScrollListener mOnScrollListener = new MyOnScrollListener();

    private MyOnFlingListener mOnFlingListener = new MyOnFlingListener();
    private int offsetY = 0;
    private int offsetX = 0;

    int startY = 0;
    int startX = 0;

    private int SEPARATE = 8;//默认8个

    enum ORIENTATION {
        HORIZONTAL, VERTICAL, NULL
    }

    private ORIENTATION mOrientation = ORIENTATION.HORIZONTAL;

    public void setUpRecycleView(RecyclerView recycleView) {
        if (recycleView == null) {
            throw new IllegalArgumentException("recycleView must be not null");
        }
        mRecyclerView = recycleView;
        //处理滑动
        recycleView.setOnFlingListener(mOnFlingListener);
        //设置滚动监听，记录滚动的状态，和总的偏移量
        recycleView.setOnScrollListener(mOnScrollListener);
        //记录滚动开始的位置
        //recycleView.setOnTouchListener(mOnTouchListener);
        timerScroll();
        //获取滚动的方向
        updateLayoutManger();

    }

    public void updateLayoutManger() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.canScrollVertically()) {
                mOrientation = ORIENTATION.VERTICAL;
            } else if (layoutManager.canScrollHorizontally()) {
                mOrientation = ORIENTATION.HORIZONTAL;
            } else {
                mOrientation = ORIENTATION.NULL;
            }
            if (mAnimator != null) {
                mAnimator.cancel();
            }
            startX = 0;
            startY = 0;
            offsetX = 0;
            offsetY = 0;

        }

    }

    /**
     * 获取总共的页数
     */
    public int getPageCount() {
        if (mRecyclerView != null) {
            if (mOrientation == ORIENTATION.NULL) {
                return 0;
            }
            if (mOrientation == ORIENTATION.VERTICAL && mRecyclerView.computeVerticalScrollExtent() != 0) {
                return mRecyclerView.computeVerticalScrollRange() / mRecyclerView.computeVerticalScrollExtent();
            } else if (mRecyclerView.computeHorizontalScrollExtent() != 0) {
                Log.i("zzz", "rang=" + mRecyclerView.computeHorizontalScrollRange() + " extent=" + mRecyclerView.computeHorizontalScrollExtent());
                return mRecyclerView.computeHorizontalScrollRange() / mRecyclerView.computeHorizontalScrollExtent();
            }
        }
        return 0;
    }


    ValueAnimator mAnimator = null;

    public void scrollToPosition(int position) {
        if (mAnimator == null) {
            mOnFlingListener.onFling(0, 0);
        }
        if (mAnimator != null) {
            int startPoint = mOrientation == ORIENTATION.VERTICAL ? offsetY : offsetX, endPoint = 0;
            if (mOrientation == ORIENTATION.VERTICAL) {
                endPoint = mRecyclerView.getHeight() * position;
            } else {
                endPoint = mRecyclerView.getWidth() * position;
            }
            if (position != 0) {
                Log.e("shao", "  h: " + endPoint / position + "  startPoint : " + startPoint + " endPoint: " + endPoint);
            }
            if (startPoint != endPoint) {
                mAnimator.setIntValues(startPoint, endPoint);
                mAnimator.start();
            }
        }
    }

    public class MyOnFlingListener extends RecyclerView.OnFlingListener {

        @Override
        public boolean onFling(int velocityX, int velocityY) {
            long sta = System.currentTimeMillis();
            if (mOrientation == ORIENTATION.NULL) {
                return false;
            }
            //获取开始滚动时所在页面的index
            int p = getStartPageIndex();

            //记录滚动开始和结束的位置
            int endPoint = 0;
            int startPoint = 0;

            //如果是垂直方向
            if (mOrientation == ORIENTATION.VERTICAL) {
                startPoint = offsetY;

                /**
                 * 移动的便宜量
                 */
                if (velocityY < 0) {
                    //向上滑的时候移动的数据
                    p--;
                } else if (velocityY > 0) {
                    //向下滑动的时候计算滑动的数据
                    p++;
                }
                //更具不同的速度判断需要滚动的方向
                //注意，此处有一个技巧，就是当速度为0的时候就滚动会开始的页面，即实现页面复位
                endPoint = p * mRecyclerView.getHeight();

            } else {
                startPoint = offsetX;
                if (velocityX < 0) {
                    p--;
                } else if (velocityX > 0) {
                    p++;
                }
                endPoint = p * mRecyclerView.getWidth();

            }
            if (endPoint < 0) {
                endPoint = 0;
            }

            //使用动画处理滚动
            if (mAnimator == null) {
                mAnimator = new ValueAnimator().ofInt(startPoint, endPoint);

                mAnimator.setDuration(30);
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int nowPoint = (int) animation.getAnimatedValue();

                        if (mOrientation == ORIENTATION.VERTICAL) {
                            int dy = nowPoint - offsetY;
                            //这里通过RecyclerView的scrollBy方法实现滚动。
                            mRecyclerView.scrollBy(0, dy);
                        } else {
                            int dx = nowPoint - offsetX;
                            mRecyclerView.scrollBy(dx, 0);
                        }
                    }
                });
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //回调监听
                        if (null != mOnPageChangeListener) {
                            mOnPageChangeListener.onPageChange(getPageIndex());
                        }
                        //修复双击item bug
                        mRecyclerView.stopScroll();
                        startY = offsetY;
                        startX = offsetX;
                    }
                });
            } else {
                mAnimator.cancel();
                mAnimator.setIntValues(startPoint, endPoint);
            }

            mAnimator.start();
            long end = System.currentTimeMillis();
            Log.e("shao", "--------mAnimator- " + (end - sta));
            return true;
        }
    }

    public class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //newState==0表示滚动停止，此时需要处理回滚
            if (newState == 0 && mOrientation != ORIENTATION.NULL) {
                boolean move;
                int vX = 0, vY = 0;
                if (mOrientation == ORIENTATION.VERTICAL) {
                    int absY = Math.abs(offsetY - startY);
                    //如果滑动的距离超过屏幕的一半表示需要滑动到下一页
                    move = absY > recyclerView.getHeight() / 2;
                    vY = 0;

                    if (move) {
                        vY = offsetY - startY < 0 ? -1000 : 1000;
                    }

                } else {
                    int absX = Math.abs(offsetX - startX);
                    move = absX > recyclerView.getWidth() / 2;
                    if (move) {
                        vX = offsetX - startX < 0 ? -1000 : 1000;
                    }
                    //recyclerView.smoothScrollBy(vX, vY);
                }


                mOnFlingListener.onFling(vX, vY);

            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //滚动结束记录滚动的偏移量
            offsetY += dy;
            offsetX += dx;
        }
    }

    private Disposable mSecondDisposable;
    private int currentPage = 1;

    /**
     * 每过10s翻屏
     */
    public void timerScroll() {

        mSecondDisposable = Observable.interval(10, 10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {

                        int count = mRecyclerView.getAdapter().getItemCount();
                        int pageCount = 0;//总页数
                        int m = count % SEPARATE;
                        if (m > 0) {
                            pageCount = count / SEPARATE + 1;
                        } else {
                            pageCount = count / SEPARATE;
                        }
                        // Log.e("shao","count: "+count+" pageCount: "+pageCount+" currentPage: "+currentPage);
                        if (currentPage < pageCount) {
                            if (currentPage == 1) {
                                listener.onStartScroll();
                                mRecyclerView.scrollToPosition(0);
//                                ((GridLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0,0);
                            } else {
                                listener.onStartScroll();
                                mRecyclerView.scrollToPosition((currentPage - 1) * SEPARATE + (SEPARATE - 1));
//                                ((GridLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset((currentPage-1)*SEPARATE+(SEPARATE-1),0);
                            }
                            currentPage++;
                        } else if (currentPage == pageCount) {
//                            scrollToPosition(0);
                            listener.onStartScroll();
                            mRecyclerView.scrollToPosition(count - 1);
//                            ((GridLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(count-1,0);
                            currentPage = 1;
                        } else {
                            currentPage = 1;
                            listener.onStartScroll();
                            mRecyclerView.scrollToPosition(0);
                        }
                    }
                });

    }

    private OnScrollListener listener;

    public void setmOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    private int getPageIndex() {
        int p = 0;
        if (mRecyclerView.getHeight() == 0 || mRecyclerView.getWidth() == 0) {
            return p;
        }
        if (mOrientation == ORIENTATION.VERTICAL) {
            p = offsetY / mRecyclerView.getHeight();
        } else {
            p = offsetX / mRecyclerView.getWidth();
        }
        return p;
    }

    private int getStartPageIndex() {
        int p = 0;
        if (mRecyclerView.getHeight() == 0 || mRecyclerView.getWidth() == 0) {
            //没有宽高无法处理
            return p;
        }
        if (mOrientation == ORIENTATION.VERTICAL) {
            p = startY / mRecyclerView.getHeight();
        } else {
            p = startX / mRecyclerView.getWidth();
        }
        return p;
    }

    onPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(onPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public interface onPageChangeListener {
        void onPageChange(int index);
    }

    public void cancelTimer() {
        if (mSecondDisposable != null && !mSecondDisposable.isDisposed()) {
            mSecondDisposable.dispose();
        }
    }

    public void setSeparate(int separate) {
        this.SEPARATE = separate;
    }

}
