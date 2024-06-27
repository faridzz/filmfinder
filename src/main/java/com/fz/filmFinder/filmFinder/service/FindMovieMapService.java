package com.fz.filmFinder.filmFinder.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FindMovieMapService {
    public static void main(String[] args) {
        FindMovieMapService service = new FindMovieMapService();
        service.getMoviesHtml("dark");
    }

    // Constants for better readability and maintainability
    private static final String BASE_URL = "https://www.movie-map.com/";
    private static final String GNOD_MAP_ID = "#gnodMap";
    private static final String SUB_ELEMENT_CLASS = ".S";

    // Retry logic (if needed)
    @Retryable(value = IOException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public Optional<Document> getMoviesHtml(String movieName) {
        try {
            String encodedMovieName = URLEncoder.encode(movieName, "UTF-8");
            String url = BASE_URL + encodedMovieName;
            log.info("Fetching HTML from: {}", url);

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15")
                    .get();
            log.debug("HTML content retrieved successfully."); // Log at debug level
            return Optional.of(doc);
        } catch (IOException e) {
            log.error("Error fetching HTML for movie '{}': {}", movieName, e.getMessage());
            return Optional.empty();
        }
    }

    public List<String> moviesList(Optional<Document> html) {
        List<String> movieTitles = new ArrayList<>();
        if (html.isPresent()) {
            Document document = html.get();
            Element gnodMap = document.selectFirst(GNOD_MAP_ID);
            if (gnodMap != null) {
                Elements subElements = gnodMap.select(SUB_ELEMENT_CLASS);
                for (Element subElement : subElements) {
                    movieTitles.add(subElement.text());
                }
                log.info("Found {} similar movie titles.", movieTitles.size());
            } else {
                log.warn("Element with ID '{}' not found in the HTML.", GNOD_MAP_ID);
            }
        } else {
            log.warn("No HTML document provided.");
        }
        return movieTitles;
    }
}
