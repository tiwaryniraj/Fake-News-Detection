package com.github.simonoppowa.popularmoviesappstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.simonoppowa.popularmoviesappstage1.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final Context context;
    private List<Review> mReviewList;

    public ReviewAdapter(Context context, List<Review> mReviewList) {
        this.context = context;
        this.mReviewList = mReviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.mReviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Review selectedReview = mReviewList.get(position);

        holder.reviewAuthorTV.setText(selectedReview.getAuthor());
        holder.reviewContentTV.setText(selectedReview.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView reviewAuthorTV;
        private final TextView reviewContentTV;

        private ViewHolder(View itemView) {
            super(itemView);

            reviewAuthorTV = itemView.findViewById(R.id.review_author_name);
            reviewContentTV = itemView.findViewById(R.id.review_content);
        }
    }

}
