package com.yiwang.safekeyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.yiwang.safekeyboard.Constants.CHARACTERS_KEY_BOARD_TYPE;
import static com.yiwang.safekeyboard.Constants.DELETE_KEY_BOARD;
import static com.yiwang.safekeyboard.Constants.KEY_CODE_32;
import static com.yiwang.safekeyboard.Constants.KEY_CODE_48;
import static com.yiwang.safekeyboard.Constants.KEY_CODE_57;
import static com.yiwang.safekeyboard.Constants.LETTER_KEY_BOARD;
import static com.yiwang.safekeyboard.Constants.LETTER_KEY_BOARD_TYPE;
import static com.yiwang.safekeyboard.Constants.LETTER_SWITCH_KEY_BOARD;
import static com.yiwang.safekeyboard.Constants.NUMBER_DELETE_KEY_BOARD;
import static com.yiwang.safekeyboard.Constants.NUMBER_KEY_BOARD_TYPE;

public class SafeKeyboard {
    private Context mContext;               //上下文

    private LinearLayout mKeyboardParentView;
    private View mKeyContainer;              //自定义键盘的容器View
    private SafeKeyboardView mKeyboardView;  //键盘的View
    private Keyboard mKeyboardNumber;        //数字键盘
    private Keyboard mKeyboardNumberOnly;    //纯数字键盘
    private Keyboard mKeyboardLetterOnly;    //字母键盘
    private Keyboard mKeyboardLetterNum;     //字母数字键盘
    private Keyboard mKeyboardSymbol;        //符号键盘
    private Keyboard mKeyboardIdCard;        //中国身份证号码键盘
    private Keyboard mKeyboardLetter;        //字母键盘总成
    private static boolean sCapes = false;
    private boolean mCapLock = false;
    private boolean mShowStart = false;
    private boolean mHideStart = false;
    private boolean mForbidPreview = false;  // 关闭按键预览功能
    private boolean mLetterWithNum;  // 字母键盘是否带有数字
    private int mKeyboardType = LETTER_KEY_BOARD_TYPE;           // SafeKeyboard 键盘类型
    private int mCurrentInputTypeInEdit;    // 当前 EditText 的输入类型, (其实这个参数比较鸡肋, 使用 mCurrentEditText 即可)
    private static final int HIDE_TIME = 300;
    private static final int HIDE_TIME_OFFSET = 50;
    private static final int SHOW_DELAY = 200;
    private static final int HIDE_DELAY = 50;
    private static final int SHOW_TIME = 300;
    private static final int VIBRATE_TIME = 20;
    private static final int NUMBER_SIZE = 10;
    private static final int VERSION_16 = 16;
    private static final int VERSION_14 = 14;
    private static final int SCROLL_POS_IN_PAR_0 = 0;
    private static final int SCROLL_POS_IN_PAR_1 = 1;
    private static final int SCROLL_POS_IN_PAR_2 = 2;
    private static final int SCROLL_POS_IN_PAR_3 = 3;
    private Handler mSafeHandler = new Handler(Looper.getMainLooper());
    private Drawable mDelDrawable;
    private Drawable mLowDrawable;
    private Drawable mUpDrawable;
    //    private Drawable upDrawableLock;
    private int mKeyboardLayoutResId;
    private int mSafeKeyboardViewId;

    private TranslateAnimation mShowAnimation;
    private TranslateAnimation mHideAnimation;
    private EditText mCurrentEditText;
    private SparseArray<Keyboard.Key> mRandomDigitKeys;
    private SparseArray<Keyboard.Key> mRandomIdCardDigitKeys;
    private SparseIntArray mEditLastKeyboardTypeArray;

    private HashMap<Integer, EditText> mEditMap;
    private HashMap<Integer, EditText> mIdCardEditMap;
    private View.OnTouchListener mEditTextTouchListener;
    private View mRootView;
    private View mScrollLayout;
    private ViewTreeObserver.OnGlobalFocusChangeListener mGlobalFocusChangeListener;
    private ViewTreeObserver mTreeObserver;
    private ViewPoint mDownPoint;
    private ViewPoint mUpPoint;
    private int mScreenHeight;
    private float mToBackSize;   // 往上移动的距离, 为负值
    private int[] mOriginalScrollPosInScr;
    private int[] mOriginalScrollPosInPar;

    private Vibrator mVibrator;

    // 已支持多 EditText 共用一个 SafeKeyboard

    /**
     * SafeKeyboard 构造方法, 传入必要的参数
     *
     * @param mContext           上下文Context
     * @param keyboardParentView 界面上显示 SafeKeyboard 的 View
     * @param rootView           含有使用了 SafeKeyboard 的 EditText 的界面根布局 View
     *                           ( 多个 EditText 共用 SafeKeyboard 但其直接父布局不是同一个 View 时, 传入公共父布局)
     * @param scrollLayout       目标 EditText 父布局 View
     */
    public SafeKeyboard(Context mContext, LinearLayout keyboardParentView,
                        @NonNull View rootView, @NonNull View scrollLayout) {

        this(mContext, keyboardParentView, rootView, scrollLayout,
                false, false);
    }

    public SafeKeyboard(Context mContext, LinearLayout keyboardParentView,
                        @NonNull View rootView, @NonNull View scrollLayout, boolean isPackageByScrollView) {
        this(mContext, keyboardParentView, rootView, scrollLayout,
                false, isPackageByScrollView);
    }

