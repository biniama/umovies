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

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<Movie> movies;
    private int rowLayout;
    private Context context;

    protected static class GalleryViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    public GalleryAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {

        Picasso.with(context)
                .load(constructImagePath(movies.get(position).getPosterPath()))
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    String constructImagePath(String posterPath) {
        return "http://image.tmdb.org/t/p/w780".concat(posterPath);
    }
}
