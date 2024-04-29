package com.fz.filmFinder.filmFinder.service;

import com.fz.filmFinder.filmFinder.model.DTO.MovieInfo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ImdbDataService {

    /*
    public static void main(String[] args) throws JSONException {
        // تست تابع getImdbData با فیلم‌های مختلف
        List<MovieInfo> moviesData = getImdbData(Arrays.asList("dark"));
        System.out.println("Movies data: " + moviesData);
    }
    */

    public static LinkedList<MovieInfo> getImdbData(List<String> moviesList) throws JSONException, InterruptedException {
        //لیستی از ابجکت فیلم ها
        LinkedList<MovieInfo> moviesDataList = new LinkedList<>();
        //
        int numThreads = 30; // Number of threads to create
        ThreadPoolExecutor executor = new ThreadPoolExecutor(numThreads, numThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        //


        for (String movie : moviesList) {
            executor.execute(() -> {
                try {
                    JSONObject jsonObject = sendHttpRequest(movie);
                    JSONArray descriptionArray = jsonObject.getJSONArray("description");
                    if (descriptionArray.length() > 2) {
                        JSONObject firstDescription = descriptionArray.getJSONObject(0);
                        MovieInfo movieData = extractMovieData(firstDescription);
                        moviesDataList.add(movieData);
                    }
                    log.info("Movie data: " + movie + " added");
                } catch (Exception e) {
                    log.error("Error processing movie: " + movie, e);
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        log.info("done");
        return moviesDataList;
    }

    private static JSONObject sendHttpRequest(String movieName) throws IOException {
        String movieUrlType;
        try {
            movieUrlType = URLEncoder.encode(movieName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IOException("Unsupported encoding: " + e.getMessage());
        }

        String url = "https://search.imdbot.workers.dev/?q=" + movieUrlType;
//        log.info("URL: " + url);

        Document response;
        try {
            response = Jsoup.connect(url).ignoreContentType(true).get();
        } catch (IOException e) {
            throw new IOException("Failed to connect to the server: " + e.getMessage());
        }

        if (response != null) {
            // با استفاده از body().text()، متن پاسخ را دریافت کنید
            String jsonData = response.body().text();
            // log.info("Response: " + jsonData);  // این خط کامنت شده است چرا که از آن استفاده نمی‌شود

            try {
                // تبدیل متن JSON به یک شیء JSONObject
                return new JSONObject(jsonData);
            } catch (JSONException e) {
                throw new IOException("Failed to parse JSON data: " + e.getMessage());
            }
        } else {
            throw new IOException("No response received from the server.");
        }
    }

    private static MovieInfo extractMovieData(JSONObject firstDescription) throws JSONException {
        MovieInfo movieInfo = new MovieInfo();
        movieInfo.setTitle(firstDescription.getString("#TITLE"));
        movieInfo.setYear(firstDescription.getInt("#YEAR"));
        movieInfo.setImdbId(firstDescription.getString("#IMDB_ID"));
        movieInfo.setRank(firstDescription.getInt("#RANK"));
        movieInfo.setActors(firstDescription.getString("#ACTORS"));
        movieInfo.setAka(firstDescription.getString("#AKA"));
        movieInfo.setImdbUrl(firstDescription.getString("#IMDB_URL"));
        movieInfo.setImdbIv(firstDescription.getString("#IMDB_IV"));
        movieInfo.setPosterUrl(firstDescription.getString("#IMG_POSTER"));
        movieInfo.setPosterWidth(firstDescription.getInt("photo_width"));
        movieInfo.setPosterHeight(firstDescription.getInt("photo_height"));
        return movieInfo;
    }
}
