package com.android.ocasa.receipt.item.available;

import android.content.Context;

import com.android.ocasa.core.TableLoader;
import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 21/07/16.
 */
public class AvailableItemsLoader extends TableLoader {

    public AvailableItemsLoader(Context context) {
        super(context);
    }

    @Override
    public AvailableItemsPresenter getPresenter() {
        return new AvailableItemsPresenter();
    }
}
