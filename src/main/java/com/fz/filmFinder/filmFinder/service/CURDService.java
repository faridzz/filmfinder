package com.fz.filmFinder.filmFinder.service;

import com.fz.filmFinder.filmFinder.model.MovieInfo;
import com.fz.filmFinder.filmFinder.model.VitrinMovieInfo;
import com.fz.filmFinder.filmFinder.repository.MovieInfoRepository;
import com.fz.filmFinder.filmFinder.repository.VitrinMovieInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CURDService {

    @Autowired
    private MovieInfoRepository movieInfoRepository;
    @Autowired
    private VitrinMovieInfoRepository vitrinMovieInfoRepository;

    CURDService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public void addMovieInfoToDatabase(MovieInfo movieInfo) {
        log.info(movieInfo.toString());
        movieInfoRepository.save(movieInfo);
    }

    //get movie information (including similar movies)
    public MovieInfo getMovieInfoFromDatabase(String movieName) {
        return movieInfoRepository.findByTitle(movieName);
    }

    public Iterable<MovieInfo> findAll() {
        return movieInfoRepository.findAll();
    }

    public void addAVitrineMovie(String vitrineName, String apiResult) {
        VitrinMovieInfo vitrinMovieInfo = new VitrinMovieInfo(vitrineName, apiResult);
        try {
            vitrinMovieInfoRepository.save(vitrinMovieInfo);
            log.info("{}add successfully", vitrinMovieInfo.getForWhat());
        } catch (Exception e) {
            log.warn("{}not add successfully", vitrinMovieInfo.getForWhat());
            throw new RuntimeException(e);

        }
    }

    public VitrinMovieInfo getVitrinMovieInfo(String vitrineName) {
        try {
            VitrinMovieInfo vitrinMovieInfo = vitrinMovieInfoRepository.findByForWhat(vitrineName);
            if (vitrinMovieInfo != null) {
                log.info("Vitrine : {}", vitrineName);
                return vitrinMovieInfo;
            } else {
                log.info("vitrineMovieInfo not found");
                return null;
            }
        } catch (Exception e) {
            log.warn("cant not find vitrine:  {}", vitrineName);
            throw new RuntimeException(e);
        }
    }
}
