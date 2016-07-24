package com.android.ocasa.sync;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import com.android.ocasa.R;
import com.android.ocasa.home.HomeActivity;
import com.codika.androidmvp.activity.BaseMvpActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by ignacio on 21/06/16.
 */
public class SyncActivity extends BaseMvpActivity<SyncView, SyncPresenter> implements SyncView{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sync);

        Uri uri = Uri.parse("asset:///animacion_loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();

        SimpleDraweeView progress = (SimpleDraweeView) findViewById(R.id.progress);
        progress.setController(controller);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().sync();
    }

    @Override
    public SyncView getMvpView() {
        return this;
    }

    @Override
    public Loader<SyncPresenter> getPresenterLoader() {
        return new SyncLoader(this);
    }

    @Override
    public void onSyncFinish() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
