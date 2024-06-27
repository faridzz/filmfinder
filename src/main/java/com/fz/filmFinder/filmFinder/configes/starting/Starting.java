package com.fz.filmFinder.filmFinder.configes.starting;

import com.fz.filmFinder.filmFinder.service.CURDService;
import com.fz.filmFinder.filmFinder.service.TmdbApiService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Starting {
    @Autowired
    private TmdbApiService tmdbApiService;
    @Autowired
    private CURDService curdService;

    // popular movies
    @PostConstruct
    private void init() {
        updatePopularMovies();
    }

    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000)
    private void updatePopularMovies() {
        try {
            String apiResult = tmdbApiService.popularMovies();
            log.info("Popular movies..........." + apiResult.length());
            curdService.addAVitrineMovie("popularMovies", apiResult);
            log.info("Added popular movies successfully to database");
        } catch (JSONException e) {
            log.error("JSON parsing error while updating popular movies", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while updating popular movies", e);
        }
    }
}
