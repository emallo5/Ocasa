package com.android.ocasa.util;

/**
 * Created by leandro on 14/3/17.
 */

public class ReceiptCounterHelper {

    private static final String AVAILABLES = "availables";
    private static final String COMPLETED_SYNC = "completed_sync";
    private static final String COMPLETED = "completed";
    private static final String TOTAL = "total";

    private static ReceiptCounterHelper instance;

    public static ReceiptCounterHelper getInstance() {
        if (instance == null)
            instance = new ReceiptCounterHelper();
        return instance;
    }

    public int getAvailableItemsCount() {
        String count = ConfigHelper.getInstance().ReadConfig(AVAILABLES);
        if (count.isEmpty()) return 0;
        return Integer.valueOf(count);
    }

    public void setAvailableItems(int count) {
        ConfigHelper.getInstance().WriteConfig(AVAILABLES, String.valueOf(count));
    }

    public int getCompletedItemsCount() {
        String count = ConfigHelper.getInstance().ReadConfig(COMPLETED);
        if (count.isEmpty()) return 0;
        return Integer.valueOf(count);
    }

    public void setCompletedItems(int count) {
        ConfigHelper.getInstance().WriteConfig(COMPLETED, String.valueOf(count));
    }

    public int getCompletedSyncItemsCount() {
        String count = ConfigHelper.getInstance().ReadConfig(COMPLETED_SYNC);
        if (count.isEmpty()) return 0;
        return Integer.valueOf(count);
    }

    public void setCompletedSyncItems(int count) {
        ConfigHelper.getInstance().WriteConfig(COMPLETED_SYNC, String.valueOf(count));
    }

    public long getTotalItemsCount() {
        String count = ConfigHelper.getInstance().ReadConfig(TOTAL);
        if (count.isEmpty()) return 0;
        return Long.valueOf(count);
    }

    public void setTotalItems(long count) {
        ConfigHelper.getInstance().WriteConfig(TOTAL, String.valueOf(count));
    }
}