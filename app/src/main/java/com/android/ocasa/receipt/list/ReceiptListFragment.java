package com.android.ocasa.receipt.list;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.cache.dao.FieldDAO;
import com.android.ocasa.cache.dao.ReceiptDAO;
import com.android.ocasa.cache.dao.RecordDAO;
import com.android.ocasa.event.CloseReceiptEvent;
import com.android.ocasa.home.HomeActivity;
import com.android.ocasa.httpmodel.ControlResponse;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.receipt.header.EditHeaderReceiptActivity;
import com.android.ocasa.adapter.ReceiptAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.receipt.base.BaseReceiptActivity;
import com.android.ocasa.receipt.detail.DetailReceiptActivity;
import com.android.ocasa.receipt.edit.EditReceiptActivity;
import com.android.ocasa.receipt.header.EditHeaderReceiptPresenter;
import com.android.ocasa.service.ReceiptService;
import com.android.ocasa.util.AlertDialogFragment;
import com.android.ocasa.util.ProgressDialogFragment;
import com.android.ocasa.util.ReceiptCounterHelper;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.ReceiptCellViewModel;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.codika.androidmvp.fragment.BaseMvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReceiptListFragment extends BaseMvpFragment<ReceiptListView, ReceiptListPresenter> implements ReceiptListView,
AlertDialogFragment.OnAlertClickListener{

    static final String ARG_ACTION_ID = "action_id";

    private ListView receiptList;
    private SwipeRefreshLayout swiperefresh;
    private FloatingActionButton addButton;
    private Button btnRefresh;
    private TextView tvCountDone;
    private TextView tvCountUndone;
    private TextView tvCountNews;
    private TextView tvTotalItems;
    private TextView tvTotalPending;

    public static ReceiptListFragment newInstance(String actionId) {

        Bundle args = new Bundle();
        args.putString(ARG_ACTION_ID, actionId);

        ReceiptListFragment fragment = new ReceiptListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
        setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
        if (swiperefresh != null) swiperefresh.setRefreshing(true);

        // una vez cargados los items, verifico si tengo para descargar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getPresenter() != null)
                    getPresenter().control();
            }
        }, 1000);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private long receiptId;

    @Subscribe
    public void onCloseReceipt(CloseReceiptEvent event){
        receiptId = event.getReceiptId();

        AlertDialogFragment.newInstance("Contabilizar","¿Esta seguro de contabilizar el comprobante? No podra volver a modificarlo").show(getChildFragmentManager(), "CloseConfirmation");

    }

    private void initControls(View view){
        receiptList = (ListView) view.findViewById(android.R.id.list);
        addButton = (FloatingActionButton) view.findViewById(R.id.add);
        btnRefresh = (Button) view.findViewById(R.id.iv_refresh);
        tvCountDone = (TextView) view.findViewById(R.id.tv_pod_count_done);
        tvCountUndone = (TextView) view.findViewById(R.id.tv_pod_count_undone);
        tvCountNews = (TextView) view.findViewById(R.id.tv_pod_count_news);
        tvTotalItems = (TextView) view.findViewById(R.id.tv_total_items);
        tvTotalPending = (TextView) view.findViewById(R.id.tv_pending);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
    }

    private void setListeners() {

        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.ocasaLightBlue));
        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
                        swiperefresh.setRefreshing(true);
                    }
                }
        );

        receiptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ReceiptCellViewModel receiptViewModel = (ReceiptCellViewModel) receiptList.getItemAtPosition(i);
                Intent intent;
//                if (receiptViewModel.isOpen()) {
//                    intent = new Intent(getActivity(), EditReceiptActivity.class);
//                } else {
                    intent = new Intent(getActivity(), DetailReceiptActivity.class);
//                }
                intent.putExtra(BaseReceiptActivity.EXTRA_RECEIPT_ID, l);
                ((BaseActivity) getActivity()).startNewActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (swiperefresh.isRefreshing()) return;
                getPresenter().load(getArguments().getString(ARG_ACTION_ID));
