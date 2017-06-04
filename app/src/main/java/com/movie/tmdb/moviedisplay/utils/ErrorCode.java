package com.movie.tmdb.moviedisplay.utils;

/**
 * Created by rohit.khurana on 6/2/2017.
 */

public enum ErrorCode {

        ERROR_API_RESPONSE(40001,"Api response not successful."),
        ERROR_HTTP_RESPONSE(40002,"Error Http Response."),
        ERROR_UNEXPECTED(40003,"Unexpected Error"),
        TIMED_OUT(40004,"Http request timed out."),
        NETWORK_NOT_AVAILABLE(40005,"Network not available.Please try after some time");

        private int mErrorCode;
        private String mErrorMessage;

        ErrorCode(int errorCode,String errorMessage){
            mErrorCode = errorCode;
            mErrorMessage = errorMessage;
        }

        public String getmErrorMessage(){
            return this.mErrorMessage;
        }
}
