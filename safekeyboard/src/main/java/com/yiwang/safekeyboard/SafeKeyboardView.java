package com.yiwang.safekeyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import static com.yiwang.safekeyboard.Constants.DELETE_KEY_BOARD;
import static com.yiwang.safekeyboard.Constants.LETTER_KEY_BOARD;
import static com.yiwang.safekeyboard.Constants.NUMBER_DELETE_KEY_BOARD;

public class SafeKeyboardView extends KeyboardView {
    private Context mContext;
    private boolean mIsCap;
    private boolean mIsCapLock;
    private boolean mEnableVibrate;
    private Drawable mDelDrawable;
    private Drawable mLowDrawable;
    private Drawable mUpDrawable;
    private Keyboard mLastKeyboard;
    /**
     * 按键的宽高至少是图标宽高的倍数
     */
    private static final int ICON2KEY = 2;

    // 键盘的一些自定义属性
    private boolean mRandomDigit;    // 数字随机
    private final static boolean DIGIT_RANDOM = false;
    //    private boolean onlyIdCard;     // 仅显示 身份证 键盘
//    private final static boolean ONLY_ID_CARD = false;
    private boolean mRememberLastType;     // 仅显示 身份证 键盘
    private final static boolean REM_LAST_TYPE = true;
    private final static boolean DEFAULT_ENABLE_VIBRATE = false;

    public SafeKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        this.mContext = context;

        initAttrs(context, attrs, 0);
    }

    public SafeKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        this.mContext = context;

        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SafeKeyboardView, defStyleAttr, 0);
            mRandomDigit = array.getBoolean(R.styleable.SafeKeyboardView_random_digit, DIGIT_RANDOM);
            // onlyIdCard = array.getBoolean(R.styleable.SafeKeyboardView_only_id_card, ONLY_ID_CARD);
            mRememberLastType = array.getBoolean(R.styleable.SafeKeyboardView_remember_last_type, REM_LAST_TYPE);
            mEnableVibrate = array.getBoolean(R.styleable.SafeKeyboardView_enable_vibrate, DEFAULT_ENABLE_VIBRATE);
            array.recycle();
        }
    }

    public void setmRememberLastType(boolean remember) {
        mRememberLastType = remember;
    }

    private void init(Context context) {
        this.mIsCap = false;
        this.mIsCapLock = false;
        // 默认三种图标
        this.mDelDrawable = context.getDrawable(R.drawable.icon_del);
        this.mLowDrawable = context.getDrawable(R.drawable.icon_capital_default);
        this.mUpDrawable = context.getDrawable(R.drawable.icon_capital_selected);
//        this.upDrawableLock = upDrawable;
        this.mLastKeyboard = null;
    }

    public boolean ismRandomDigit() {
        return mRandomDigit;
    }

