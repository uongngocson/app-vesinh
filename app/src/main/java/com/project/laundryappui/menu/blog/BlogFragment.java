package com.project.laundryappui.menu.blog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.laundryappui.R;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment {
    private Context mContext;
    private RecyclerView recyclerView;
    private BlogAdapter blogAdapter;
    private List<BlogModel> blogList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            return inflater.inflate(R.layout.fragment_blog, container, false);
        } catch (Exception e) {
            e.printStackTrace();
            // Return a simple view if inflation fails
            android.widget.TextView errorView = new android.widget.TextView(requireContext());
            errorView.setText("Error loading BlogFragment: " + e.getMessage());
            errorView.setTextColor(android.graphics.Color.RED);
            errorView.setPadding(50, 50, 50, 50);
            return errorView;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            initViews(view);
            setupRecyclerView();
            loadBlogData();
        } catch (Exception e) {
            e.printStackTrace();
            // Show error in UI
            android.widget.TextView errorView = new android.widget.TextView(requireContext());
            errorView.setText("BlogFragment Error: " + e.getMessage());
            errorView.setTextColor(android.graphics.Color.RED);
            errorView.setPadding(50, 50, 50, 50);
            if (view instanceof android.view.ViewGroup) {
                ((android.view.ViewGroup) view).addView(errorView);
            }
        }
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_blog);
    }


    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void loadBlogData() {
        blogList = new ArrayList<>();

        // Add blog posts with URLs from the website
        blogList.add(new BlogModel(
            R.drawable.ic_shirt,
            "Cách vệ sinh giày da hiệu quả tại nhà",
            "Hướng dẫn chi tiết các bước vệ sinh giày da một cách chuyên nghiệp, giúp duy trì độ bền và vẻ đẹp của đôi giày.",
            "Mẹo vệ sinh",
            "15 Dec 2024",
            "3 phút đọc",
            "https://vesinhgiay24h.com/vi/blog/how-to-clean-shoes"
        ));

        blogList.add(new BlogModel(
            R.drawable.ic_washing_machine,
            "Lịch sử quả bóng World Cup Adidas",
            "Khám phá lịch sử phát triển của các quả bóng World Cup từ Adidas qua các kỳ World Cup.",
            "Tin tức",
            "12 Dec 2024",
            "5 phút đọc",
            "https://vesinhgiay24h.com/vi/blog/history-adidas-world-cup-match-balls"
        ));

        blogList.add(new BlogModel(
            R.drawable.ic_iron,
            "Di sản bóng đá nữ Mỹ",
            "Tìm hiểu về lịch sử và sự phát triển của bóng đá nữ tại Mỹ qua các thời kỳ.",
            "Thể thao",
            "10 Dec 2024",
            "4 phút đọc",
            "https://vesinhgiay24h.com/vi/blog/legacy-womens-american-football"
        ));

        blogList.add(new BlogModel(
            R.drawable.ic_more,
            "Dịch vụ vệ sinh chuyên nghiệp từ YIBO",
            "Khám phá các dịch vụ vệ sinh giày và túi xách chuyên nghiệp tại YIBO.vn với công nghệ hiện đại.",
            "Dịch vụ",
            "8 Dec 2024",
            "2 phút đọc",
            "https://vesinhgiay24h.com/vi"
        ));

        blogAdapter = new BlogAdapter(requireContext(), blogList);
        recyclerView.setAdapter(blogAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
