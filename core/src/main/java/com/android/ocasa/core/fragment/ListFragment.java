package com.android.ocasa.core.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.android.ocasa.core.R;
import com.android.ocasa.core.listener.RecyclerItemClickListener;

/**
 * Created by ignacio on 11/01/16.
 */
public class ListFragment extends Fragment {

    private RecyclerView mList;
    private ProgressBar mProgress;

    private RecyclerItemClickListener.OnItemClickListener onClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, long id) {
            onListItemClick(view, position, id);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
    }

    private void initControls(View view){

        mList = (RecyclerView) view.findViewById(R.id.list);
        mList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onClickListener));
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
    }

    public RecyclerView getRecyclerView(){
        return mList;
    }

    public void onListItemClick(View v, int position, long id) {
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