//    public boolean isOnlyIdCard() {
//        return onlyIdCard;
//    }

    public boolean ismRememberLastType() {
        return mRememberLastType;
    }

    public boolean isVibrateEnable() {
        return mEnableVibrate;
    }

    public void enableVibrate(boolean enableVibrate) {
        this.mEnableVibrate = enableVibrate;
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        super.setKeyboard(keyboard);
        this.mLastKeyboard = keyboard;
    }

    public Keyboard getmLastKeyboard() {
        return mLastKeyboard;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void drawNormalKey(Canvas canvas, Keyboard.Key key) {
        drawKeyBackground(R.drawable.keyboard_key_bg, canvas, key);
//        int color = Color.BLACK;
//        drawTextAndIcon(canvas, key, null, Color.BLACK);
    }

    @Override
    public void drawSpecialKey(Canvas canvas, Keyboard.Key key, Paint paint) {
        int color = Color.WHITE;
        if (key.codes[0] == NUMBER_DELETE_KEY_BOARD) {
            drawTextAndIcon(canvas, key, mDelDrawable, color);
        } else if (key.codes[0] == DELETE_KEY_BOARD) {
            drawKeyBackground(R.drawable.keyboard_change, canvas, key);
            drawTextAndIcon(canvas, key, mDelDrawable, color);
        } else if (key.codes[0] == LETTER_KEY_BOARD || key.codes[0] == Constants.SPECIAL_CHARACTERS_KEY_BOARD) {
            drawKeyBackground(R.drawable.keyboard_change, canvas, key);
            drawTextAndIcon(canvas, key, null, color);
        } else if (key.codes[0] == -1) {
            if (mIsCapLock) {
                drawKeyBackground(R.drawable.keyboard_change, canvas, key);
                drawTextAndIcon(canvas, key, mUpDrawable, color);
            } else if (mIsCap) {
                drawKeyBackground(R.drawable.keyboard_change, canvas, key);
                drawTextAndIcon(canvas, key, mUpDrawable, color);
            } else {
                drawKeyBackground(R.drawable.keyboard_change, canvas, key);
                drawTextAndIcon(canvas, key, mLowDrawable, color);
            }

        }
    }

    private void drawKeyBackground(int id, Canvas canvas, Keyboard.Key key) {
        Drawable drawable = mContext.getResources().getDrawable(id);
        int[] state = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            drawable.setState(state);
        }
        drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        drawable.draw(canvas);
    }

    private void drawTextAndIcon(Canvas canvas, Keyboard.Key key, @Nullable Drawable drawable, int color) {
        try {
//            Rect bounds = new Rect();
//            Paint paint = new Paint();
//            paint.setTextAlign(Paint.Align.CENTER);
//            paint.setAntiAlias(true);
//            paint.setColor(color);
//
//            if (key.label != null) {
//                String label = key.label.toString();
//
//                Field field;
//
//                if (label.length() > 1 && key.codes.length < 2) {
//                    int labelTextSize = 0;
//                    try {
//                        field = KeyboardView.class.getDeclaredField(getContext().getString(R.string.mLabelTextSize));
//                        field.setAccessible(true);
//                        labelTextSize = (int) field.get(this);
//                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    paint.setTextSize(labelTextSize);
//                    paint.setTypeface(Typeface.DEFAULT_BOLD);
//                } else {
//                    int keyTextSize = 0;
//                    try {
//                        field = KeyboardView.class.getDeclaredField(getContext().getString(R.string.mLabelTextSize));
//                        field.setAccessible(true);
//                        keyTextSize = (int) field.get(this);
//                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    paint.setTextSize(keyTextSize + 10);
//                    paint.setTypeface(Typeface.DEFAULT);
//                }
//
//                paint.getTextBounds(key.label.toString(), 0, key.label.toString().length(), bounds);
//                canvas.drawText(key.label.toString(), key.x + (1.0f * key.width / 2),
//                        (key.y + 1.0f * key.height / 2) + 1.0f * bounds.height() / 2, paint);
//            }
            if (drawable == null) {
                return;
            }
            // 约定: 最终图标的宽度和高度都需要在按键的宽度和高度的二分之一以内
            // 如果: 图标的实际宽度和高度都在按键的宽度和高度的二分之一以内, 那就不需要变换, 否则就需要等比例缩小
            int iconSizeWidth, iconSizeHeight;
            key.icon = drawable;
            int iconH = px2dip(mContext, key.icon.getIntrinsicHeight());
            int iconW = px2dip(mContext, key.icon.getIntrinsicWidth());
            if (key.width >= (ICON2KEY * iconW) && key.height >= (ICON2KEY * iconH)) {
                //图标的实际宽度和高度都在按键的宽度和高度的二分之一以内, 不需要缩放, 因为图片已经够小或者按键够大
                setIconSize(canvas, key, iconW, iconH);
            } else {
                //图标的实际宽度和高度至少有一个不在按键的宽度或高度的二分之一以内, 需要等比例缩放, 因为此时图标的宽或者高已经超过按键的二分之一
                //需要把超过的那个值设置为按键的二分之一, 另一个等比例缩放
                //不管图标大小是多少, 都以宽度width为标准, 把图标的宽度缩放到和按键一样大, 并同比例缩放高度
                double multi = 1.0 * iconW / key.width;
                int tempIconH = (int) (iconH / multi);
                if (tempIconH <= key.height) {
                    //宽度相等时, 图标的高度小于等于按键的高度, 按照现在的宽度和高度设置图标的最终宽度和高度
                    iconSizeHeight = tempIconH / ICON2KEY;
                    iconSizeWidth = key.width / ICON2KEY;
                } else {
                    //宽度相等时, 图标的高度大于按键的高度, 这时按键放不下图标, 需要重新按照高度缩放
                    double mul = 1.0 * iconH / key.height;
                    int tempIconW = (int) (iconW / mul);
                    iconSizeHeight = key.height / ICON2KEY;
                    iconSizeWidth = tempIconW / ICON2KEY;
                }
                setIconSize(canvas, key, iconSizeWidth, iconSizeHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setIconSize(Canvas canvas, Keyboard.Key key, int iconSizeWidth, int iconSizeHeight) {
        int left = key.x + (key.width - iconSizeWidth) / 2;
        int top = key.y + (key.height - iconSizeHeight) / 2;
        int right = key.x + (key.width + iconSizeWidth) / 2;
        int bottom = key.y + (key.height + iconSizeHeight) / 2;
        key.icon.setBounds(left, top, right, bottom);
        key.icon.draw(canvas);
        key.icon = null;
    }

    public void setmIsCap(boolean mIsCap) {
        this.mIsCap = mIsCap;
    }

    public void setmIsCapLock(boolean isCapLock) {
        this.mIsCapLock = isCapLock;
    }

    public void setmDelDrawable(Drawable mDelDrawable) {
        this.mDelDrawable = mDelDrawable;
    }

    public void setmLowDrawable(Drawable mLowDrawable) {
        this.mLowDrawable = mLowDrawable;
    }

    public void setmUpDrawable(Drawable mUpDrawable) {
        this.mUpDrawable = mUpDrawable;
    }

//    public void setUpDrawableLock(Drawable upDrawableLock) {
//        this.upDrawableLock = upDrawableLock;
//    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
