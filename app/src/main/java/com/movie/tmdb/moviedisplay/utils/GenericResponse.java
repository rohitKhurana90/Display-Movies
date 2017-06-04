package com.movie.tmdb.moviedisplay.utils;

public class GenericResponse {
    private boolean mSuccess;
    private ErrorCode mErrorCode;
    private Object mResponseData;

    public GenericResponse() {
    }

    public GenericResponse(ErrorCode errorCode) {
        this.mSuccess = false;
        this.mErrorCode = errorCode;
    }


    public GenericResponse(boolean success, Object responseData) {
        this.mSuccess = success;
        this.mResponseData = responseData;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        this.mSuccess = success;
    }

    public ErrorCode getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.mErrorCode = errorCode;
    }

    public Object getResponseData() {
        return mResponseData;
    }

    public void setResponseData(Object responseData) {
        this.mResponseData = responseData;
    }

}