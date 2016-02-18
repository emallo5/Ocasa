package com.android.ocasa.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.ocasa.R;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.service.MenuService;
import com.android.ocasa.sync.SyncService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by ignacio on 21/01/16.
 */
public class SyncFragment extends BaseFragment {

    private SyncCallback callback;

    private SyncReceiver receiver;
    private IntentFilter filter;

    public interface SyncCallback{
        public void onSync();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (SyncCallback) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement LoginCallback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sync, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Uri uri = Uri.parse("asset:///animacion_loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();

        SimpleDraweeView progress = (SimpleDraweeView) view.findViewById(R.id.progress);
        progress.setController(controller);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        receiver = new SyncReceiver();
        filter = new IntentFilter();
        filter.addAction(MenuService.MENU_SYNC_FINISHED_ACTION);
        filter.addAction(MenuService.MENU_SYNC_ERROR_ACTION);

        sync();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    private void sync(){
        Intent intent = new Intent(getActivity(), SyncService.class);
        intent.putExtra(SyncService.EXTRA_SYNC, SyncService.MENU_SYNC);
        getActivity().startService(intent);
    }

    public class SyncReceiver extends BroadcastReceiver {

        public SyncReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equalsIgnoreCase(MenuService.MENU_SYNC_FINISHED_ACTION))
                callback.onSync();
            else if(intent.getAction().equalsIgnoreCase(MenuService.MENU_SYNC_ERROR_ACTION)){
                callback.onSync();
            }
        }
    }
}
