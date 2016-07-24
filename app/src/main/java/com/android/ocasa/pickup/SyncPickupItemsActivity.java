package com.android.ocasa.pickup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.android.ocasa.R;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.service.RecordService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Emiliano Mallo on 25/04/16.
 */
public class SyncPickupItemsActivity extends BaseActivity {

    public static final String EXTRA_RECORD_ID = "record_id";

    private SyncReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_pickup_items);

        Uri uri = Uri.parse("asset:///animacion_loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();

        SimpleDraweeView progress = (SimpleDraweeView) findViewById(R.id.progress);
        progress.setController(controller);

        receiver = new SyncReceiver();
        filter = new IntentFilter();
        filter.addAction(RecordService.RECORD_SYNC_FINISHED_ACTION);
        filter.addAction(RecordService.RECORD_SYNC_ERROR_ACTION);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    public class SyncReceiver extends BroadcastReceiver {

        public SyncReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equalsIgnoreCase(RecordService.RECORD_SYNC_FINISHED_ACTION)) {
                Intent pickUpIntent = new Intent(context, PickupActivity.class);
                pickUpIntent.putExtra(PickupActivity.EXTRA_RECORD_ID, getIntent().getLongExtra(EXTRA_RECORD_ID, 0));
                startNewActivity(pickUpIntent);
                finish();
            }else if(intent.getAction().equalsIgnoreCase(RecordService.RECORD_SYNC_ERROR_ACTION)){
                finish();
            }
        }
    }

}
