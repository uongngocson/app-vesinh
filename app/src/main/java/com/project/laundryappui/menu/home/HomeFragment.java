package com.project.laundryappui.menu.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.project.laundryappui.R;
import com.project.laundryappui.ShoeBookingActivity;
import com.project.laundryappui.menu.home.adapter.HomeAdapter;
import com.project.laundryappui.menu.home.model.HomeModel;
import com.project.laundryappui.services.ServiceDetailActivity;
import com.project.laundryappui.utils.JsonDataManager;
import com.project.laundryappui.utils.LocationManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    
    private Context mContext;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HomeAdapter homeAdapter;
    private List<HomeModel> homeModelList;
    
    private JsonDataManager jsonDataManager;
    private LocationManager locationManager;
    
    private RelativeLayout serviceIroning;
    private RelativeLayout serviceWashIron;
    private RelativeLayout serviceDryClean;
    private RelativeLayout serviceMore;
    private RelativeLayout serviceWashShoes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // Khởi tạo managers
            jsonDataManager = JsonDataManager.getInstance(mContext);
            locationManager = LocationManager.getInstance(mContext);
            
            initServiceViews(view);
            setupServiceClickListeners();
            setAdapterType(view);
            loadStoresWithLocation();
        } catch (Exception e) {
            Log.e(TAG, "Error in onViewCreated", e);
            e.printStackTrace();
            Toast.makeText(mContext, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void initServiceViews(View view) {
        // Find the service icon containers by their parent layout
        View container = view.findViewById(R.id.container_choose_service);
        if (container instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) container;
            if (viewGroup.getChildCount() >= 5) {
                serviceIroning = (RelativeLayout) viewGroup.getChildAt(0);
                serviceWashIron = (RelativeLayout) viewGroup.getChildAt(1);
                serviceDryClean = (RelativeLayout) viewGroup.getChildAt(2);
                serviceMore = (RelativeLayout) viewGroup.getChildAt(3);
                serviceWashShoes = (RelativeLayout) viewGroup.getChildAt(4);
            } else if (viewGroup.getChildCount() >= 4) {
                serviceIroning = (RelativeLayout) viewGroup.getChildAt(0);
                serviceWashIron = (RelativeLayout) viewGroup.getChildAt(1);
                serviceDryClean = (RelativeLayout) viewGroup.getChildAt(2);
                serviceMore = (RelativeLayout) viewGroup.getChildAt(3);
            }
        }
    }
    
    private void setupServiceClickListeners() {
        if (serviceIroning != null) {
            serviceIroning.setOnClickListener(v -> openServiceDetail("IRONING"));
        }
        
        if (serviceWashIron != null) {
            serviceWashIron.setOnClickListener(v -> openServiceDetail("WASH_IRON"));
        }
        
        if (serviceDryClean != null) {
            serviceDryClean.setOnClickListener(v -> openServiceDetail("DRY_CLEAN"));
        }
        
        if (serviceMore != null) {
            serviceMore.setOnClickListener(v -> openServiceDetail("MORE"));
        }

        if (serviceWashShoes != null) {
            serviceWashShoes.setOnClickListener(v -> openShoeBooking());
        }
    }
    
    private void openServiceDetail(String serviceType) {
        Intent intent = new Intent(mContext, ServiceDetailActivity.class);
        intent.putExtra("SERVICE_TYPE", serviceType);
        startActivity(intent);
    }

    private void openShoeBooking() {
        Intent intent = new Intent(mContext, ShoeBookingActivity.class);
        startActivity(intent);
    }

    /**
     * Load danh sách cửa hàng và sắp xếp theo khoảng cách từ vị trí user
     */
    private void loadStoresWithLocation() {
        // Lấy vị trí hiện tại của user
        locationManager.getCurrentLocation((latitude, longitude) -> {
            Log.d(TAG, "Got user location: " + latitude + ", " + longitude);
            
            try {
                // Lấy danh sách cửa hàng đã sort theo khoảng cách
                homeModelList = jsonDataManager.getStoresSortedByDistance(latitude, longitude);
                
                Log.d(TAG, "Loaded and sorted " + homeModelList.size() + " stores by distance");
                
                // Nếu không có dữ liệu, sử dụng fallback
                if (homeModelList == null || homeModelList.isEmpty()) {
                    Log.w(TAG, "No data loaded, using fallback data");
                    homeModelList = getFallbackData();
                }
                
                // Cập nhật adapter với dữ liệu mới
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        updateAdapter();
                    });
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error loading data from JSON", e);
                homeModelList = getFallbackData();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        updateAdapter();
                    });
                }
            }
        });
    }
    
    /**
     * Cập nhật adapter với dữ liệu mới
     */
    private void updateAdapter() {
        if (homeAdapter == null) {
            homeAdapter = new HomeAdapter(homeModelList);
            recyclerView.setAdapter(homeAdapter);
        } else {
            homeAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * Dữ liệu dự phòng nếu không load được JSON
     */
    private List<HomeModel> getFallbackData() {
        List<HomeModel> fallbackList = new ArrayList<>();
        fallbackList.add(new HomeModel(R.drawable.bg_post1, "Amanda Laundry", "$10-$20", "Distance 1.2 km"));
        fallbackList.add(new HomeModel(R.drawable.bg_post2, "Papa Laundry", "$30-$40", "Distance 1.3 km"));
        fallbackList.add(new HomeModel(R.drawable.bg_post3, "Mama Laundry", "$50-$60", "Distance 1.4 km"));
        return fallbackList;
    }

    private void setAdapterType(View view) {
        recyclerView    = view.findViewById(R.id.recyclerview_recommended);
        layoutManager   = new LinearLayoutManager(mContext);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(true);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}