    /**
     * SafeKeyboard 构造方法, 传入必要的参数
     *
     * @param mContext           上下文Context
     * @param keyboardParentView 界面上显示 SafeKeyboard 的 View
     * @param rootView           含有使用了 SafeKeyboard 的 EditText 的界面根布局 View
     * @param scrollLayout       目标 EditText 父布局 View
     *                           ( 多个 EditText 共用 SafeKeyboard 但其直接父布局不是同一个 View 时, 传入公共父布局)
     * @param letterWithNum      字母键盘是否带有数字
     */
    public SafeKeyboard(Context mContext, LinearLayout keyboardParentView,
                        @NonNull View rootView, @NonNull View scrollLayout, boolean letterWithNum, boolean isPackageByScrollView) {
        this.mContext = mContext;
        this.mKeyboardParentView = keyboardParentView;
        this.mKeyboardLayoutResId = R.layout.layout_keyboard_containor;
        this.mSafeKeyboardViewId = R.id.safeKeyboardLetter;
        this.mRootView = rootView;
        this.mScrollLayout = scrollLayout;
        this.mLetterWithNum = letterWithNum;

        initData();
        initKeyboard();
        initAnimation();
    }

    public SafeKeyboard(Context mContext, LinearLayout keyboardParentView, int keyboardLayoutResId, int mSafeKeyboardViewId,
                        Drawable del, Drawable low, Drawable up, Drawable upLock, @NonNull View rootView, @NonNull ViewGroup scrollLayout) {
        this.mContext = mContext;
        this.mKeyboardParentView = keyboardParentView;
        this.mKeyboardLayoutResId = keyboardLayoutResId;
        this.mSafeKeyboardViewId = mSafeKeyboardViewId;
        this.mDelDrawable = del;
        this.mLowDrawable = low;
        this.mUpDrawable = up;
//        this.upDrawableLock = upLock;
        this.mRootView = rootView;
        this.mScrollLayout = scrollLayout;
        this.mLetterWithNum = false;

        initData();
        initKeyboard();
        initAnimation();
    }

    public void enableRememberLastKeyboardType(boolean enable) {
        mKeyboardView.setmRememberLastType(enable);
    }

