package com.android.ocasa;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.manifest.BroadcastReceiverData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 26/01/16.
 */
public class CustomTestRunner extends RobolectricGradleTestRunner {

    public CustomTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        AndroidManifest manifest = super.getAppManifest(config);
        List<BroadcastReceiverData> broadcastReceivers = manifest.getBroadcastReceivers();
        List<BroadcastReceiverData> removeList = new ArrayList<>();
        for(BroadcastReceiverData receiverData : broadcastReceivers) {
            removeList.add(receiverData);
        }
        broadcastReceivers.removeAll(removeList);
        return  manifest;
    }
}
