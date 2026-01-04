package com.project.laundryappui.menu.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.laundryappui.R;
import com.project.laundryappui.menu.home.model.HomeModel;

import java.util.List;

/**
 * Adapter hiển thị danh sách cửa hàng trên map
 */
public class MapsAdapter extends RecyclerView.Adapter<MapsAdapter.ViewHolder> {
    private List<HomeModel> storeList;
    private OnStoreClickListener listener;

    public interface OnStoreClickListener {
        void onStoreClick(HomeModel store);
    }

    public MapsAdapter(List<HomeModel> storeList, OnStoreClickListener listener) {
        this.storeList = storeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_maps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeModel store = storeList.get(position);

        holder.textName.setText(store.getName());
        holder.textRating.setText(String.format("%.1f", store.getRating()));
        holder.textLocation.setText(store.getDistrict() + " - " + store.getLocation());

        // Click listener
        holder.container.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStoreClick(store);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList != null ? storeList.size() : 0;
    }

    /**
     * Cập nhật danh sách cửa hàng
     */
    public void updateStores(List<HomeModel> newStores) {
        this.storeList = newStores;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        TextView textName;
        TextView textRating;
        TextView textLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_recommended);
            textName = itemView.findViewById(R.id.item_recommended_name);
            textRating = itemView.findViewById(R.id.item_recommended_price);
            textLocation = itemView.findViewById(R.id.item_recommended_location);
        }
    }
}