    private void initData() {
        mCapLock = false;
        sCapes = false;
        mToBackSize = 0;
        mDownPoint = new ViewPoint();
        mUpPoint = new ViewPoint();
        mEditMap = new HashMap<>();
        mIdCardEditMap = new HashMap<>();
        mEditLastKeyboardTypeArray = new SparseIntArray();
        mVibrator = null;
        mOriginalScrollPosInScr = new int[]{0, 0, 0, 0};
        mOriginalScrollPosInPar = new int[]{0, 0, 0, 0};

        // 获取 WindowManager 实例, 得到屏幕的操作权
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            // 给 metrics 赋值
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            // 设备屏幕的宽度,高度变量
            mScreenHeight = metrics.heightPixels;
        }
    }

    private void initAnimation() {
        mShowAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mShowAnimation.setDuration(SHOW_TIME);
        mHideAnimation.setDuration(HIDE_TIME);

        mShowAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mShowStart = true;
                // 在这里设置可见, 会出现第一次显示键盘时直接闪现出来, 没有动画效果, 后面正常
                // keyContainer.setVisibility(View.VISIBLE);
                // 动画持续时间 SHOW_TIME 结束后, 不管什么操作, 都需要执行, 把 isShowStart 值设为 false; 否则
                // 如果 onAnimationEnd 因为某些原因没有执行, 会影响下一次使用
                mSafeHandler.removeCallbacks(mShowEnd);
                mSafeHandler.postDelayed(mShowEnd, SHOW_TIME);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mShowStart = false;
                mKeyContainer.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mHideStart = true;
                // 动画持续时间 HIDE_TIME 结束后, 不管什么操作, 都需要执行, 把 isHideStart 值设为 false; 否则
                // 如果 onAnimationEnd 因为某些原因没有执行, 会影响下一次使用
                mSafeHandler.removeCallbacks(mHideEnd);
                mSafeHandler.postDelayed(mHideEnd, HIDE_TIME);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSafeHandler.removeCallbacks(mHideEnd);
                if (mHideStart) {
                    // isHideStart 未被置为初试状态, 说明还没有执行 hideEnd 内容, 这里手动执行一下
                    doHideEnd();
                }
                // 说明已经被执行了不需要在执行一遍了, 下面就什么都不用管了
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    private void initKeyboard() {
        mKeyContainer = LayoutInflater.from(mContext).inflate(mKeyboardLayoutResId, mKeyboardParentView, true);
        mKeyContainer.setVisibility(View.GONE);
        mKeyboardNumber = new Keyboard(mContext, R.xml.keyboard_num_symbol);     //实例化数字键盘
        // 注: 这里有三个数字键盘,  keyboard_num_symbol:带部分符号;   keyboard_num:可切换的数字键盘;    keyboard_num_only:纯数字键盘, 不可切换
        mKeyboardNumberOnly = new Keyboard(mContext, R.xml.keyboard_num_only);

        mKeyboardLetterOnly = new Keyboard(mContext, R.xml.keyboard_letter);         //实例化字母键盘
        mKeyboardLetterNum = new Keyboard(mContext, R.xml.keyboard_letter_num);         //实例化字母键盘
        mKeyboardSymbol = new Keyboard(mContext, R.xml.keyboard_symbol);         //实例化符号键盘
        mKeyboardIdCard = new Keyboard(mContext, R.xml.keyboard_id_card_zn);     //实例化 IdCard(中国身份证) 键盘
        // 由于符号键盘与字母键盘共用一个KeyBoardView, 所以不需要再为符号键盘单独实例化一个KeyBoardView

        mKeyboardLetter = mLetterWithNum ? mKeyboardLetterNum : mKeyboardLetterOnly;

        initRandomDigitKeys();
        initIdCardRandomDigitKeys();

        mKeyboardView = mKeyContainer.findViewById(mSafeKeyboardViewId);
        if (mDelDrawable == null) {
            mDelDrawable = mContext.getDrawable(R.drawable.icon_del);
        }
        if (mLowDrawable == null) {
            mLowDrawable = mContext.getDrawable(R.drawable.icon_capital_default);
        }
        if (mUpDrawable == null) {
            mUpDrawable = mContext.getDrawable(R.drawable.icon_capital_selected);
        }
//        if (upDrawableLock == null)
//            upDrawableLock = mContext.getDrawable(R.drawable.icon_capital_selected);
        mKeyboardView.setmDelDrawable(mDelDrawable);
        mKeyboardView.setmLowDrawable(mLowDrawable);
        mKeyboardView.setmUpDrawable(mUpDrawable);
//        keyboardView.setUpDrawableLock(upDrawableLock);
        // setKeyboard(keyboardLetter);                         //给键盘View设置键盘
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(mListener);

        FrameLayout done = mKeyContainer.findViewById(R.id.keyboardDone);
//        FrameLayout done = null;
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SafeKeyboard.this.isKeyboardShown()) {
                    mSafeHandler.removeCallbacks(mHideRun);
                    mSafeHandler.removeCallbacks(mShowRun);
                    mSafeHandler.postDelayed(mHideRun, HIDE_DELAY);
                }
            }
        });

        mKeyboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });

        if (mRootView != null) {
            mTreeObserver = mRootView.getViewTreeObserver();
            mGlobalFocusChangeListener = new ViewTreeObserver.OnGlobalFocusChangeListener() {
                @Override
                public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                    if (oldFocus instanceof EditText) {
                        // 上一个获得焦点的为 EditText
                        EditText oldEdit = (EditText) oldFocus;
                        if (mEditMap.get(oldEdit.getId()) != null) {
                            // 前 EditText 使用了 SafeKeyboard
                            // 新获取焦点的是 EditText
                            if (newFocus instanceof EditText) {
                                EditText newEdit = (EditText) newFocus;
                                if (mEditMap.get(newEdit.getId()) != null) {
                                    // 该 EditText 也使用了 SafeKeyboard
                                    // Log.i(TAG, "Safe --> Safe, 开始检查是否需要手动 show");
                                    SafeKeyboard.this.keyboardPreShow(newEdit);
                                } else {
                                    // 该 EditText 没有使用 SafeKeyboard, 则隐藏 SafeKeyboard
                                    // Log.i(TAG, "Safe --> 系统, 开始检查是否需要手动 hide");

                                    // 说明: 如果 EditText 外被 ScrollView 包裹, 切换成系统输入法的时候, SafeKeyboard 会被异常顶起
                                    // 需要在 Activity 的声明中增加 android:windowSoftInputMode="stateAlwaysHidden|adjustPan" 语句
                                    SafeKeyboard.this.keyboardPreHide();
                                }
                            } else {
                                // 新获取焦点的不是 EditText, 则隐藏 SafeKeyboard
                                // Log.i(TAG, "Safe --> 其他, 开始检查是否需要手动 hide");
                                SafeKeyboard.this.keyboardPreHide();
                            }
                        } else {
                            // 前 EditText 没有使用 SafeKeyboard
                            // 新获取焦点的是 EditText
                            if (newFocus instanceof EditText) {
                                EditText newEdit = (EditText) newFocus;
                                // 该 EditText 使用了 SafeKeyboard, 则显示
                                if (mEditMap.get(newEdit.getId()) != null) {
                                    // Log.i(TAG, "系统 --> Safe, 开始检查是否需要手动 show");
                                    SafeKeyboard.this.keyboardPreShow(newEdit);
                                } else {
                                    // Log.i(TAG, "系统 --> 系统, 开始检查是否需要手动 hide");
                                    SafeKeyboard.this.keyboardPreHide();
                                }
                            } else {
                                // ... 否则不需要管理此次事件, 但是为保险起见, 可以隐藏一次 SafeKeyboard, 当然隐藏前需要判断是否已显示
                                // Log.i(TAG, "系统 --> 其他, 开始检查是否需要手动 hide");
                                SafeKeyboard.this.keyboardPreHide();
                            }
                        }
                    } else {
                        // 新获取焦点的是 EditText
                        if (newFocus instanceof EditText) {
                            EditText newEdit = (EditText) newFocus;
                            // 该 EditText 使用了 SafeKeyboard, 则显示
                            if (mEditMap.get(newEdit.getId()) != null) {
                                // Log.i(TAG, "其他 --> Safe, 开始检查是否需要手动 show");
                                SafeKeyboard.this.keyboardPreShow(newEdit);
                            } else {
                                // Log.i(TAG, "其他 --> 系统, 开始检查是否需要手动 hide");
                                SafeKeyboard.this.keyboardPreHide();
                            }
                        } else {
                            // ... 否则不需要管理此次事件, 但是为保险起见, 可以隐藏一次 SafeKeyboard, 当然隐藏前需要判断是否已显示
                            // Log.i(TAG, "其他 --> 其他, 开始检查是否需要手动 hide");
                            SafeKeyboard.this.keyboardPreHide();
                        }
                    }
                }
            };
            mTreeObserver.addOnGlobalFocusChangeListener(mGlobalFocusChangeListener);
        }

        mEditTextTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof EditText) {
                    EditText mEditText = (EditText) v;
                    SafeKeyboard.this.hideSystemKeyBoard(mEditText);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mDownPoint.setCooX((int) event.getRawX());
                        mDownPoint.setCooY((int) event.getRawY());
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        mUpPoint.setCooX((int) event.getRawX());
                        mUpPoint.setCooY((int) event.getRawY());
                        if (SafeKeyboard.this.isTouchConsiderClick(mDownPoint, mUpPoint, mEditText) && mEditText.hasFocus()) {
                            if (mCurrentEditText == mEditText && SafeKeyboard.this.isShow()) {
                                return false;
                            }
                            SafeKeyboard.this.keyboardPreShow(mEditText);
                        }
                        mDownPoint.clearPoint();
                        mUpPoint.clearPoint();
                    }
                }
                return false;
            }
        };
    }

    /**
     * 用来计算按下和抬起时的两点位置的关系, 是否可以将此次 Touch 事件 看作 Click 事件
     * 两点各自的 x/y 轴距离不超过 touchSlop 最小滑动距离, 且两点中心点在目标 EditText 上 时, 返回 true, 否则 false
     *
     * @param down      按下时的位置点
     * @param up        抬起时的位置点
     * @param mEditText 目标 EditText
     * @return 是否考虑此次为点击事件
     */
    private boolean isTouchConsiderClick(ViewPoint down, ViewPoint up, EditText mEditText) {
        boolean flag = false;
        int touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        if (Math.abs(down.getCooX() - up.getCooX()) < touchSlop && Math.abs(down.getCooY() - up.getCooY()) < touchSlop) {
            int[] position = new int[2];
            mEditText.getLocationOnScreen(position);
            int width = mEditText.getWidth();
            int height = mEditText.getHeight();
            int x = (down.getCooX() + up.getCooX()) / 2;
            int y = (down.getCooY() + up.getCooY()) / 2;
            if (position[0] + width >= x && position[1] + height >= y) {
                flag = true;
            }
        }

        return flag;
    }

    private void keyboardPreHide() {
        mSafeHandler.removeCallbacks(mHideRun);
        mSafeHandler.removeCallbacks(mShowRun);
        getOriginalScrollLayoutPos();
        if (stillNeedOptManually(false)) {
            mSafeHandler.postDelayed(mHideRun, HIDE_DELAY);
        }
    }

    private void keyboardPreShow(final EditText mEditText) {
        mSafeHandler.removeCallbacks(mShowRun);
        mSafeHandler.removeCallbacks(mHideRun);
        getOriginalScrollLayoutPos();
        if (stillNeedOptManually(true)) {
            setCurrentEditText(mEditText);
            mSafeHandler.postDelayed(mShowRun, SHOW_DELAY);
        } else {
            // 说明不需要再手动显示, 只需要切换键盘模式即可 (甚至不用切换)
            // 这里需要检查当前 EditText 的显示是否合理
            final long delay = doScrollLayoutBack(false, mEditText) ? HIDE_TIME + HIDE_TIME_OFFSET : 0;
            mSafeHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 如果已经显示了, 那么切换键盘即可
                    SafeKeyboard.this.setCurrentEditText(mEditText);
                    SafeKeyboard.this.setKeyboard(SafeKeyboard.this.getKeyboardByInputType());
                }
            }, delay);
        }
    }

    private void initRandomDigitKeys() {
        mRandomDigitKeys = new SparseArray<>();
        List<Keyboard.Key> keys = mKeyboardNumber.getKeys();
        for (Keyboard.Key key : keys) {
            int code = key.codes[0];
            if (code >= KEY_CODE_48 && code <= KEY_CODE_57) {
                mRandomDigitKeys.put(code, key);
            }
        }
    }

    private void initIdCardRandomDigitKeys() {
        mRandomIdCardDigitKeys = new SparseArray<>();
        List<Keyboard.Key> keys = mKeyboardIdCard.getKeys();
        for (Keyboard.Key key : keys) {
            int code = key.codes[0];
            if (code >= KEY_CODE_48 && code <= KEY_CODE_57) {
                mRandomIdCardDigitKeys.put(code, key);
            }
        }
    }

    /**
     * 更新 mScrollLayout 原始位置, 且只获取一次
     */
    private void getOriginalScrollLayoutPos() {
        if (mOriginalScrollPosInScr[SCROLL_POS_IN_PAR_0] == SCROLL_POS_IN_PAR_0 && mOriginalScrollPosInScr[SCROLL_POS_IN_PAR_1] == SCROLL_POS_IN_PAR_0) {
            int[] pos = new int[]{SCROLL_POS_IN_PAR_0, SCROLL_POS_IN_PAR_0};
            mScrollLayout.getLocationOnScreen(pos);
            mOriginalScrollPosInScr[SCROLL_POS_IN_PAR_0] = pos[SCROLL_POS_IN_PAR_0];
            mOriginalScrollPosInScr[SCROLL_POS_IN_PAR_1] = pos[SCROLL_POS_IN_PAR_1];
            mOriginalScrollPosInScr[SCROLL_POS_IN_PAR_2] = pos[SCROLL_POS_IN_PAR_0] + mScrollLayout.getWidth();
            mOriginalScrollPosInScr[SCROLL_POS_IN_PAR_3] = pos[SCROLL_POS_IN_PAR_1] + mScrollLayout.getHeight();
        }

        if (mOriginalScrollPosInPar[SCROLL_POS_IN_PAR_0] == SCROLL_POS_IN_PAR_0 && mOriginalScrollPosInPar[SCROLL_POS_IN_PAR_1] == SCROLL_POS_IN_PAR_0
                && mOriginalScrollPosInPar[SCROLL_POS_IN_PAR_2] == SCROLL_POS_IN_PAR_0 && mOriginalScrollPosInPar[SCROLL_POS_IN_PAR_3] == SCROLL_POS_IN_PAR_0) {
            mOriginalScrollPosInPar[SCROLL_POS_IN_PAR_0] = mScrollLayout.getLeft();
            mOriginalScrollPosInPar[SCROLL_POS_IN_PAR_1] = mScrollLayout.getTop();
            mOriginalScrollPosInPar[SCROLL_POS_IN_PAR_2] = mScrollLayout.getRight();
            mOriginalScrollPosInPar[SCROLL_POS_IN_PAR_3] = mScrollLayout.getBottom();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void putEditText(EditText mEditText) {
        if (mEditMap == null) {
            mEditMap = new HashMap<>();
        }
        mEditMap.put(mEditText.getId(), mEditText);
        mEditText.setOnTouchListener(mEditTextTouchListener);
    }

    public void putEditText2IdCardType(int id, EditText mEditText) {
        if (mIdCardEditMap == null) {
            mIdCardEditMap = new HashMap<>();
        }
        mIdCardEditMap.put(id, mEditText);
    }

    /**
     * 设置是否强制关闭预览功能
     * <p>
     * 解释：因为系统自带的 KeyboardView 的按键预览功能是使用 PopupWindow 来实现的, 那么在
     * PopupWindow 中使用了 SafeKeyboard (本软键盘), 那么必须关闭预览, 否则会直接崩溃.
     * ( 即调用 setForbidPreview(true) )
     *
     * @param mForbidPreview 是否关闭预览
     */
    public void setmForbidPreview(boolean mForbidPreview) {
        this.mForbidPreview = mForbidPreview;
    }

    // 设置键盘点击监听
    private KeyboardView.OnKeyboardActionListener mListener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void onPress(int primaryCode) {
            if (mKeyboardType == NUMBER_KEY_BOARD_TYPE) {
                mKeyboardView.setPreviewEnabled(false);
            } else {
                mKeyboardView.setPreviewEnabled(!mForbidPreview);
                if (primaryCode == LETTER_SWITCH_KEY_BOARD || primaryCode == DELETE_KEY_BOARD || primaryCode == NUMBER_DELETE_KEY_BOARD || primaryCode == KEY_CODE_32 || primaryCode == LETTER_KEY_BOARD
                        || primaryCode == Constants.SPECIAL_CHARACTERS_KEY_BOARD /*|| primaryCode == -35*/) {
                    mKeyboardView.setPreviewEnabled(false);
                } else {
                    mKeyboardView.setPreviewEnabled(!mForbidPreview);
                }
            }
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            try {
                Editable editable = mCurrentEditText.getText();
                int start = mCurrentEditText.getSelectionStart();
                int end = mCurrentEditText.getSelectionEnd();
                if (primaryCode == Keyboard.KEYCODE_CANCEL) {
                    // 隐藏键盘
                    mSafeHandler.removeCallbacks(mHideRun);
                    mSafeHandler.removeCallbacks(mShowRun);
                    mSafeHandler.post(mHideRun/*, HIDE_DELAY*/);
                } else if (primaryCode == Keyboard.KEYCODE_DELETE || primaryCode == DELETE_KEY_BOARD || primaryCode == NUMBER_DELETE_KEY_BOARD) {

                    // 回退键,删除字符
                    if (editable != null && editable.length() > 0) {
                        if (start == end) { //光标开始和结束位置相同, 即没有选中内容
                            editable.delete(start - 1, start);
                        } else { //光标开始和结束位置不同, 即选中EditText中的内容
                            editable.delete(start, end);
                        }
                    }
                } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                    // 大小写切换
                    changeKeyboardLetterCase();
                    // 重新setKeyboard, 进而系统重新加载, 键盘内容才会变化(切换大小写)
                    mKeyboardType = LETTER_KEY_BOARD_TYPE;
                    switchKeyboard();
                } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                    // 数字与字母键盘互换
                    if (mKeyboardType == NUMBER_KEY_BOARD_TYPE) { //当前为数字键盘
                        mKeyboardType = LETTER_KEY_BOARD_TYPE;
                    } else {        //当前不是数字键盘
                        mKeyboardType = NUMBER_KEY_BOARD_TYPE;
                    }
                    switchKeyboard();
                } else if (primaryCode == Constants.SPECIAL_CHARACTERS_KEY_BOARD) {
                    // 字母与符号切换
                    if (mKeyboardType == CHARACTERS_KEY_BOARD_TYPE) { //当前是符号键盘
                        mKeyboardType = LETTER_KEY_BOARD_TYPE;
                    } else {        //当前不是符号键盘, 那么切换到符号键盘
                        mKeyboardType = CHARACTERS_KEY_BOARD_TYPE;
                    }
                    switchKeyboard();
                } else {
                    // 输入键盘值
                    // editable.insert(start, Character.toString((char) primaryCode));
                    editable.replace(start, end, Character.toString((char) primaryCode));
                    if (mEditLastKeyboardTypeArray.get(mCurrentEditText.getId(), 1) == 1 && !mCapLock && sCapes) {
                        sCapes = mCapLock = false;
                        toLowerCase();
                        mKeyboardView.setmIsCap(sCapes);
                        mKeyboardView.setmIsCapLock(mCapLock);
                        switchKeyboard();
                    }
                }

                // 添加按键震动
                if (mKeyboardView != null && mKeyboardView.isVibrateEnable()) {
                    if (mVibrator == null) {
                        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    }
                    if (mVibrator != null) {
                        mVibrator.vibrate(VIBRATE_TIME);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeUp() {
        }
    };

    private void refreshDigitKeyboard(Keyboard keyboard) {
        if (keyboard != null) {
            SparseArray<Keyboard.Key> randomKeys;
            if (keyboard == mKeyboardIdCard) {
                // 如果是 IdCard 键盘
                randomKeys = mRandomIdCardDigitKeys;
            } else {
                // 否则认为是 数字 键盘
                randomKeys = mRandomDigitKeys;
            }
            HashSet<Integer> set = new HashSet<>();
            while (set.size() < NUMBER_SIZE) {
                int num = (int) (Math.random() * NUMBER_SIZE);
                if (set.add(num)) {
                    // set.size() - 1 表示目前是第几个数字按键
                    Keyboard.Key key = randomKeys.get(set.size() - 1 + KEY_CODE_48);
                    key.label = num + "";
                    key.codes[0] = KEY_CODE_48 + num;
                }
            }
        } else {
//            Log.w(TAG, "Refresh Digit ERROR! Keyboard is null");
        }
    }

    private void switchKeyboard() {
        switch (mKeyboardType) {
            case LETTER_KEY_BOARD_TYPE:
                setKeyboard(mKeyboardLetter);
                break;
            case CHARACTERS_KEY_BOARD_TYPE:
                setKeyboard(mKeyboardSymbol);
                break;
            case NUMBER_KEY_BOARD_TYPE:
                if (mKeyboardView.ismRandomDigit()) {
                    refreshDigitKeyboard(mKeyboardNumber);
                }
                setKeyboard(mKeyboardNumber);
                break;
            default:
//                Log.e(TAG, "ERROR keyboard type");
                break;
        }
    }

    private void setKeyboard(Keyboard keyboard) {
        int type;
        if (keyboard == mKeyboardLetter) {
            type = LETTER_KEY_BOARD_TYPE;
        } else if (keyboard == mKeyboardSymbol) {
            type = CHARACTERS_KEY_BOARD_TYPE;
        } else if (keyboard == mKeyboardNumber || keyboard == mKeyboardNumberOnly || keyboard == mKeyboardIdCard/*|| mEditIsNumInput(mCurrentEditText)*/) {
            type = NUMBER_KEY_BOARD_TYPE;
        } else {
            type = LETTER_KEY_BOARD_TYPE;
        }
        mEditLastKeyboardTypeArray.put(mCurrentEditText.getId(), type);
        mKeyboardType = type;
        mKeyboardView.setKeyboard(keyboard);
        // hideSystemKeyBoard(mCurrentEditText);
    }

    private boolean mEditIsNumInput(EditText mCurrentEditText) {
        return mCurrentEditText.getInputType() == EditorInfo.TYPE_CLASS_NUMBER;
    }

    private void changeKeyboardLetterCase() {
        if (!sCapes) {
            // 为小写时, 改为大写.
            toUpperCase();
        } else if (mCapLock) {
            toLowerCase();
        }
        if (mCapLock) {
            mCapLock = sCapes = false;
        } else if (sCapes) {
            mCapLock = true;
        } else {
            sCapes = true;
            mCapLock = true;
        }
        mKeyboardView.setmIsCap(sCapes);
        mKeyboardView.setmIsCapLock(mCapLock);
    }

    private void toLowerCase() {
        List<Keyboard.Key> keyList = mKeyboardLetter.getKeys();
        for (Keyboard.Key key : keyList) {
            if (key.label != null && isUpCaseLetter(key.label.toString())) {
                key.label = key.label.toString().toLowerCase();
                key.codes[0] += KEY_CODE_32;
            }
        }
    }

    private void toUpperCase() {
        List<Keyboard.Key> keyList = mKeyboardLetter.getKeys();
        for (Keyboard.Key key : keyList) {
            if (key.label != null && isLowCaseLetter(key.label.toString())) {
                key.label = key.label.toString().toUpperCase();
                key.codes[0] -= KEY_CODE_32;
            }
        }
    }

    public void hideKeyboard() {
        if (mKeyContainer.getVisibility() == View.VISIBLE) {
            mKeyContainer.clearAnimation();
            mKeyContainer.startAnimation(mHideAnimation);
        }
    }

    /**
     * 只起到延时开始显示的作用
     */
    private final Runnable mShowRun = new Runnable() {
        @Override
        public void run() {
            showKeyboard();
        }
    };

    private final Runnable mHideRun = new Runnable() {
        @Override
        public void run() {
            hideKeyboard();
        }
    };

    private final Runnable mHideEnd = new Runnable() {
        @Override
        public void run() {
            doHideEnd();
        }
    };

    private void doHideEnd() {
        mHideStart = false;

        doScrollLayoutBack(true, null);

        mKeyContainer.clearAnimation();
        if (mKeyContainer.getVisibility() != View.GONE) {
            mKeyContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 回落
     *
     * @param isHide 回落的同时, SafeKeyboard 是否隐藏
     */
    private boolean doScrollLayoutBack(final boolean isHide, EditText mEditText) {
        int thisScrollY = 0;
        if (!isHide && mEditText != null) {
            // 这种情况说明是点击了一个 EditText, 则需要判断是否需要移动 mScrollLayout 来适应 SafeKeyboard 的显示
            int[] mEditPos = new int[2];
            mEditText.getLocationOnScreen(mEditPos);
//            Log.e("SafeKeyboard_Scroll", "0: " + mEditPos[0] + ", 1: " + mEditPos[1]);

            int keyboardHeight = mKeyContainer.getHeight();
            int keyStartY = mScreenHeight - keyboardHeight;
            getOriginalScrollLayoutPos();
            int touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

            if (mEditText.getHeight() + touchSlop > keyStartY - mOriginalScrollPosInScr[1]) {
                // mEditText 的高度 大于 SafeKeyboard 上边界到 mScrollLayout 上边界的距离, 即 mEditText 无法完全显示
                // 添加一个长文本输入功能

                return false;
            } else {
                // 可以正常显示
                if (mEditPos[1] < mOriginalScrollPosInScr[1]) {
                    // 说明当前的 mEditText 的 top 位置已经被其他布局遮挡, 需要布局往下滑动一点, 使 mEditText 可以完全显示
                    thisScrollY = mOriginalScrollPosInScr[1] - mEditPos[1] + touchSlop; // 正值
                } else if (mEditPos[1] + mEditText.getHeight() > keyStartY) {
                    // 说明当前的 mEditText 的 bottom 位置已经被其他布局遮挡, 需要布局往上滑动一点, 使 mEditText 可以完全显示
                    thisScrollY = keyStartY - mEditPos[1] - mEditText.getHeight(); //负值
                } else {
                    // 各项均正常, 不需要重新滑动
//                    Log.i("SafeKeyboard_LOG", "No need to scroll");
                    return false;
                }
            }
        }

        mToBackSize += thisScrollY;
        if (isHide) {
            mScrollLayout.animate().setDuration(SHOW_TIME).translationYBy(-mToBackSize).start();
            mToBackSize = 0;
        } else {
            mScrollLayout.animate().setDuration(SHOW_TIME).translationYBy(thisScrollY).start();
        }

        return true;
    }

    /**
     * 顶起
     */
    private void doScrollLayout() {
        // 计算 SafeKeyboard 显示后是否会遮挡住 EditText
        editNeedScroll(mCurrentEditText);
    }

    private final Runnable mShowEnd = new Runnable() {
        @Override
        public void run() {
            mShowStart = false;
            // 在迅速点击不同输入框时, 造成自定义软键盘和系统软件盘不停的切换, 偶尔会出现停在使用系统键盘的输入框时, 没有隐藏
            // 自定义软键盘的情况, 为了杜绝这个现象, 加上下面这段代码
            if (!mCurrentEditText.isFocused()) {
                mSafeHandler.removeCallbacks(mHideRun);
                mSafeHandler.removeCallbacks(mShowRun);
                mSafeHandler.postDelayed(mHideRun, HIDE_DELAY);
            }

            // 这个只能在 keyContainer 显示后才能调用, 只有这个时候才能获取到 keyContainer 的宽、高值
            SafeKeyboard.this.doScrollLayout();
        }
    };

    private void showKeyboard() {
        Keyboard mKeyboard = getKeyboardByInputType();
        if (mKeyboard != null && (mKeyboard == mKeyboardNumber || mKeyboard == mKeyboardIdCard
                || mKeyboard == mKeyboardNumberOnly) && mKeyboardView.ismRandomDigit()) {
            refreshDigitKeyboard(mKeyboard);
        }
        setKeyboard(mKeyboard == null ? mKeyboardLetter : mKeyboard);
        mKeyContainer.setVisibility(View.VISIBLE);
        mKeyContainer.clearAnimation();
        mKeyContainer.startAnimation(mShowAnimation);
    }

    /**
     * @param mEditText 目标 EditText
     */
    private void editNeedScroll(EditText mEditText) {
        int keyboardHeight = mKeyContainer.getHeight();
        int keyStartY = mScreenHeight - keyboardHeight;
        int[] position = new int[2];
        mEditText.getLocationOnScreen(position);
        int mEditTextBottomY = position[1] + mEditText.getHeight();
        if (mEditTextBottomY - keyStartY > 0) {
            final float to = keyStartY - mEditTextBottomY - 10; // 为负值
            if (position[1] + to < mOriginalScrollPosInScr[1]) {
                // 说明往上顶起之后 mEditText 会被遮挡, 即 mEditText 的 top 距离顶部的距离 小于 要移动的距离
                // 这里就不需要顶起了, 需要显示一个长文本显示页面
                // 添加一个长文本显示功能, 不过这里的长文本显示似乎没有什么意义
                return;
            }
            mToBackSize = to;
            mScrollLayout.animate().translationYBy(mToBackSize).setDuration(SHOW_TIME).start();
        }
    }

    private Keyboard getKeyboardByInputType() {
        Keyboard lastKeyboard = mKeyboardLetter; // 默认字母键盘

        if (mCurrentInputTypeInEdit == InputType.TYPE_CLASS_NUMBER) {
            lastKeyboard = mKeyboardNumberOnly;
        } else if (mIdCardEditMap.get(mCurrentEditText.getId()) != null) {
            lastKeyboard = mKeyboardIdCard;
        } else if (mKeyboardView.ismRememberLastType()) {
            int type = mEditLastKeyboardTypeArray.get(mCurrentEditText.getId(), 1);
            switch (type) {
                case LETTER_KEY_BOARD_TYPE:
                    lastKeyboard = mKeyboardLetter;
                    break;
                case CHARACTERS_KEY_BOARD_TYPE:
                    lastKeyboard = mKeyboardSymbol;
                    break;
                case NUMBER_KEY_BOARD_TYPE:
                    lastKeyboard = mKeyboardNumber;
                    break;
                default:
//                    Log.e(TAG, "ERROR keyboard type");
                    break;
            }
        }

        return lastKeyboard;
    }

    private boolean isLowCaseLetter(String str) {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        return letters.contains(str);
    }

    private boolean isUpCaseLetter(String str) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return letters.contains(str);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void add2MapAllAndEditTextListeners(ArrayList<EditText> editTexts) {
        for (EditText text : editTexts) {
            mEditMap.put(text.getId(), text);
            text.setOnTouchListener(mEditTextTouchListener);
        }
    }

    private void setCurrentEditText(EditText mEditText) {
        mCurrentEditText = mEditText;
        mCurrentInputTypeInEdit = mEditText.getInputType();
    }

    public boolean isShow() {
        return isKeyboardShown();
    }

    //隐藏系统键盘关键代码
    private void hideSystemKeyBoard(EditText edit) {
        this.mCurrentEditText = edit;
        InputMethodManager imm = (InputMethodManager) this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }

        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= VERSION_16) {
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= VERSION_14) {
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            edit.setInputType(0);
        } else {
            try {
                Method setShowSoftInputOnFocus = EditText.class.getMethod(methodName, Boolean.TYPE);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edit, Boolean.FALSE);
            } catch (NoSuchMethodException e) {
                edit.setInputType(0);
                e.printStackTrace();
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isKeyboardShown() {
        return mKeyContainer.getVisibility() == View.VISIBLE;
    }

    public boolean stillNeedOptManually(boolean preferShow) {
        boolean flag;
        if (preferShow) {
            // 想要显示
            flag = mHideStart || (!isKeyboardShown() && !mShowStart);
        } else {
            // 想要隐藏
            flag = mShowStart || (isKeyboardShown() && !mHideStart);
        }
        return flag;
    }

    public void setDelDrawable(Drawable delDrawable) {
        this.mDelDrawable = delDrawable;
        mKeyboardView.setmDelDrawable(mDelDrawable);
    }

    public void setLowDrawable(Drawable lowDrawable) {
        this.mLowDrawable = lowDrawable;
        mKeyboardView.setmLowDrawable(mLowDrawable);
    }

    public void setUpDrawable(Drawable upDrawable) {
        this.mUpDrawable = upDrawable;
        mKeyboardView.setmUpDrawable(mUpDrawable);
    }

//    public void setUpDrawableLock(Drawable upDrawableLock) {
//        this.upDrawableLock = upDrawableLock;
//        keyboardView.setUpDrawable(upDrawableLock);
//    }

    public void release() {
        sCapes = false;
        mToBackSize = 0;
        mEditTextTouchListener = null;
        if (mTreeObserver != null && mGlobalFocusChangeListener != null && mTreeObserver.isAlive()) {
            mTreeObserver.removeOnGlobalFocusChangeListener(mGlobalFocusChangeListener);
        }
        mTreeObserver = null;
        mGlobalFocusChangeListener = null;
        if (mEditLastKeyboardTypeArray != null) {
            mEditLastKeyboardTypeArray.clear();
        }
        if (mEditMap != null) {
            mEditMap.clear();
        }
        if (mIdCardEditMap != null) {
            mIdCardEditMap.clear();
        }
        mVibrator = null;
        if (mSafeHandler != null) {
            mSafeHandler.removeCallbacksAndMessages(null);
        }
    }
}
