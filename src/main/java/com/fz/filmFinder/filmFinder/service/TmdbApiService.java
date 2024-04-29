package com.fz.filmFinder.filmFinder.service;

import com.fz.filmFinder.filmFinder.model.DTO.MovieInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TmdbApiService {
    private static OkHttpClient client = new OkHttpClient();
    final private static String tokenKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYWZjNWZjNTAzY2IyOTk0MmVkZjI0OWU3N2MyNDQ0OCIsInN1YiI6IjY2MmExYjkxNTBmN2NhMDBiM2M4ODAyYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.YtuHLu0FCM1WKN-WbKr9yThSnvU9u5nEALBqt9uQElY";
    final private static String photoResolution = "300"; // resolution number 200 , 300 , 400 , 500 , 600 ....

    private static FinderApiService finderApiService;

    public TmdbApiService(FinderApiService finderApiService) {
        this.finderApiService = finderApiService;

    }

    public static void main(String[] args) throws IOException, JSONException, InterruptedException {
//        Response response = popularMovies();
//        if (response != null && response.isSuccessful()) {
//            String responseBody = response.body().string();
//            log.info(extractMoviesData(responseBody).toString());
//        } else {
//            System.err.println("Failed to get movie data!");
//        }
        FinderApiService finderApiService = new FinderApiService(new FindMovieMapService());
        List<String> moviesList = finderApiService.getMoviesList("dark");
        TmdbApiService tmdbApiService = new TmdbApiService(finderApiService);
        List<MovieInfo> similarMovies = tmdbApiService.getSmiliarListWithInfo(moviesList);
        log.info(similarMovies.toString());
    }

    //get information of similar movies list
    public static LinkedList<MovieInfo> getSmiliarListWithInfo(List<String> similarMoviesList) throws IOException, JSONException, InterruptedException {
        LinkedList<MovieInfo> listSimilarMovies = new LinkedList<MovieInfo>();
        int numThreads = 8; // Number of threads to create
        ThreadPoolExecutor executor = new ThreadPoolExecutor(numThreads, numThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        for (String movie : similarMoviesList) {
            executor.execute(() -> {
                try {
                    Response response = getMovieData(movie);

                    if (response.isSuccessful() && response != null) {
                        String responseString = response.body().string();
                        MovieInfo movieInfo = extractMovieData(responseString);
                        if (movieInfo == null) {
                            log.info("movie name ---------------->" + movie + " is null");
                        } else {
                            listSimilarMovies.add(movieInfo);
//                            log.info(movieInfo.toString());
                        }
                    } else {
                        log.error("error in getSmiliarListWithInfo and getMovieData from server");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e); // Include the caught exception when rethrowing
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        return listSimilarMovies;
    }


    ////////////////////////////////////////////////////////////////
//get response of a movie data
    private static Response getMovieData(String movieName) throws IOException {
        String movieUrlType = "";
        try {
            movieUrlType = URLEncoder.encode(movieName, "UTF-8");
        } catch (IOException e) {
            throw new IOException("Unsupported encoding: " + e.getMessage());
        }

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/search/movie?query=" + movieUrlType + "&include_adult=false&language=en-US&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", ("Bearer " + tokenKey))
                .build();

        return client.newCall(request).execute();
    }

    ////////////////////////////////////////////////////////////////////////
    //get a response popularMovies
    private static Response popularMovies() throws IOException {

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + tokenKey)
                .build();

        return client.newCall(request).execute();


    }

    //get popular movies list
    public static LinkedList<MovieInfo> popularMoviesList() throws IOException, JSONException {
        Response response = popularMovies();
        LinkedList<MovieInfo> movieInfos = new LinkedList<MovieInfo>();
        if (response != null && response.isSuccessful()) {
            String responseBody = response.body().string();
            movieInfos = extractMoviesData(responseBody);
            //get from movie object
//            log.info(extractMoviesData(responseBody).toString());
        } else {
            System.err.println("Failed to get movie data!");
        }
        return movieInfos;
    }

    ////////////////////////////////////////////////////////////////////////
    //response body string to linked list of movies information
    private static LinkedList<MovieInfo> extractMoviesData(String responseBody) throws JSONException {
        LinkedList<MovieInfo> moviesInfo = new LinkedList<>();
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray results = jsonResponse.getJSONArray("results");

            // Loop through each movie in the results
            for (int i = 0; i < results.length(); i++) {
                MovieInfo movieInfo = new MovieInfo();
                JSONObject movie = results.getJSONObject(i);
                //title
                movieInfo.setTitle(movie.getString("original_title"));
                //String Date to Integer Date
                //year
                String dateStr = movie.getString("release_date");
                String yearStr = dateStr.split("-")[0];
                Integer yearInteger = Integer.parseInt(yearStr);
                movieInfo.setYear(yearInteger);
                ///img link
                movieInfo.setPosterUrl("https://image.tmdb.org/t/p/w" + photoResolution + movie.getString("poster_path"));
                //add a movie to movie list
                moviesInfo.add(movieInfo);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON error here
        }

        return moviesInfo;
    }

    //get a movie info with object format
    private static MovieInfo extractMovieData(String responseBody) throws JSONException {
        MovieInfo movieInfo = new MovieInfo();
        try {
            //get all results from json
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray results = jsonResponse.getJSONArray("results");
            //-----------------------------------------------------------------
            if (results.length() == 0) {
                return null;
            }
            //get first result
            JSONObject movie = results.getJSONObject(0);
            //title
            movieInfo.setTitle(movie.getString("original_title"));
            //String Date to Integer Date
            //year
            String dateStr = movie.getString("release_date");
            String yearStr = dateStr.split("-")[0];
            Integer yearInteger = 000;
            if (dateStr.length() > 0) {
                yearInteger = Integer.parseInt(yearStr);
            }
            movieInfo.setYear(yearInteger);
            ///img link
            movieInfo.setPosterUrl("https://image.tmdb.org/t/p/w" + photoResolution + movie.getString("poster_path"));
            //add a movie to movie list


        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON error here
        }

        return movieInfo;
    }


}