//                Intent intent = new Intent(getActivity(), EditHeaderReceiptActivity.class);
//                intent.putExtra(EditHeaderReceiptActivity.EXTRA_ACTION_ID, getArguments().getString(ARG_ACTION_ID));
//                ((BaseActivity) getActivity()).startNewActivity(intent);
            }
        });
    }

    @Override
    public void onFormSuccess(FormViewModel formViewModel) {
        Receipt receipt = new Receipt();
        receipt.setNumber((int) (Math.random() * 1000));

        Action action = new Action();
        action.setId(getArguments().getString(ARG_ACTION_ID));
        receipt.setAction(action);

        List<Field> fields = new ArrayList<>();

        for (FieldViewModel fieldViewModel : formViewModel.getFields()) {
            Field field = new Field();

            Column column = new Column();
            column.setId(fieldViewModel.getTag());

            field.setColumn(column);
            field.setValue(fieldViewModel.getValue());
            field.setReceipt(receipt);

            fields.add(field);
        }

        receipt.setHeaderValues(fields);

        new ReceiptDAO(getActivity()).save(receipt);
        new FieldDAO(getActivity()).save(receipt.getHeaderValues());

        Intent intent = new Intent(getActivity(), EditReceiptActivity.class);
        intent.putExtra(EditReceiptActivity.EXTRA_RECEIPT_ID, receipt.getId());

        ((BaseActivity)getActivity()).startNewActivity(intent);
    }

    public void setTitle(String title){
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public ReceiptListView getMvpView() {
        return this;
    }

    @Override
    public Loader<ReceiptListPresenter> getPresenterLoader() {
        return new ReceiptListLoader(getActivity());
    }

    @Override
    public void onReceiptsLoadSuccess (ReceiptTableViewModel table) {
        if (table == null)
            return;

        setTitle("Listado " + table.getName());
        receiptList.setAdapter(new ReceiptAdapter(table.getReceipts()));
        updateCounters(table.getReceipts());

        swiperefresh.setRefreshing(false);
    }

    private void updateCounters (List<ReceiptCellViewModel> receipts) {
        int opened = 0;
        int closed = 0;
        int news = 0;

        long total = new RecordDAO(getContext()).findForActionId(getArguments().getString(ARG_ACTION_ID));

        for (ReceiptCellViewModel r : receipts) {
            if (r.isOpen()) opened++;
            else closed++;
            if (r.isCreated()) news++;
        }

        tvCountDone.setText(": " + closed);
        tvCountUndone.setText(": " + opened);
        tvCountNews.setText(": " + news);
        tvTotalPending.setText(": " + (total-closed-opened));
        tvTotalItems.setText(": " + total);

        ReceiptCounterHelper.getInstance().setCompletedItems(opened);
        ReceiptCounterHelper.getInstance().setCompletedSyncItems(closed);

        long oldTotal = ReceiptCounterHelper.getInstance().getTotalItemsCount();
        if (oldTotal != total)
            createLocalNotification(total - oldTotal);
        ReceiptCounterHelper.getInstance().setTotalItems(total);
    }

    private void createLocalNotification(long news) {

        if (news <= 0) return;

        NotificationCompat.Builder b = new NotificationCompat.Builder(getContext());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_arrow_statusbar_24dp)
                .setContentTitle("OCASA")
                .setContentText("Tiene " + news + (news > 1 ? " paradas nuevas!" : " parada nueva!"))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    private void syncReceipts() {

        for (int i=0; i < ((ReceiptAdapter) receiptList.getAdapter()).getReceipts().size(); i++) {

            final ReceiptCellViewModel receipt = ((ReceiptAdapter) receiptList.getAdapter()).getReceipts().get(i);
            if (receipt.isOpen()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPresenter().close(receipt.getId());
                        }
                    }, i * 1000);
//                }
            }
        }
    }

    @Override
    public void onCloseReceiptSuccess() {
        hideProgress();
        getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
    }

    @Override
    public void onControlSynResponse(ControlResponse response) {
        if (!response.isStatus()) return;

        ProgressDialogFragment progress = ProgressDialogFragment.newInstance("Descargando actualización de ruta...");
        progress.setCancelable(false);
        progress.show(getChildFragmentManager(), "down");
        getPresenter().sync();
    }

    @Override
    public void onSyncSuccess() {
        DialogFragment dialog = (DialogFragment) getChildFragmentManager().findFragmentByTag("down");
        if (dialog != null) dialog.dismiss();

        getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
        swiperefresh.setRefreshing(true);

        Toast.makeText(getContext(), "Ruta actualizada!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSyncError() {
        DialogFragment dialog = (DialogFragment) getChildFragmentManager().findFragmentByTag("down");
        if (dialog != null) dialog.dismiss();
        Toast.makeText(getContext(), "Error al actualizar, intente manualmente Sincronizar!", Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        ProgressDialogFragment.newInstance("Sincronizando...").show(getChildFragmentManager(), "Progress");
    }

    public void hideProgress() {
        DialogFragment dialog = (DialogFragment) getChildFragmentManager().findFragmentByTag("Progress");
        if (dialog != null) dialog.dismiss();
    }

    @Override
    public void onPosiviteClick(String tag) {

        ReceiptService service = new ReceiptService();
        Receipt receipt = service.findReceiptById(getContext(), receiptId);

        if (receipt.isOpen()) {
            getPresenter().close(receiptId);
            showProgress();
        } else
            getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
    }

    @Override
    public void onNeutralClick(String tag) {

    }

    @Override
    public void onNegativeClick(String tag) {

    }
}
