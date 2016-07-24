package com.android.ocasa.record.create;

import android.content.Context;

import com.android.ocasa.core.FormLoader;
import com.android.ocasa.core.FormPresenter;

/**
 * Created by ignacio on 14/07/16.
 */
public class CreateRecordLoader extends FormLoader {

    public CreateRecordLoader(Context context) {
        super(context);
    }

    @Override
    public FormPresenter getPresenter() {
        return new CreateRecordPresenter();
    }
}
