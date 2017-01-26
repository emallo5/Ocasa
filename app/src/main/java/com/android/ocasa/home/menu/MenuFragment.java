package com.android.ocasa.home.menu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.R;
import com.android.ocasa.adapter.MenuAdapter;
import com.android.ocasa.adapter.MenuOptionsAdapter;
import com.android.ocasa.core.activity.MenuActivity;
import com.android.ocasa.core.listener.RecyclerItemClickListener;
import com.android.ocasa.home.HomeActivity;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.ApplicationViewModel;
import com.android.ocasa.viewmodel.CategoryViewModel;
import com.android.ocasa.viewmodel.MenuViewModel;
import com.android.ocasa.viewmodel.OptionViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.codika.androidmvp.fragment.BaseMvpFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 14/01/16.
 */
public class MenuFragment extends BaseMvpFragment<MenuView, MenuPresenter> implements MenuActivity.MenuListener, MenuView{

    private TextView appName;
    private ImageView appsOpenButton;
    private ListView list;
    private LinearLayout appsContainer;
    private ImageView appsCloseButton;
    private RecyclerView appList;

    private MenuViewModel menu;

    private ApplicationViewModel currentApplication;

    private OnMenuItemClickListener callback;

    public interface OnMenuItemClickListener{
        void onTableClick(Table table);
        void onActionClick(Action action);
        void onOptionClick(OptionViewModel option);
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

        list = (ListView) view.findViewById(R.id.list);

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MenuOptionsAdapter adapter = (MenuOptionsAdapter) list.getAdapter();

                callback.onOptionClick((OptionViewModel) list.getItemAtPosition(position));
//                if (adapter.getItemViewType(position) == 1) {
//                    callback.onTableClick((Table) list.getItemAtPosition(position));
//                }else if(adapter.getItemViewType(position) == 2){
//                    callback.onActionClick((Action) list.getItemAtPosition(position));
//                }
            }
        });

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
                currentApplication = menu.getApplications().get(position);
                refreshMenuOptions();
            }
        }));

    }

    @Override
    public void onResume() {
        super.onResume();
        if(menu == null)
            getPresenter().menu();
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
    public MenuView getMvpView() {
        return this;
    }

    @Override
    public Loader<MenuPresenter> getPresenterLoader() {
        return new MenuLoader(getActivity());
    }

    @Override
    public void onMenuLoadSuccess(MenuViewModel menu) {
        if(menu.getApplications().isEmpty())
            return;

        this.menu = menu;

        currentApplication = menu.getApplications().get(0);
        refreshMenuOptions();

        appList.setAdapter(new MenuAdapter(menu.getApplications()));

        CategoryViewModel category = menu.getApplications().get(0).getCategories().get(0);

        List<OptionViewModel> options = category.getOptions();

        if(options.size() > 0){
            callback.onOptionClick(options.get(0));
        }

//        ((HomeActivity)getActivity()).openMenu();
//
//        if(tables.size() > 0){
//            callback.onTableClick(tables.get(0));
//        }else{
//            callback.onActionClick(new ArrayList<>(category.getActions()).get(0));
//        }
    }

    private void refreshMenuOptions() {

        String versionName = BuildConfig.VERSION_NAME;
        appName.setText(currentApplication.getTitle() + " v" + versionName);

        list.setAdapter(new MenuOptionsAdapter(currentApplication.getCategories()));
    }

    @Override
    public void onMenuClosed() {
        appsContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMenuOpened() {

    }
}
