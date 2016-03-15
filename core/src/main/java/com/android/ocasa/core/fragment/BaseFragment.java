package com.android.ocasa.core.fragment;

import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.core.activity.MenuActivity;

/**
 * Created by ignacio on 21/01/16.
 */
public class BaseFragment extends Fragment{


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);

        if (Build.VERSION.SDK_INT >= 11) {
            if (animation == null && nextAnim != 0) {
                animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            }

            if (animation != null) {
                getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        getView().setLayerType(View.LAYER_TYPE_NONE, null);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                });
            }
        }

        return animation;
    }

    public void setTitle(String title){
        ((BarActivity) getActivity()).getToolbar().setTitle(title);
    }

    public void showFragment(String hideTag, Fragment fragment, String shoTag){

        if(getFragmentManager().findFragmentByTag(shoTag) != null)
            return;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(com.android.ocasa.core.R.anim.slide_in_left,
                com.android.ocasa.core.R.anim.slide_out_left,
                com.android.ocasa.core.R.anim.slide_in_right,
                com.android.ocasa.core.R.anim.slide_out_right);
        transaction.hide(getFragmentManager().findFragmentByTag(hideTag));
        transaction.add(com.android.ocasa.core.R.id.container, fragment, shoTag);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void showDialog(String tag, DialogFragment dialog){

        if(getChildFragmentManager().findFragmentByTag(tag) != null)
            return;

        dialog.show(getChildFragmentManager(), tag);
    }
}
