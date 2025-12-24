package com.project.laundryappui.menu.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.project.laundryappui.R;
import com.project.laundryappui.menu.home.adapter.HomeAdapter;
import com.project.laundryappui.menu.home.model.HomeModel;
import com.project.laundryappui.services.ServiceDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private Context mContext;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HomeAdapter homeAdapter;
    private List<HomeModel> homeModelList;
    
    private RelativeLayout serviceIroning;
    private RelativeLayout serviceWashIron;
    private RelativeLayout serviceDryClean;
    private RelativeLayout serviceMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initServiceViews(view);
        setupServiceClickListeners();
        setAdapterType(view);
        setAdapter();
    }
    
    private void initServiceViews(View view) {
        // Find the service icon containers by their parent layout
        View container = view.findViewById(R.id.container_choose_service);
        if (container instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) container;
            if (viewGroup.getChildCount() >= 4) {
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
    }
    
    private void openServiceDetail(String serviceType) {
        Intent intent = new Intent(mContext, ServiceDetailActivity.class);
        intent.putExtra("SERVICE_TYPE", serviceType);
        startActivity(intent);
    }

    private void initData() {
        homeModelList = new ArrayList<>();

        homeModelList.add(new HomeModel(R.drawable.bg_post1, "Amanda Laundry", "$10-$20", "Distance 1.2 km" ));
        homeModelList.add(new HomeModel(R.drawable.bg_post2, "Papa Laundry", "$30-$40", "Distance 1.3 km" ));
        homeModelList.add(new HomeModel(R.drawable.bg_post3, "Mama Laundry", "$50-$60", "Distance 1.4 km" ));
    }

    private void setAdapterType(View view) {
        recyclerView    = view.findViewById(R.id.recyclerview_recommended);
        layoutManager   = new LinearLayoutManager(mContext);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(true);
    }

    private void setAdapter() {
        initData();

        homeAdapter = new HomeAdapter(homeModelList);
        recyclerView.setAdapter(homeAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}