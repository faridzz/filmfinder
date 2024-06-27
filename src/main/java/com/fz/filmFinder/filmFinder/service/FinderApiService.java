package com.fz.filmFinder.filmFinder.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FinderApiService {

    private final FindMovieMapService findMovieMapService;

    public FinderApiService(FindMovieMapService findMovieMapService) {
        this.findMovieMapService = findMovieMapService;
    }

    public static void main(String[] args) {
        FindMovieMapService findMovieMapService = new FindMovieMapService();
        FinderApiService finderApiService = new FinderApiService(findMovieMapService);
        finderApiService.getMoviesList("dark");

    }

    public List<String> getMoviesList(String movieName) throws RuntimeException {
        // Get the HTML document for the given movie name


        try {
            Optional<Document> html = findMovieMapService.getMoviesHtml(movieName);
            // Check if the HTML document is present
            if (html.isPresent()) {
                // Extract the list of movies from the HTML document
                return findMovieMapService.moviesList(html);
            } else {
                // If HTML document is not present, return an empty list
                log.error("no same movie find or we have a error");
                return List.of("No movies found for the given movie name.");
            }
        } catch (Exception e) {
            // Throw a custom exception if any error occurs during the process
            throw new RuntimeException("An error occurred while searching for movies.", e);
        }
    }
}
