package com.hasset.umovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hasset.umovies.R;
import com.hasset.umovies.adapter.GalleryAdapter;
import com.hasset.umovies.model.Movie;
import com.hasset.umovies.model.MovieResponse;
import com.hasset.umovies.model.Theme;
import com.hasset.umovies.rest.RetrofitApiClient;
import com.hasset.umovies.rest.TmdbApiInterface;
import com.hasset.umovies.utils.Constants;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements GalleryAdapter.GalleryOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String API_KEY = "19cc83360dbb0af4a3d635c0227fa8da";

    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable;
    private GalleryAdapter galleryAdapter;
    private TmdbApiInterface apiService;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }

        initRecyclerView();

        apiService = RetrofitApiClient.getClient().create(TmdbApiInterface.class);
        compositeDisposable = new CompositeDisposable();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        getMovieFromAPI(Theme.TOP_RATED);

        progressBar.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        galleryAdapter = new GalleryAdapter(this, this);
        recyclerView.setAdapter(galleryAdapter);
    }

    private void getMovieFromAPI(Theme theme) {

        if (theme.equals(Theme.TOP_RATED)) {

            if (Looper.myLooper() == Looper.getMainLooper()) {
                Log.d(TAG, "1 onResponse: running on main thread");
            } else {
                Log.d(TAG, "1 onResponse: not running on main thread");
            }

            compositeDisposable.add(apiService.getTopRatedMovies(API_KEY)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));

        } else if (theme.equals(Theme.POPULAR)) {

            compositeDisposable.add(apiService.getPopularMovies(API_KEY)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } else {
            Log.e(TAG, "Unknown Theme:" + theme.toString());
        }
    }

    private void handleResponse(MovieResponse movieResponse) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.d(TAG, "2 onResponse: running on main thread");
        } else {
            Log.d(TAG, "2 onResponse: not running on main thread");
        }
        List<Movie> movies = movieResponse.getResults();
        galleryAdapter.setMovies(movies);
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(this, "Error " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Log.e(TAG, throwable.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Take appropriate action for each action item click
        switch (item.getItemId()) {

            case R.id.action_top_rated:
                Toast.makeText(this, "Top Rated", Toast.LENGTH_SHORT).show();
                getMovieFromAPI(Theme.TOP_RATED);
                Toast.makeText(this, "Top Rated Loaded", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_popular:
                Toast.makeText(this, "Popular", Toast.LENGTH_SHORT).show();
                getMovieFromAPI(Theme.POPULAR);
                Toast.makeText(this, "Popular Loaded", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.MOVIE, movie);
        startActivity(intent);
    }
}
