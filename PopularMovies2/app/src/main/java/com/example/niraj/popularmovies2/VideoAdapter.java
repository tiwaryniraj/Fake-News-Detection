package com.github.simonoppowa.popularmoviesappstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.simonoppowa.popularmoviesappstage1.model.Video;
import com.github.simonoppowa.popularmoviesappstage1.utilities.YoutubeUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final Context context;

    private List<Video> mVideoList;

    private final ListItemClickListener mListItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public VideoAdapter(Context context, ListItemClickListener listener, List<Video> mVideoList) {
        this.context = context;
        this.mVideoList = mVideoList;
        this.mListItemClickListener = listener;
    }

    public void setVideoList(List<Video> videoList) {
        this.mVideoList = videoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {

        Video selectedVideo = mVideoList.get(position);

        URL tnUrl = YoutubeUtils.buildYoutubeThumbnailURL(selectedVideo.getKey());
        Picasso.with(context).load(tnUrl.toString()).into(holder.videoImageIV);

        holder.videoNameTV.setText(selectedVideo.getType());
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView videoImageIV;
        private final TextView videoNameTV;

        private ViewHolder(View itemView) {
            super(itemView);

            videoImageIV = itemView.findViewById(R.id.video_image);
            videoNameTV = itemView.findViewById(R.id.video_type);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedPosition);
        }
    }
}