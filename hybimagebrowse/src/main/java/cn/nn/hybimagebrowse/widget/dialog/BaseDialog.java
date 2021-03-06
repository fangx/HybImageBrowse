package cn.nn.hybimagebrowse.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * **************************
 * Class:       BaseDialog
 * Author:      fangx
 * Date:        16/11/4
 * Description:
 * ***************************
 */
public class BaseDialog extends Dialog {

    /**
     * TAG(日志)
     */
    protected String TAG;
    /**
     * context(上下文)
     */
    protected Context mContext;
    protected Context context;
    /**
     * (DisplayMetrics)设备密度
     */
    protected DisplayMetrics dm;
    /**
     * enable dismiss outside dialog(设置点击对话框以外区域,是否dismiss)
     */
    protected boolean cancel;
    /**
     * dialog width scale(宽度比例)
     */
    protected float widthScale = 1;
    /**
     * dialog height scale(高度比例)
     */
    protected float heightScale;
    /**
     * top container(最上层容器)
     */
    protected LinearLayout ll_top;
    /**
     * container to control dialog height(用于控制对话框高度)
     */
    protected LinearLayout ll_control_height;
    /**
     * is showAnim running(显示动画是否正在执行)
     */
    private boolean isShowAnim;
    /**
     * is DismissAnim running(消失动画是否正在执行)
     */
    private boolean isDismissAnim;
    /**
     * max height(最大高度)
     */
    protected float maxHeight;

    /**
     * @param context
     */
    public BaseDialog(Context context) {
        super(context);
        setDialogTheme();
        this.context = context;
        this.mContext = context;
        this.TAG = this.getClass().getSimpleName();
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    }

    /**
     * when dailog attached to window,set dialog width and height and show anim
     * (当dailog依附在window上,设置对话框宽高以及显示动画)
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        this.cancel = cancel;
        super.setCanceledOnTouchOutside(cancel);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void dismiss() {
        superDismiss();
    }

    /**
     * dismiss without anim(无动画dismiss)
     */
    public void superDismiss() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity != null && !activity.isFinishing()) {
                try {
                    super.dismiss();
                } catch (Exception e) {

                }
            }
        } else {
            try {
                super.dismiss();
            } catch (Exception e) {

            }
        }

    }

    /**
     * dialog anim by styles(动画弹出对话框,style动画资源)
     *
     * @param animStyle
     */
    public void show(int animStyle) {
        Window window = getWindow();
        window.setWindowAnimations(animStyle);
        show();
    }

    /**
     * set window dim or not(设置背景是否昏暗)
     *
     * @param isDimEnabled
     * @return BaseDialog
     */
    public BaseDialog dimEnabled(boolean isDimEnabled) {
        if (isDimEnabled) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        return this;
    }

    /**
     * set dialog width scale:0-1(设置对话框宽度,占屏幕宽的比例0-1)
     *
     * @param widthScale
     * @return BaseDialog
     */
    public BaseDialog widthScale(float widthScale) {
        this.widthScale = widthScale;
        return this;
    }

    /**
     * set dialog height scale:0-1(设置对话框高度,占屏幕宽的比例0-1)
     *
     * @param heightScale
     * @return BaseDialog
     */
    public BaseDialog heightScale(float heightScale) {
        this.heightScale = heightScale;
        return this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isDismissAnim || isShowAnim) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (isDismissAnim || isShowAnim) {
            return;
        }
        super.onBackPressed();
    }

}