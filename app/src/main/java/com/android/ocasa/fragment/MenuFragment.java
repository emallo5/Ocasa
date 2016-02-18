package com.android.ocasa.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.adapter.MenuAdapter;
import com.android.ocasa.adapter.MenuOptionsAdapter;
import com.android.ocasa.core.activity.MenuActivity;
import com.android.ocasa.core.adapter.AdapterItem;
import com.android.ocasa.core.listener.RecyclerItemClickListener;
import com.android.ocasa.loader.MenuTaskLoader;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 14/01/16.
 */
public class MenuFragment extends Fragment implements MenuActivity.MenuListener, LoaderManager.LoaderCallbacks<List<Application>>{

    private TextView appName;
    private ImageView appsOpenButton;
    private RecyclerView list;
    private LinearLayout appsContainer;
    private ImageView appsCloseButton;
    private RecyclerView appList;

    private List<Application> applications;

    private Application currentApplication;

    private OnMenuItemClickListener callback;

    public interface OnMenuItemClickListener{
        public void onItemClick(Table table);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnMenuItemClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.getClass().getName() + " must implements OnMenuItemClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
        setListeners();
    }

    private void initControls(View view){

        appName = (TextView) view.findViewById(R.id.app_name);

        appsOpenButton = (ImageView) view.findViewById(R.id.open_app);

        list = (RecyclerView) view.findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        appsContainer = (LinearLayout) view.findViewById(R.id.apps_container);

        appsCloseButton = (ImageView) view.findViewById(R.id.close_app);

        appList = (RecyclerView) view.findViewById(R.id.app_list);
        appList.setHasFixedSize(true);
        appList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    private void setListeners(){

        appsOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppsList();
            }
        });

        list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {

                MenuOptionsAdapter adapter = (MenuOptionsAdapter) list.getAdapter();
                AdapterItem item = adapter.getItem(position);

                if (item.getType() == 2) {
                    callback.onItemClick((Table) item.getData());
                }
            }
        }));

        appsCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAppsList();
            }
        });

        appList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                hideAppList();
                currentApplication = applications.get(position);
                refreshMenuOptions();
            }
        }));

    }

    private void showAppsList(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int cx = appsContainer.getRight();
            int cy = appsContainer.getTop();

            int finalRadius = (int) Math.hypot(appsContainer.getWidth(), appsContainer.getHeight());

            AnimatorSet showSet = new AnimatorSet();

            Animator reveal =
                    ViewAnimationUtils.createCircularReveal(appsContainer, cx, cy, 0, finalRadius);

            ObjectAnimator viewAnimator = ObjectAnimator.ofFloat(appList, View.Y, appList.getTop() + 100f, appList.getTop());
            viewAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            showSet.playTogether(reveal, viewAnimator);

            appsContainer.setVisibility(View.VISIBLE);
            showSet.start();
        }else{
            appsContainer.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_in));

            appsContainer.setVisibility(View.VISIBLE);
        }
    }

    private void hideAppList(){
        appsContainer.startAnimation(AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.fade_out));

        appsContainer.setVisibility(View.INVISIBLE);
    }

    private void closeAppsList(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int cx = appsContainer.getRight();
            int cy = appsContainer.getTop();

            int initialRadius = (int) Math.hypot(appsContainer.getWidth(), appsContainer.getHeight());

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(appsContainer, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    appsContainer.setVisibility(View.INVISIBLE);
                }
            });

            anim.start();
        }else{
            appsContainer.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_out));

            appsContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public Loader<List<Application>> onCreateLoader(int i, Bundle bundle) {
        return new MenuTaskLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Application>> loader, List<Application> applications) {

        if(applications.isEmpty())
            return;

        this.applications = applications;

        currentApplication = applications.get(0);
        refreshMenuOptions();

        appList.setAdapter(new MenuAdapter(applications));
    }

    @Override
    public void onLoaderReset(Loader<List<Application>> loader) {

    }

    private void refreshMenuOptions(){

        appName.setText(currentApplication.getName());

        list.setAdapter(new MenuOptionsAdapter(new ArrayList<>(currentApplication.getCategories())));
    }

    @Override
    public void onMenuClosed() {
        appsContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMenuOpened() {

    }
}
