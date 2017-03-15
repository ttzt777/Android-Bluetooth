package com.tt.android_ble.ui.anim;


import android.support.v4.app.FragmentTransaction;

import com.tt.android_ble.R;

/**
 * -------------------------------------------------
 * Description：fragment动画封装
 * Author：TT
 * Since：2017/3/14
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public enum Transitions {

    NONE(0, 0, 0, 0),
    ENTER_FORM_RIGHT(R.anim.anim_right_in, R.anim.anim_left_out, R.anim.anim_left_in, R.anim.anim_right_out),
    FADE(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out);

    private final static int ANIM_NOT_DEFINED = 0;

    int enter;
    int exit;
    int popEnter;
    int popExit;

    Transitions(int enter, int exit, int popEnter, int popExit) {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
    }

    /**
     * Fragment进出动画设置
     * @param ft 事务
     * @param transition 动画设置枚举封装
     */
    public static void applyTransition(FragmentTransaction ft, Transitions transition) {
        if (ft == null || transition == null) {
            return;
        }

        if (checkTransitionHasPopAnim(transition)) {
            ft.setCustomAnimations(transition.enter, transition.exit, transition.popEnter, transition.popExit);
        } else {
            ft.setCustomAnimations(transition.enter, transition.exit);
        }
    }

    private static boolean checkTransitionHasPopAnim(Transitions transition) {
        if (transition.popEnter != ANIM_NOT_DEFINED && transition.popExit != ANIM_NOT_DEFINED) {
            return true;
        }

        return false;
    }
}
