package com.fz.filmFinder.filmFinder.service;

import com.fz.filmFinder.filmFinder.model.DTO.MovieInfoDTO;
import com.fz.filmFinder.filmFinder.model.MovieInfo;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CreateAMovieInfo {

    public MovieInfo createMovieInfo(MovieInfoDTO theMovie, List<MovieInfoDTO> similarMovies) {
        // Use the builder to create an instance of MovieInfo
        MovieInfo myMovieInfo = new MovieInfo.Builder()
                .title(theMovie.getTitle())
                .year(theMovie.getYear())
                .imgLink(theMovie.getPosterUrl())
                .movieOrTv(theMovie.getMovieOrTv())
                .build();

        // Create a list for similar movies
        List<MovieInfo> similarMoviesList = new LinkedList<>();

        // Convert list of DTOs to list of model entities
        for (MovieInfoDTO dto : similarMovies) {
            similarMoviesList.add(dto.movieInfoDtoToMovieInfo());
        }

        // Add the similar movies list to the MovieInfo instance
        myMovieInfo.setRelatedMovies(similarMoviesList);

        return myMovieInfo;
    }
}
