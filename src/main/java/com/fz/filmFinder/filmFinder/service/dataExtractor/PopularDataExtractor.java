package com.fz.filmFinder.filmFinder.service.dataExtractor;

import com.fz.filmFinder.filmFinder.model.DTO.MovieInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

@Slf4j // Lombok annotation for logging
public class PopularDataExtractor {

    /**
     * Extracts movie information from a JSON response and returns a list of MovieInfoDTO objects.
     *
     * @param responseBody    The JSON response string containing movie data.
     * @param photoResolution The desired resolution for movie poster images.
     * @return A LinkedList containing MovieInfoDTO objects for each movie, or an empty list if no movies are found or an error occurs.
     */
    public LinkedList<MovieInfoDTO> extractMovieData(String responseBody, String photoResolution) {
        LinkedList<MovieInfoDTO> moviesInfo = new LinkedList<>();

        try {
            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray results = jsonResponse.getJSONArray("results");

            // Loop through each movie in the results array
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);

                // Extract and parse the release year
                String dateStr = movie.getString("release_date");
                int year = 0; // Default value if year is not available
                if (!dateStr.isEmpty()) {
                    String[] dateParts = dateStr.split("-");
                    if (dateParts.length > 0) { // Ensure year is present
                        year = Integer.parseInt(dateParts[0]);
                    }
                }

                // Build MovieInfoDTO object using the builder pattern
                MovieInfoDTO movieInfoDTO = new MovieInfoDTO()
                        .title(movie.getString("original_title"))
                        .year(year)
                        .posterUrl("https://image.tmdb.org/t/p/w" + photoResolution + movie.getString("poster_path"))
                        .voteCount(movie.getLong("vote_count"))
                        .build();

                // Add the movie to the list
                moviesInfo.add(movieInfoDTO);
            }

        } catch (JSONException e) {
            // Log the error
            log.error("Error while creating popular movie list: {}", e.getMessage());
            // Optionally, rethrow the exception or handle it in another way.
        }

        return moviesInfo;
    }
}
