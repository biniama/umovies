package com.hasset.umovies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hasset.umovies.R;
import com.hasset.umovies.model.Movie;
import com.hasset.umovies.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hasset.umovies.utils.Util.constructImagePath;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.detail_image_view) ImageView detailImageView;
    @BindView(R.id.rating_bar) RatingBar ratingBar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.overview) TextView overview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();

        // Enabling Up / Back navigation
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            Movie movie = (Movie) bundle.get(Constants.MOVIE);

            if (movie != null) {
                title.setText(movie.getTitle());

                Picasso.with(getApplicationContext())
                        .load(constructImagePath(movie.getPosterPath()))
                        .into(detailImageView);
                ratingBar.setRating(Float.parseFloat(movie.getPopularity().toString()));
                releaseDate.setText(movie.getReleaseDate());
                overview.setText(movie.getOverview());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
