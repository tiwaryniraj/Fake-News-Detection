package com.github.simonoppowa.popularmoviesappstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.simonoppowa.popularmoviesappstage1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Simon on 15.03.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private final Context context;

    private List<Movie> mPopularMovieList;

    private final ListItemClickListener mListItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MovieAdapter(Context context, ListItemClickListener listener, List<Movie> popularMovieList) {
        this.context = context;
        this.mListItemClickListener = listener;
        this.mPopularMovieList = popularMovieList;
    }

    public void setPopularMoviesList(List<Movie> popularMoviesList) {
        mPopularMovieList = popularMoviesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //setting title to textView
        holder.titleTV.setText(mPopularMovieList.get(position).getTitle());

        //setting image to imageView
        Picasso.with(context).load(mPopularMovieList.get(position).getImagePath()).into(holder.movieImageIV);

    }

    @Override
    public int getItemCount() {
        return mPopularMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView movieImageIV;
        private final TextView titleTV;


        private ViewHolder(View itemView) {
            super(itemView);

            movieImageIV = itemView.findViewById(R.id.movie_image);
            titleTV = itemView.findViewById(R.id.movie_title);

            itemView.setOnClickListener(this);

            //itemViewClickListener only works on TextView :/
            movieImageIV.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedPosition);
        }
    }
}
