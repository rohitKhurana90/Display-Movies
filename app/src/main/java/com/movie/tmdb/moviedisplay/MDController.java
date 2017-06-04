package com.movie.tmdb.moviedisplay;

import android.content.Context;
import android.widget.Toast;

import com.movie.tmdb.moviedisplay.server.ApiProvider;
import com.movie.tmdb.moviedisplay.utils.ErrorCode;
import com.movie.tmdb.moviedisplay.utils.GenericResponse;
import com.movie.tmdb.moviedisplay.utils.MDUtils;

import java.util.List;

/**
 * Created by rohit.khurana on 6/2/2017.
 */

public class MDController {

    private ApiProvider mProvider;
    private Context mContext;

    public MDController(Context context) {
        mContext = context;
        mProvider = new ApiProvider(context);
    }

    public GenericResponse createSessionAndAuthenticate() {
        if (MDUtils.isNetworkAvailable(mContext)) {
            GenericResponse response = mProvider.createRequestToken();
            if (!response.isSuccess()) {
                return response;
            }
            response = mProvider.authneticate();
            if (!response.isSuccess()) {
                return response;
            }
            return mProvider.createSession();
        } else {
            return new GenericResponse(ErrorCode.NETWORK_NOT_AVAILABLE);
        }
    }

    public GenericResponse getMoviesList(String page) {
        if (MDUtils.isNetworkAvailable(mContext)) {
            return mProvider.getMovies(page);
        } else {
            return new GenericResponse(ErrorCode.NETWORK_NOT_AVAILABLE);
        }
    }

    public GenericResponse getMoviesByReleaseYear(String page, String fromYear, String toYear) {
        if (MDUtils.isNetworkAvailable(mContext)) {
            return mProvider.getMoviesByReleaseYear(page, fromYear, toYear);
        } else {
            return new GenericResponse(ErrorCode.NETWORK_NOT_AVAILABLE);
        }

    }
}
