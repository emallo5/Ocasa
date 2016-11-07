package com.android.ocasa.util;

import android.content.Context;
import android.support.v4.app.DialogFragment;

/**
 * Created by ignacio on 19/09/16.
 */
public abstract class FieldDetailDialogFragment extends DialogFragment implements AlertDialogFragment.OnAlertClickListener{

    static final String ARG_FIELD_NAME = "field_name";
    static final String ARG_FIELD_TAG = "field_tag";

    private OnFieldSaveListener onFieldSaveListener;

    public interface OnFieldSaveListener{
        void onSave(String tag, String value);
        void onError();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onFieldSaveListener = (OnFieldSaveListener) getParentFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(context.getClass().getSimpleName() + " must implements OnFieldSaveListener");
        }
    }

    public OnFieldSaveListener getOnFieldSaveListener() {
        return onFieldSaveListener;
    }

    public void showSaveAlert(){
        AlertDialogFragment.newInstance("Guardar", "").show(getChildFragmentManager(), "Alert");
    }

    @Override
    public void onPosiviteClick(String tag) {
        onFieldSaveListener.onSave(getArguments().getString(ARG_FIELD_TAG), getValue());
        dismiss();
    }

    @Override
    public void onNeutralClick(String tag) {

    }

    @Override
    public void onNegativeClick(String tag) {

    }

    public abstract String getValue();
}
