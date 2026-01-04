package com.project.laundryappui.menu.blog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.laundryappui.R;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {
    private Context context;
    private List<BlogModel> blogList;

    public BlogAdapter(Context context, List<BlogModel> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog_post, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        BlogModel blogPost = blogList.get(position);

        holder.blogImage.setImageResource(blogPost.getImageResource());
        holder.blogTitle.setText(blogPost.getTitle());
        holder.blogDescription.setText(blogPost.getExcerpt());
        holder.blogCategory.setText(blogPost.getCategory());
        holder.blogAuthor.setText("Papa Laundry");
        holder.blogDate.setText(blogPost.getDate());
        holder.blogViews.setText("1.2k");

        // Set click listener on itemView instead of cardView for safety
        holder.itemView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(blogPost.getUrl()));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        ImageView blogImage;
        TextView blogTitle;
        TextView blogDescription;
        TextView blogCategory;
        TextView blogAuthor;
        TextView blogDate;
        TextView blogViews;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            blogImage = itemView.findViewById(R.id.blog_image);
            blogTitle = itemView.findViewById(R.id.blog_title);
            blogDescription = itemView.findViewById(R.id.blog_description);
            blogCategory = itemView.findViewById(R.id.blog_category);
            blogAuthor = itemView.findViewById(R.id.blog_author);
            blogDate = itemView.findViewById(R.id.blog_date);
            blogViews = itemView.findViewById(R.id.blog_views);
        }
    }
}
