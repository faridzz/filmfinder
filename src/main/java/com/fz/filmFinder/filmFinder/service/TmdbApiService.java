package com.fz.filmFinder.filmFinder.service;

import com.fz.filmFinder.filmFinder.defaultData.DefaultData;
import com.fz.filmFinder.filmFinder.enums.MovieOrTv;
import com.fz.filmFinder.filmFinder.model.DTO.MovieInfoDTO;
import com.fz.filmFinder.filmFinder.model.MovieInfo;
import com.fz.filmFinder.filmFinder.service.dataExtractor.DataExtractor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class TmdbApiService {


    private static OkHttpClient client = new OkHttpClient();
    final private static String tokenKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYWZjNWZjNTAzY2IyOTk0MmVkZjI0OWU3N2MyNDQ0OCIsInN1YiI6IjY2MmExYjkxNTBmN2NhMDBiM2M4ODAyYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.YtuHLu0FCM1WKN-WbKr9yThSnvU9u5nEALBqt9uQElY";
    final private static String photoResolution = "300"; // resolution number 200 , 300 , 400 , 500 , 600 ....

    private static FinderApiService finderApiService;
    @Autowired
    DefaultData defaultData;
    @Autowired
    CreateAMovieInfo createMovieInfo;
    @Autowired
    private CURDService curdService;

    public TmdbApiService(FinderApiService finderApiService, DefaultData defaultData) {
        this.defaultData = defaultData;
        this.finderApiService = finderApiService;

    }

    public TmdbApiService() {


    }

    public static void main(String[] args) throws IOException, JSONException, InterruptedException {
//        Response response = popularMovies();
//        if (response != null && response.isSuccessful()) {
//            String responseBody = response.body().string();
//            log.info(extractMoviesData(responseBody).toString());
//        } else {
//            System.err.println("Failed to get movie data!");
//        }
//        FinderApiService finderApiService = new FinderApiService(new FindMovieMapService());
//        List<String> moviesList = finderApiService.getMoviesList("dark");
//        TmdbApiService tmdbApiService = new TmdbApiService(finderApiService);
//        List<MovieInfoDTO> similarMovies = tmdbApiService.getSmiliarListWithInfo(moviesList);
//        log.info(similarMovies.toString());

        DefaultData defaultData = new DefaultData();
        System.out.println(defaultData.popularMovies());
    }

    //get information of similar movies list
    public static LinkedList getSmiliarListWithInfo(List<String> similarMoviesList) throws IOException, JSONException, InterruptedException {
        CreateAMovieInfo creatAMovieInfo = new CreateAMovieInfo();

        LinkedList<MovieInfoDTO> listSimilarMovies = new LinkedList<>();
        //seached movie info DTO
        MovieInfoDTO serchedMovieInfoDTO = new MovieInfoDTO();

        if (similarMoviesList.size() > 2) {
            // movie name who serached
            String serchedMovie = similarMoviesList.stream().findFirst().get();
            //dto of serched movie
            serchedMovieInfoDTO = findMostCorrectData(serchedMovie);
            //log info about serched movie
            log.info("Serched................................................................" +
                    serchedMovieInfoDTO.getTitle() +
                    serchedMovieInfoDTO.getYear());
            ExecutorService executor = Executors.newCachedThreadPool(); // Create a thread pool

            for (String movie : similarMoviesList) {

                executor.submit(() -> {
                    MovieInfoDTO movieInfoDTO = findMostCorrectData(movie);
                    if (movieInfoDTO != null) {
                        listSimilarMovies.add(movieInfoDTO);
                    }

                });

            }

            executor.shutdown();
//        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            executor.awaitTermination(10, TimeUnit.SECONDS);

            // made a array of movie serched data and movies similar
            LinkedList complexResults = new LinkedList();
            complexResults.add(serchedMovieInfoDTO);
            complexResults.add(listSimilarMovies);
            return complexResults;

        } else {
            return listSimilarMovies;
        }
    }
////////////////////////////////////////////////////////////////////////
    private static MovieInfoDTO findMostCorrectData(String movie) {
        try {
            // Get movie data from the server
            Response response = getMovieData(movie, "movie");

            if (response.isSuccessful() && response != null) {
                String responseString;
                try (response) {
                    responseString = response.body().string();
                }

                // Extract movie info from the response
                DataExtractor dataExtractor = new DataExtractor();
                MovieInfoDTO movieInfoDTO = (MovieInfoDTO) dataExtractor.extract(responseString, photoResolution, MovieOrTv.MOVIE);

                // If movie info is null, try to get TV show info
                if (movieInfoDTO == null) {
                    log.info("MovieInfoDTO is null, checking for TV show info");
                    movieInfoDTO = getTVInfo(movie);

                    if (movieInfoDTO == null) {
                        log.info("Movie name \"" + movie + "\" has no movie or TV show info");
                    }
                } else {
                    // If movie info has less than 150 votes or is null, check for TV show info
                    if (movieInfoDTO.getVoteCount() < 150 || movieInfoDTO.getVoteCount() == null) {
                        log.info("Movie has less than 150 votes, checking for TV show info");
                        MovieInfoDTO tvInfo = getTVInfo(movie);

                        if (tvInfo != null) {
                            // Log TV info details
                            log.info("TV info found: " + tvInfo.toString());

                            // Check if TV show title is similar to the movie title
                            if (isSimilarTitle(tvInfo.getTitle(), movie) ||
                                    isSimilarTitle(tvInfo.getTitle(), movieInfoDTO.getTitle())) {
                                // If TV show is more popular than the movie, replace movie info with TV info
                                if (movieInfoDTO.getVoteCount() != 0 && (tvInfo.getVoteCount() / movieInfoDTO.getVoteCount()) > 2) {
                                    movieInfoDTO = tvInfo;
                                }
                            }
                        }
                    }

                    // Check for required movie info fields before returning
                    if (isCompleteMovieInfo(movieInfoDTO)) {
                        log.info("Final MovieInfoDTO: " + movieInfoDTO.toString());
                        return movieInfoDTO;
                    } else {
                        log.info("MovieInfoDTO has incomplete info: " + movieInfoDTO.toString());
                    }
                }
            } else {
                log.error("Error in getting movie data from server");
            }
        } catch (Exception e) {
            log.error("An error occurred", e);
            throw new RuntimeException(e); // Rethrow the exception
        }
        return null;
    }

    private static boolean isSimilarTitle(String title1, String title2) {
        if (title1 == null || title2 == null) {
            return false;
        }
        return title1.toLowerCase().trim().contains(title2.toLowerCase().trim());
    }

    private static boolean isCompleteMovieInfo(MovieInfoDTO movieInfoDTO) {
        return movieInfoDTO.getPosterUrl() != null && movieInfoDTO.getYear() != 0;
    }

    //get information about tv movies
    public static MovieInfoDTO getTVInfo(String tvName) throws IOException, JSONException {
        Response response = getMovieData(tvName, "tv");
        String responseBodyString = response.body().string();
        DataExtractor extractor = new DataExtractor();
        MovieInfoDTO tvInfo = (MovieInfoDTO) extractor.extract(responseBodyString, photoResolution, MovieOrTv.TV);
        return tvInfo;
    }

    ;


    ////////////////////////////////////////////////////////////////
//get response of a movie data and tv show data
    private static Response getMovieData(String movieName, String type) throws IOException {
        String movieUrlType = "";
        try {
            try {
                movieUrlType = URLEncoder.encode(movieName, "UTF-8");
            } catch (IOException e) {
                throw new IOException("Unsupported encoding: " + e.getMessage());
            }
            // movie or tv show
            String tvOrShow = null;
            if (type.equalsIgnoreCase("movie")) {
                tvOrShow = "https://api.themoviedb.org/3/search/movie?query=" + movieUrlType + "&include_adult=false&language=en-US&page=1";


            } else if (type.equalsIgnoreCase("tv")) {
                tvOrShow = "https://api.themoviedb.org/3/search/tv?query=" + movieUrlType + "&include_adult=false&language=en-US&page=1";
            }

            Request request = new Request.Builder()
                    .url(tvOrShow)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", ("Bearer " + tokenKey))
                    .build();

            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /////////////////////////////////////////////////////////////////part ----> popularMovies

    //get a response popularMovies


    // Method to fetch popular movies data
    public static String popularMovies() {
//        client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(6, TimeUnit.SECONDS).build();
        DefaultData defaultData = new DefaultData();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + tokenKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                log.info("Request was successful");
                ResponseBody body = response.body();
                if (body != null) {
                    log.info("Body is not null");
                    return body.string();
                } else {
                    log.warn("Response body is null, returning default data");
                    return defaultData.popularMovies();
                }
            } else {
                log.error("Popular movies request failed with code: " + response.code());
                return defaultData.popularMovies();
            }
        } catch (IOException e) {
            log.error("Failed to fetch popular movies so we return defalut value: " + e.getMessage(), e);
            return defaultData.popularMovies();
        }
    }

    //get popular movies as a list
//    @Cacheable(value = "popularList", key = "popularMoviesList")
    public LinkedList<MovieInfoDTO> popularMoviesList() throws IOException, JSONException {
        log.info("getting popular movies list");
        DefaultData defaultData = new DefaultData();
        LinkedList<MovieInfoDTO> movieInfoDTOS = null;
        DataExtractor dataExtractor = new DataExtractor();
        try {
            try {

                String responseBody = curdService.getVitrinMovieInfo("popularMovies").getApiResult();

                movieInfoDTOS = (LinkedList<MovieInfoDTO>) dataExtractor.extract(responseBody, photoResolution, MovieOrTv.POPULAR);
            } catch (JSONException e) {
                log.error("Exception occurred while fetching popular movies list", e);
                throw e;
            }
            log.info("updateing popular movies list");

        } catch (JSONException e) {

            throw new RuntimeException(e);
        }
        return movieInfoDTOS;
    }


}