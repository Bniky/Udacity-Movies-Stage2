package Utilities;

/**
 * Created by Nicholas on 01/08/2017.
 */

import org.json.JSONException;

import android.util.Log;

import com.bniky.nicholas.movies.data.MovieTrailer;
import com.bniky.nicholas.movies.data.ReviewsOfMovie;
import com.bniky.nicholas.movies.data.MovieImageData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class NetworkHelper {


    /** Tag for the log messages */
    public static final String LOG_TAG = NetworkHelper.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link NetworkHelper} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private NetworkHelper() {
    }


    /**
     * Query the MOVIEDB dataset and return an {@link MovieImageData} object to represent a single earthquake.
     */

    /**
     * Return a list of {@link MovieImageData} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<MovieImageData> extractMovieImage(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<MovieImageData> movieData = new ArrayList<>();
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of Earthquake objects with the corresponding data.

            jsonResponse = makeHttpRequest(url);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.optJSONArray("results");

            Log.v(LOG_TAG, "FORLOOPING");
            for(int i=0;i<results.length();i++){
                JSONObject objectF = results.getJSONObject(i);

                String poster_path = objectF.getString("poster_path");
                String title = objectF.getString("title");
                int id = objectF.getInt("id");
                double vote_Average = objectF.getDouble("vote_average");
                String backDrop = objectF.getString("backdrop_path");
                String overView = objectF.getString("overview");
                String releaseOfFilm = objectF.getString("release_date");

                movieData.add(new MovieImageData(poster_path, title, id, vote_Average, backDrop, overView, releaseOfFilm));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);

        }catch (IOException io){
            io.printStackTrace();
        }

        // Return the list of earthquakes
        return movieData;
    }

    public static <T> ArrayList<T> extractMovieTrailer(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<T> movieTrailerData = new ArrayList<>();
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            Log.i("NetworkHelper", url.toString());

            jsonResponse = makeHttpRequest(url);
            Log.i("NetworkHelper", jsonResponse);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.optJSONArray("results");

            Log.v(LOG_TAG, "FORLOOPING--in MovieTrailer");
            for(int i=0;i<results.length();i++){
                JSONObject objectF = results.getJSONObject(i);

                String key = objectF.getString("key");

                movieTrailerData.add((T) new MovieTrailer(key));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("NetworkHelper", "Problem parsing the movie JSON results " + requestUrl, e);

        }catch (IOException io){
            io.printStackTrace();
        }

        // Return the list of earthquakes
        return movieTrailerData;
    }

    public static <T> ArrayList<T> extractData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<T> movieData = new ArrayList<>();

        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        //Direct the Loader to extract the correct information, Look for key words in URL
        if(requestUrl.contains("popular") || requestUrl.contains("top_rate")) {
            return imageDataFetch(movieData, jsonResponse, url);
        }else if(requestUrl.contains("video")){
            return trailerVideoDataFetch(movieData, jsonResponse, url);
        }else if(requestUrl.contains("review")){
            return reviewMovieDataFetch(movieData, jsonResponse, url);
        }
            return null;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Returns ArrayList for Movie details first Activity GridView
    private static <T> ArrayList<T> imageDataFetch(List<T> movieData, String jsonResponse, URL url) {

        // build up a list of MovieData objects with the corresponding data.
        try {
            jsonResponse = makeHttpRequest(url);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.optJSONArray("results");

            Log.v(LOG_TAG, "FORLOOPING");
            for (int i = 0; i < results.length(); i++) {
                JSONObject objectF = results.getJSONObject(i);

                String poster_path = objectF.getString("poster_path");
                String title = objectF.getString("title");
                int id = objectF.getInt("id");
                double vote_Average = objectF.getDouble("vote_average");
                String backDrop = objectF.getString("backdrop_path");
                String overView = objectF.getString("overview");
                String releaseOfFilm = objectF.getString("release_date");

                movieData.add((T) new MovieImageData(poster_path, title, id, vote_Average, backDrop, overView, releaseOfFilm));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("NetworkHelper", "Problem parsing the movie JSON results " + url.toString(), e);

        } catch (IOException io) {
            io.printStackTrace();
        }
        return (ArrayList<T>) movieData;
    }

    private static <T> ArrayList<T> trailerVideoDataFetch(List<T> movieData, String jsonResponse, URL url){

        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            Log.i("NetworkHelper", url.toString());

            jsonResponse = makeHttpRequest(url);
            Log.i("NetworkHelper", jsonResponse);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.optJSONArray("results");

            Log.v(LOG_TAG, "FORLOOPING--in MovieTrailer");
            for(int i=0;i<results.length();i++){
                JSONObject objectF = results.getJSONObject(i);

                String key = objectF.getString("key");

                movieData.add((T) new MovieTrailer(key));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("NetworkHelper", "Problem parsing the movie JSON results " + url.toString(), e);

        }catch (IOException io){
            io.printStackTrace();
        }

        // Return the list of earthquakes
        return  (ArrayList <T>) movieData;
    }

    private static <T> ArrayList<T> reviewMovieDataFetch(List<T> movieData, String jsonResponse, URL url){

        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            Log.i("NetworkHelper", url.toString());

            jsonResponse = makeHttpRequest(url);
            Log.i("NetworkHelper", jsonResponse);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.optJSONArray("results");

            Log.v(LOG_TAG, "FORLOOPING--in Reviews");
            for(int i=0;i<results.length();i++){
                JSONObject objectF = results.getJSONObject(i);

                String author = objectF.getString("author");
                String content = objectF.getString("content");

                movieData.add((T) new ReviewsOfMovie(author, content));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("NetworkHelper", "Problem parsing the movie JSON results " + url.toString(), e);

        }catch (IOException io){
            io.printStackTrace();
        }

        // Return the list of earthquakes
        return  (ArrayList <T>) movieData;
    }
}
