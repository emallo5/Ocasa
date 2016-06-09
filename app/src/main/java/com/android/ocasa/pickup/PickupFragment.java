package com.android.ocasa.pickup;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.ocasa.R;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.pickup.adapter.PickupItemsPagerAdapter;
import com.android.ocasa.pickup.loader.PickupFormTaskLoader;
import com.android.ocasa.pickup.scanner.ScannerActivity;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.FormTextFieldView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Emiliano Mallo on 21/04/16.
 */
public class PickupFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<FormViewModel>, OnLoadItemsCallback{

    static final String ARG_RECORD_ID = "record_id";

    static final int SCANNER_REQUEST_CODE = 1000;

    public interface PickUpItemsCallback{
        void reloadItems(List<String> codes);
    }

    private LinearLayout container;
    private TabLayout tabLayout;
    private ViewPager itemsPager;

    private FormTextFieldView status;

    private Button add;

    private Set<String> codes;

    public static PickupFragment newInstance(long recordId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECORD_ID, recordId);

        PickupFragment fragment = new PickupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getLoaderManager().initLoader(0, getArguments(), this);

        codes = new HashSet<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pickup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        status = (FormTextFieldView) view.findViewById(R.id.status);
        status.getLabel().setVisibility(View.GONE);
        try {
            status.setValue("Retirado");
        } catch (FormatException e) {
            e.printStackTrace();
        }
        status.getField().getAction().setVisibility(View.GONE);

        tabLayout = (TabLayout) view.findViewById(R.id.appbartabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        itemsPager = (ViewPager) view.findViewById(R.id.viewpager);
        add = (Button) view.findViewById(R.id.start_scanner);

        setListeners();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SCANNER_REQUEST_CODE){
                codes.addAll(data.getStringArrayListExtra(ScannerActivity.EXTRA_RESULT_CODES));
                loadItems(codes);
            }
        }
    }

    private void loadItems(Set<String> codes){

        if(itemsPager.getAdapter() == null) {
            PickupItemsPagerAdapter adapter = new PickupItemsPagerAdapter(getChildFragmentManager(),
                    getResources().getStringArray(R.array.pickup_tabs));
            adapter.addFragment(PickupFoundItemsFragment.newInstance("700", new ArrayList<>(codes)));
            adapter.addFragment(PickupNotFoundItemsFragment.newInstance("700", new ArrayList<>(codes)));

            tabLayout.setVisibility(View.VISIBLE);
            itemsPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(itemsPager);
        }else{
            PickupItemsPagerAdapter adapter = (PickupItemsPagerAdapter) itemsPager.getAdapter();
            ((PickUpItemsCallback)adapter.getItem(0)).reloadItems(new ArrayList<String>(codes));
            ((PickUpItemsCallback)adapter.getItem(1)).reloadItems(new ArrayList<String>(codes));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_receipt, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save){
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setListeners(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ScannerActivity.class), SCANNER_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onItemsCountChange(int tab, int count) {
        tabLayout.getTabAt(tab).setText(String.format(getResources().getStringArray(R.array.pickup_tabs)[tab], count));
    }

    @Override
    public Loader<FormViewModel> onCreateLoader(int id, Bundle args) {
        return new PickupFormTaskLoader(getActivity(), args.getLong(ARG_RECORD_ID));
    }

    @Override
    public void onLoadFinished(Loader<FormViewModel> loader, FormViewModel data) {

        setTitle(data.getTitle());
    }

    @Override
    public void onLoaderReset(Loader<FormViewModel> loader) {

    }
}
