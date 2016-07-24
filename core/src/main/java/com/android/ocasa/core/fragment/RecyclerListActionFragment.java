package com.android.ocasa.core.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.android.ocasa.core.R;

/**
 * Created by ignacio on 09/06/16.
 */
public class RecyclerListActionFragment extends BaseFragment {

    private RecyclerView mList;
    private ProgressBar mProgress;
    private FloatingActionButton mFloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_action, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
    }

    private void initControls(View view){

        mList = (RecyclerView) view.findViewById(R.id.list);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.action);
    }

    public FloatingActionButton getAction(){
        return mFloatingActionButton;
    }

    public RecyclerView getRecyclerView(){
        return mList;
    }

    public RecyclerView.Adapter getAdapter(){
        return mList.getAdapter();
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mList.setAdapter(adapter);
    }

    public void setListShown(boolean shown) {
        if (shown) {
            mProgress.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_out));
            mList.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_in));

            mProgress.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
        } else {
            mProgress.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_in));
            mList.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_out));

            mProgress.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
        }
    }
}
