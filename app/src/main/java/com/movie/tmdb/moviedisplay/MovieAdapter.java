package com.movie.tmdb.moviedisplay;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movie.tmdb.moviedisplay.model.Movie;
import com.movie.tmdb.moviedisplay.view.DetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohit.khurana on 6/3/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> mMovieList;
    private Activity mAct;
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public MovieAdapter(Activity context) {
        mAct = context;
        mMovieList = new ArrayList<>();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public CardView movieCard;
        public ImageView poster;
        public TextView movieName;
        public TextView year;

        public MovieViewHolder(View view) {
            super(view);
            this.movieCard = (CardView) view.findViewById(R.id.card_view);
            this.poster = (ImageView) view.findViewById(R.id.moviePoster);
            this.movieName = (TextView) view.findViewById(R.id.name);
            this.year = (TextView) view.findViewById(R.id.yearOfRelease);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            viewHolder = new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row, parent, false));
        } else {
            viewHolder = new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            final Movie movie = mMovieList.get(position);

            Picasso.with(mAct).load(IMAGE_URL + movie.getPoster_path()).placeholder(R.drawable.placeholder).into(((MovieViewHolder) holder).poster);
            ((MovieViewHolder) holder).movieName.setText(movie.getTitle());
            ((MovieViewHolder) holder).year.setText((movie.getRelease_date().split("-"))[0]);

            ((MovieViewHolder) holder).movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailFragment fragment = DetailFragment.getInstance();
                    fragment.setMovie(movie);
                    FragmentManager fm = mAct.getFragmentManager();
                    fragment.show(fm, "dialog");
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return mMovieList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setMovieData(List<Movie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    public void addMovieData(List<Movie> movieList) {
        int size = mMovieList.size();
        mMovieList.addAll(new ArrayList<Movie>(movieList));
        notifyItemRangeInserted(size, movieList.size());
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }


}
