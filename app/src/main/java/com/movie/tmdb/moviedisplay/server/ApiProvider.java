package com.movie.tmdb.moviedisplay.server;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.movie.tmdb.moviedisplay.model.MovieResponse;
import com.movie.tmdb.moviedisplay.utils.ErrorCode;
import com.movie.tmdb.moviedisplay.utils.GenericResponse;
import com.movie.tmdb.moviedisplay.utils.MDUtils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rohit.khurana on 6/2/2017.
 */

public class ApiProvider {


    public static final String USER_NAME = "rohit.khurana";
    public static final String PASSWORD = "rohit141290";
    public static final String API_KEY = "ef0ee8647117d5d57be6bd2eea56afd5";

    private static final String URL = "https://api.themoviedb.org/3";
    private static final String REQUEST_TOKEN = "/authentication/token/new";
    private static final String VALIDATE_TOKEN = "/authentication/token/validate_with_login";
    private static final String CREATE_SESSION = "/authentication/session/new";
    private static final String GET_MOVIES = "/movie/popular";
    private static final String GET_MOVIES_BY_YOR = "/discover/movie";

    private static String sRequestToken;
    private static String sSessionId;
    private static String sTokenExpiry;
    private Context mContext;

    public ApiProvider(Context context) {
        mContext = context;
    }

    public GenericResponse createRequestToken() {
        HttpsURLConnection urlConn = null;
        try {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("api_key", API_KEY);
            String query = urlEncodedString(queryMap);
            urlConn = (HttpsURLConnection) new URL(URL + REQUEST_TOKEN + "?" + query).openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setReadTimeout(60000);
            if (urlConn.getResponseCode() == 200) {
                String theString = IOUtils.toString(urlConn.getInputStream(), "UTF-8");
                Map<String, String> map = MDUtils.parseJsonAsMap(theString);
                if (Boolean.parseBoolean(map.get("success"))) {
                    sRequestToken = map.get("request_token");
                    sTokenExpiry = map.get("expires_at");
                    MDSettingsManager settingsManager = MDSettingsManager.getInstance();
                    SharedPreferences.Editor editor = settingsManager.getSharedPreferences(mContext, MDSettingsManager.APP_SETTINGS).edit();
                    editor.putString(MDSettingsManager.REQUEST_TOKEN, sRequestToken);
                    editor.putString(MDSettingsManager.TOKEN_EXPIRY, sTokenExpiry);
                    editor.commit();
                    return new GenericResponse(true, null);
                } else {
                    return new GenericResponse(ErrorCode.ERROR_API_RESPONSE);
                }
            } else if (urlConn.getResponseCode() == 408) {
                return new GenericResponse(ErrorCode.TIMED_OUT);
            } else {
                return new GenericResponse(ErrorCode.ERROR_HTTP_RESPONSE);
            }
        } catch (IOException e) {
            return new GenericResponse(ErrorCode.ERROR_UNEXPECTED);
        } finally {
            // Don't close connection, we are returning connection
        }
    }


    public GenericResponse authneticate() {
        HttpsURLConnection urlConn = null;
        try {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("api_key", API_KEY);
            queryMap.put("username", USER_NAME);
            queryMap.put("password", PASSWORD);
            queryMap.put("request_token", sRequestToken);
            String query = urlEncodedString(queryMap);
            urlConn = (HttpsURLConnection) new URL(URL + VALIDATE_TOKEN + "?" + query).openConnection();
            urlConn.setRequestMethod("GET");
            if (urlConn.getResponseCode() == 200) {
                String theString = IOUtils.toString(urlConn.getInputStream(), "UTF-8");
                Map<String, String> map = MDUtils.parseJsonAsMap(theString);
                if (Boolean.parseBoolean(map.get("success"))) {
                    MDSettingsManager settingsManager = MDSettingsManager.getInstance();
                    SharedPreferences.Editor editor = settingsManager.getSharedPreferences(mContext, MDSettingsManager.APP_SETTINGS).edit();
                    editor.putBoolean(MDSettingsManager.AUTHENTICATED, true);
                    editor.commit();
                    return new GenericResponse(true, null);
                } else {
                    return new GenericResponse(ErrorCode.ERROR_API_RESPONSE);
                }
            } else if (urlConn.getResponseCode() == 408) {
                return new GenericResponse(ErrorCode.TIMED_OUT);
            } else {
                return new GenericResponse(ErrorCode.ERROR_HTTP_RESPONSE);
            }
        } catch (IOException e) {
            return new GenericResponse(ErrorCode.ERROR_UNEXPECTED);
        } finally {
            // Don't close connection, we are returning connection
        }
    }

