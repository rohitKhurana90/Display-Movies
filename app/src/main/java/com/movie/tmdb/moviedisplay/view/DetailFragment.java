package com.movie.tmdb.moviedisplay.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.tmdb.moviedisplay.R;
import com.movie.tmdb.moviedisplay.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * Created by rohit.khurana on 6/3/2017.
 */

public class DetailFragment extends DialogFragment {

    Movie mMovie;
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w342/";
    DialogFragment mDialog;

    public static DetailFragment getInstance(){
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    public void setMovie(Movie movie){
        mMovie = movie;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment,container,false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ImageView poster1 = (ImageView) view.findViewById(R.id.poster1);
        Picasso.with(getActivity()).load(IMAGE_URL+mMovie.getPoster_path()).placeholder(R.drawable.placeholder_detail).into(poster1);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView overView = (TextView) view.findViewById(R.id.overview);
        TextView releaseDate = (TextView) view.findViewById(R.id.releasedateVal);
        TextView voteCount = (TextView) view.findViewById(R.id.voteCountVal);
        TextView language = (TextView) view.findViewById(R.id.languageVal);
        ImageView cancel = (ImageView) view.findViewById(R.id.cancel_action);
        Locale loc = new Locale(mMovie.getOriginal_language());

        releaseDate.setText(mMovie.getRelease_date());
        voteCount.setText(mMovie.getVote_count());
        title.setText(mMovie.getTitle());
        overView.setText(mMovie.getOverview());
        language.setText(loc.getDisplayLanguage(loc));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }
}
