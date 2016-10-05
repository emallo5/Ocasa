package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Application;
import com.android.ocasa.viewmodel.ApplicationViewModel;

import java.util.List;

/**
 * Created by ignacio on 18/01/16.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{

    private List<ApplicationViewModel> applications;

    public MenuAdapter(List<ApplicationViewModel> applications) {
        this.applications = applications;
    }

    @Override
    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(MenuHolder holder, int position) {

        ApplicationViewModel app = applications.get(position);

        holder.name.setText(app.getTitle());
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public class MenuHolder extends RecyclerView.ViewHolder{

        TextView name;

        public MenuHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