    public GenericResponse createSession(String requestToken) {
        sRequestToken = requestToken;
        return createSession();
    }

    public GenericResponse createSession() {
        HttpsURLConnection urlConn = null;
        try {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("api_key", API_KEY);
            queryMap.put("request_token", sRequestToken);
            String query = urlEncodedString(queryMap);
            urlConn = (HttpsURLConnection) new URL(URL + CREATE_SESSION + "?" + query).openConnection();
            urlConn.setRequestMethod("GET");
            if (urlConn.getResponseCode() == 200) {
                String theString = IOUtils.toString(urlConn.getInputStream(), "UTF-8");
                Map<String, String> map = MDUtils.parseJsonAsMap(theString);
                if (Boolean.parseBoolean(map.get("success"))) {
                    sSessionId = map.get("session_id");
                    MDSettingsManager settingsManager = MDSettingsManager.getInstance();
                    SharedPreferences.Editor editor = settingsManager.getSharedPreferences(mContext, MDSettingsManager.APP_SETTINGS).edit();
                    editor.putString(MDSettingsManager.SESSION_ID, sSessionId);
                    editor.commit();
                    return new GenericResponse(true, null);
                } else {
                    return new GenericResponse(ErrorCode.ERROR_API_RESPONSE);
                }
            } else if (urlConn.getResponseCode() == 408) {
                return new GenericResponse(ErrorCode.TIMED_OUT);
            } else {
                return new GenericResponse(ErrorCode.ERROR_HTTP_RESPONSE);
            }
        } catch (IOException e) {
            return new GenericResponse(ErrorCode.ERROR_UNEXPECTED);
        } finally {
            // Don't close connection, we are returning connection
        }

    }


    public GenericResponse getMovies(String page) {

        GenericResponse response = null;
        HttpsURLConnection urlConn = null;
        try {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("api_key", API_KEY);
            queryMap.put("page", page);
            String query = urlEncodedString(queryMap);
            urlConn = (HttpsURLConnection) new URL(URL + GET_MOVIES + "?" + query).openConnection();
            urlConn.setRequestMethod("GET");
            if (urlConn.getResponseCode() == 200) {
                String theString = IOUtils.toString(urlConn.getInputStream(), "UTF-8");
                if (theString != null) {
                    Gson gson = new Gson();
                    MovieResponse movie = gson.fromJson(theString, MovieResponse.class);
                    return new GenericResponse(true, movie);
                }
            } else if (urlConn.getResponseCode() == 408) {
                return new GenericResponse(ErrorCode.TIMED_OUT);
            } else {
                response = new GenericResponse(ErrorCode.ERROR_HTTP_RESPONSE);
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new GenericResponse(ErrorCode.ERROR_UNEXPECTED);
        } finally {
            // Don't close connection, we are returning connection
        }
        return null;

    }


    public GenericResponse getMoviesByReleaseYear(String page, String fromYear, String toYear) {

        GenericResponse response = null;
        HttpsURLConnection urlConn = null;
        try {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("api_key", API_KEY);
            queryMap.put("sort_by", "popularity.desc");
            queryMap.put("page", page);
            queryMap.put("primary_release_date.gte", fromYear);
            queryMap.put("primary_release_date.lte", toYear);
            String query = urlEncodedString(queryMap);
            urlConn = (HttpsURLConnection) new URL(URL + GET_MOVIES_BY_YOR + "?" + query).openConnection();
            urlConn.setRequestMethod("GET");
            if (urlConn.getResponseCode() == 200) {
                String theString = IOUtils.toString(urlConn.getInputStream(), "UTF-8");
                if (theString != null) {
                    Gson gson = new Gson();
                    MovieResponse movie = gson.fromJson(theString, MovieResponse.class);
                    return new GenericResponse(true, movie);
                }
            } else {
                response = new GenericResponse(ErrorCode.ERROR_HTTP_RESPONSE);
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new GenericResponse(ErrorCode.ERROR_UNEXPECTED);
        } finally {
            // Don't close connection, we are returning connection
        }
        return null;

    }

    public String urlEncodedString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (String key : params.keySet()) {
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(params.get(key), "UTF-8"));
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }
}
