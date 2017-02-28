package com.hasset.umovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hasset.umovies.R;
import com.hasset.umovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<Movie> movies;
    private Context context;

    private GalleryOnClickHandler clickHandler;

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface GalleryOnClickHandler {
        void onClick(Movie movie);
    }

    public GalleryAdapter(GalleryOnClickHandler clickHandler, Context context) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.thumbnail) ImageView thumbnail;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movies.get(adapterPosition);
            clickHandler.onClick(movie);
        }
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int rowLayout = R.layout.gallery_thumbnail;
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {

        Picasso.with(context)
                .load(constructImagePath(movies.get(position).getPosterPath()))
                //.resize(680, 900)
                .placeholder(R.drawable.movie_placeholder)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        return movies.size();
    }

    private String constructImagePath(String posterPath) {
        return "http://image.tmdb.org/t/p/w185".concat(posterPath);
    }
}
