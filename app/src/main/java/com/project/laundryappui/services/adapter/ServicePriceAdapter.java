package com.project.laundryappui.services.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.laundryappui.R;
import com.project.laundryappui.services.model.ServiceItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ServicePriceAdapter extends RecyclerView.Adapter<ServicePriceAdapter.ViewHolder> {
    
    private Context context;
    private List<ServiceItem> items;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(ServiceItem item);
        void onAddClick(ServiceItem item);
        void onIncreaseClick(ServiceItem item);
        void onDecreaseClick(ServiceItem item);
        void onRemoveClick(ServiceItem item);
    }
    
    public ServicePriceAdapter(Context context, List<ServiceItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_price, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceItem item = items.get(position);
        
        holder.itemName.setText(item.getName());
        holder.itemTime.setText(item.getEstimatedTime());
        holder.itemPrice.setText(formatPrice(item.getPrice()));
        holder.itemIcon.setImageResource(item.getIconResource());
        
        // Update UI based on quantity
        if (item.getQuantity() > 0) {
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.quantityControls.setVisibility(View.VISIBLE);
            holder.quantityText.setText(String.valueOf(item.getQuantity()));
        } else {
            holder.btnAddToCart.setVisibility(View.VISIBLE);
            holder.quantityControls.setVisibility(View.GONE);
        }
        
        // Click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
        
        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddClick(item);
            }
        });
        
        holder.btnIncrease.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIncreaseClick(item);
            }
        });
        
        holder.btnDecrease.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDecreaseClick(item);
            }
        });
        
        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveClick(item);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    private String formatPrice(int price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price) + "đ";
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        TextView itemName;
        TextView itemTime;
        TextView itemPrice;
        Button btnAddToCart;
        LinearLayout quantityControls;
        TextView quantityText;
        ImageView btnIncrease;
        ImageView btnDecrease;
        ImageView btnRemove;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            itemIcon = itemView.findViewById(R.id.item_icon);
            itemName = itemView.findViewById(R.id.item_name);
            itemTime = itemView.findViewById(R.id.item_time);
            itemPrice = itemView.findViewById(R.id.item_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
            quantityControls = itemView.findViewById(R.id.quantity_controls);
            quantityText = itemView.findViewById(R.id.quantity_text);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}

