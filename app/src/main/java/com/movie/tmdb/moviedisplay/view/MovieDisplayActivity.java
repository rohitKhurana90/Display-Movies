package com.movie.tmdb.moviedisplay.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.movie.tmdb.moviedisplay.MDController;
import com.movie.tmdb.moviedisplay.MovieAdapter;
import com.movie.tmdb.moviedisplay.R;
import com.movie.tmdb.moviedisplay.model.Movie;
import com.movie.tmdb.moviedisplay.model.MovieResponse;
import com.movie.tmdb.moviedisplay.server.MDSettingsManager;
import com.movie.tmdb.moviedisplay.utils.GenericResponse;
import com.movie.tmdb.moviedisplay.view.FilterFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by rohit.khurana on 6/2/2017.
 */

public class MovieDisplayActivity extends AppCompatActivity implements FilterFragment.FilterCallback {

    private MDController mController;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private static boolean sFilterOn = false;
    private Menu mMenu;
    private static boolean isLoading;
    private static final int PAGE_SIZE = 20;
    private static int sPage = 1;
    private String mFromYear;
    private String mToYear;
    private Activity mAct;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.md_activity);
        mDialog = new ProgressDialog(this);
        mController = new MDController(this);
        mAct = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


        RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mLayoutManager.getItemCount();
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + 2)) {
                    if (!sFilterOn) {
                        new GetMoviesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ++sPage + "");
                    } else {
                        new GetMoviesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ++sPage + "", mFromYear, mToYear);
                    }
                }
            }
        };
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        authProcession();
    }

    private void authProcession() {
        MDSettingsManager settingsManager = MDSettingsManager.getInstance();
        SharedPreferences preferencese = settingsManager.getSharedPreferences(this, MDSettingsManager.APP_SETTINGS);
        if (settingsManager.getStringValue(preferencese, MDSettingsManager.REQUEST_TOKEN) != null &&
                settingsManager.getBooleanValue(preferencese, MDSettingsManager.AUTHENTICATED)) {
            try {
                String expiryDateTime = settingsManager.getStringValue(preferencese, MDSettingsManager.TOKEN_EXPIRY);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                df.setTimeZone(TimeZone.getTimeZone("gmt"));
                String currentTime = df.format(new Date());

                Date currentDate = df.parse(currentTime);
                Date expiryDate = df.parse(expiryDateTime);

                if (expiryDate.compareTo(currentDate) <= 0) {
                    new LoginTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new GetMoviesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sPage + "");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            new LoginTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class LoginTask extends AsyncTask<Void, Void, GenericResponse> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(mAct);
            mDialog.setTitle(mAct.getString(R.string.app_name));
            mDialog.setMessage(mAct.getString(R.string.authenticating_creating_session));
            mDialog.setIndeterminate(true);
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected GenericResponse doInBackground(Void... params) {

            return mController.createSessionAndAuthenticate();
        }

        @Override
        protected void onPostExecute(GenericResponse response) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
            if (response!=null && response.isSuccess()) {
                new GetMoviesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sPage + "");
            } else {
                Toast.makeText(getApplicationContext(), response.getErrorCode().getmErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_diplay_menu, menu);
        return true;
    }

    private class GetMoviesTask extends AsyncTask<String, Void, GenericResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GenericResponse doInBackground(String... params) {
            GenericResponse response = null;
            isLoading = true;
            if (!sFilterOn) {
                response = mController.getMoviesList(params[0]);
            } else {
                response = mController.getMoviesByReleaseYear(params[0], params[1], params[2]);
            }
            return response;
        }

        @Override
        protected void onPostExecute(GenericResponse response) {
            if (response!=null && response.isSuccess()) {
                MovieResponse movieResponse = (MovieResponse) response.getResponseData();
                sPage = Integer.parseInt(movieResponse.getPage());
                if (sPage == 1) {
                    mAdapter.setMovieData(new ArrayList<Movie>(Arrays.asList(movieResponse.getResult())));
                    isLoading = false;
                } else {
                    mAdapter.addMovieData(Arrays.asList(movieResponse.getResult()));
                    isLoading = false;
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuItem filterSort = menu.findItem(R.id.filter);
        if (filterSort != null) {
            filterSort.setIcon((sFilterOn) ? R.drawable.filter_on : R.drawable.filter);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            FilterFragment fragment = new FilterFragment();
            fragment.setmCallBack(this);
            FragmentManager fm = getFragmentManager();
            fragment.show(fm, "filter");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFilterApply(String fromYear, String toYear) {
        sFilterOn = true;
        MenuItem filter = mMenu.findItem(R.id.filter);
        filter.setIcon(R.drawable.filter_on);
        mFromYear = fromYear;
        mToYear = toYear;
        new GetMoviesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1 + "", fromYear, toYear);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sFilterOn = false;
        sPage = 1;
        MenuItem filter = mMenu.findItem(R.id.filter);
        filter.setIcon(R.drawable.filter);
    }

    @Override
    public void onFilterClear() {
        sFilterOn = false;
        sPage = 1;
        MenuItem filter = mMenu.findItem(R.id.filter);
        filter.setIcon(R.drawable.filter);
        new GetMoviesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sPage + "");
    }

}
