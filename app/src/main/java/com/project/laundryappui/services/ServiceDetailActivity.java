package com.project.laundryappui.services;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.laundryappui.R;
import com.project.laundryappui.services.adapter.ServicePriceAdapter;
import com.project.laundryappui.services.data.ServicesDataLoader;
import com.project.laundryappui.services.model.LaundryItem;
import com.project.laundryappui.services.model.LaundryService;
import com.project.laundryappui.services.model.ServiceData;
import com.project.laundryappui.services.model.ServiceItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceDetailActivity extends AppCompatActivity {

    private ImageView serviceIcon;
    private TextView serviceName;
    private TextView serviceDescription;
    private TextView estimatedTime;
    private TextView totalPrice;
    private Button btnBookNow;
    private RecyclerView recyclerViewPrices;
    
    private ServicePriceAdapter priceAdapter;
    private List<ServiceItem> serviceItems;
    private int totalAmount = 0;
    private int totalItems = 0;
    
    private String serviceType;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        
        // Get service type from intent
        serviceType = getIntent().getStringExtra("SERVICE_TYPE");
        if (serviceType == null) {
            serviceType = "IRONING";
        }
        
        initViews();
        setupToolbar();
        setupServiceInfo();
        setupRecyclerView();
        loadServiceItems();
    }
    
    private void initViews() {
        serviceIcon = findViewById(R.id.service_icon);
        serviceName = findViewById(R.id.service_name);
        serviceDescription = findViewById(R.id.service_description);
        estimatedTime = findViewById(R.id.estimated_time);
        totalPrice = findViewById(R.id.total_price);
        btnBookNow = findViewById(R.id.btn_book_now);
        recyclerViewPrices = findViewById(R.id.recyclerview_prices);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    
    private void setupServiceInfo() {
        switch (serviceType) {
            case "IRONING":
                serviceIcon.setImageResource(R.drawable.ic_iron);
                serviceName.setText(R.string.ironing_service);
                serviceDescription.setText(R.string.ironing_desc);
                estimatedTime.setText(R.string.hours_24);
                break;
                
            case "WASH_IRON":
                serviceIcon.setImageResource(R.drawable.ic_washing_machine);
                serviceName.setText(R.string.wash_iron_service);
                serviceDescription.setText(R.string.wash_iron_desc);
                estimatedTime.setText(R.string.hours_48);
                break;
                
            case "DRY_CLEAN":
                serviceIcon.setImageResource(R.drawable.ic_shirt);
                serviceName.setText(R.string.dry_clean_service);
                serviceDescription.setText(R.string.dry_clean_desc);
                estimatedTime.setText(R.string.hours_72);
                break;

            case "WASH_SHOES":
                serviceIcon.setImageResource(R.drawable.ic_more);
                serviceName.setText(R.string.wash_shoes_service);
                serviceDescription.setText(R.string.wash_shoes_desc);
                estimatedTime.setText(R.string.hours_48);
                break;

            default:
                serviceIcon.setImageResource(R.drawable.ic_more);
                serviceName.setText(R.string.service_detail);
                serviceDescription.setText(R.string.service_description);
                estimatedTime.setText(R.string.hours_24);
                break;
        }
    }
    
    private void setupRecyclerView() {
        recyclerViewPrices.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPrices.setHasFixedSize(true);
        
        serviceItems = new ArrayList<>();
        priceAdapter = new ServicePriceAdapter(this, serviceItems, new ServicePriceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ServiceItem item) {
                // Handle item click
                if (item.getQuantity() > 0) {
                    Toast.makeText(ServiceDetailActivity.this, 
                        item.getName() + " - Số lượng: " + item.getQuantity(), 
                        Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onAddClick(ServiceItem item) {
                // Add to cart
                item.setQuantity(1);
                totalItems++;
                updateCart();
                priceAdapter.notifyItemChanged(serviceItems.indexOf(item));
                Toast.makeText(ServiceDetailActivity.this, 
                    "Đã thêm " + item.getName(), 
                    Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onIncreaseClick(ServiceItem item) {
                // Increase quantity
                item.setQuantity(item.getQuantity() + 1);
                totalItems++;
                updateCart();
                priceAdapter.notifyItemChanged(serviceItems.indexOf(item));
            }
            
            @Override
            public void onDecreaseClick(ServiceItem item) {
                // Decrease quantity
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    totalItems--;
                    updateCart();
                    priceAdapter.notifyItemChanged(serviceItems.indexOf(item));
                } else {
                    // If quantity is 1, remove from cart
                    removeFromCart(item);
                }
            }
            
            @Override
            public void onRemoveClick(ServiceItem item) {
                // Remove from cart
                removeFromCart(item);
            }
        });
        
        recyclerViewPrices.setAdapter(priceAdapter);
        
        // Setup book now button
        btnBookNow.setOnClickListener(v -> {
            if (totalItems > 0) {
                showCartSummary();
            } else {
                Toast.makeText(this, 
                    "Vui lòng chọn ít nhất một món", 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadServiceItems() {
        serviceItems.clear();
        
        // Load data từ JSON file - Professional approach
        ServicesDataLoader dataLoader = ServicesDataLoader.getInstance();
        ServiceData serviceData = dataLoader.loadServicesData(this);
        
        if (serviceData != null) {
            LaundryService service = serviceData.getServiceById(serviceType);
            
            if (service != null && service.getItems() != null) {
                // Convert LaundryItem to ServiceItem
                for (LaundryItem item : service.getItems()) {
                    int iconResId = getIconResource(item.getIcon());
                    ServiceItem serviceItem = new ServiceItem(item, iconResId);
                    serviceItems.add(serviceItem);
                }
            }
        }
        
        // Fallback nếu không load được JSON
        if (serviceItems.isEmpty()) {
            loadFallbackData();
        }
        
        priceAdapter.notifyDataSetChanged();
    }
    
    /**
     * Helper method để get icon resource từ tên string
     */
    private int getIconResource(String iconName) {
        try {
            return getResources().getIdentifier(iconName, "drawable", getPackageName());
        } catch (Exception e) {
            return R.drawable.ic_shirt; // Default icon
        }
    }
    
    /**
     * Fallback data nếu JSON load fail
     */
    private void loadFallbackData() {
        switch (serviceType) {
            case "IRONING":
                serviceItems.add(new ServiceItem("Áo sơ mi", 15000, "24 giờ", R.drawable.ic_shirt));
                serviceItems.add(new ServiceItem("Quần dài", 15000, "24 giờ", R.drawable.ic_shirt));
                break;
            case "WASH_IRON":
                serviceItems.add(new ServiceItem("Áo sơ mi", 25000, "48 giờ", R.drawable.ic_washing_machine));
                serviceItems.add(new ServiceItem("Quần dài", 25000, "48 giờ", R.drawable.ic_washing_machine));
                break;
            case "DRY_CLEAN":
                serviceItems.add(new ServiceItem("Áo sơ mi", 40000, "72 giờ", R.drawable.ic_shirt));
                serviceItems.add(new ServiceItem("Quần dài", 45000, "72 giờ", R.drawable.ic_shirt));
                break;
            default:
                serviceItems.add(new ServiceItem("Dịch vụ khác", 50000, "24 giờ", R.drawable.ic_more));
                break;
        }
    }
    
    private void updateCart() {
        // Recalculate total
        totalAmount = 0;
        totalItems = 0;
        
        for (ServiceItem item : serviceItems) {
            if (item.getQuantity() > 0) {
                totalAmount += item.getPrice() * item.getQuantity();
                totalItems += item.getQuantity();
            }
        }
        
        updateTotalPrice();
    }
    
    private void removeFromCart(ServiceItem item) {
        totalItems -= item.getQuantity();
        item.setQuantity(0);
        updateCart();
        priceAdapter.notifyItemChanged(serviceItems.indexOf(item));
        Toast.makeText(this, 
            "Đã xóa " + item.getName(), 
            Toast.LENGTH_SHORT).show();
    }
    
    private void updateTotalPrice() {
        if (totalItems > 0) {
            totalPrice.setText(formatPrice(totalAmount) + " (" + totalItems + " món)");
        } else {
            totalPrice.setText("0đ");
        }
    }
    
    private void showCartSummary() {
        // Inflate custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_cart_summary, null);
        
        // Find views
        TextView cartItemCount = dialogView.findViewById(R.id.cart_item_count);
        LinearLayout cartItemsContainer = dialogView.findViewById(R.id.cart_items_container);
        TextView cartSubtotal = dialogView.findViewById(R.id.cart_subtotal);
        TextView cartTotal = dialogView.findViewById(R.id.cart_total);
        
        // Set item count
        cartItemCount.setText(totalItems + " món");
        
        // Add items to container
        int itemNumber = 0;
        for (ServiceItem item : serviceItems) {
            if (item.getQuantity() > 0) {
                itemNumber++;
                View itemView = getLayoutInflater().inflate(R.layout.item_cart_summary, cartItemsContainer, false);
                
                TextView itemNumberView = itemView.findViewById(R.id.item_number);
                TextView itemName = itemView.findViewById(R.id.item_name);
                TextView itemQuantityPrice = itemView.findViewById(R.id.item_quantity_price);
                TextView itemTotal = itemView.findViewById(R.id.item_total);
                
                itemNumberView.setText(String.valueOf(itemNumber));
                itemName.setText(item.getName());
                itemQuantityPrice.setText(formatPrice(item.getPrice()) + " x " + item.getQuantity());
                itemTotal.setText(formatPrice(item.getPrice() * item.getQuantity()));
                
                cartItemsContainer.addView(itemView);
            }
        }
        
        // Set totals
        cartSubtotal.setText(formatPrice(totalAmount));
        cartTotal.setText(formatPrice(totalAmount));
        
        // Show dialog
        new android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Đặt ngay", (dialog, which) -> {
                Toast.makeText(this, 
                    "Đang xử lý đơn hàng " + totalItems + " món - " + formatPrice(totalAmount), 
                    Toast.LENGTH_LONG).show();
                // TODO: Navigate to checkout/booking confirmation
            })
            .setNegativeButton("Tiếp tục chọn", null)
            .setCancelable(true)
            .show();
    }
    
    private String formatPrice(int price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price) + "đ";
    }
}

