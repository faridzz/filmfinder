package com.fz.filmFinder.filmFinder.controller;

import com.fz.filmFinder.filmFinder.model.DTO.MovieInfoDTO;
import com.fz.filmFinder.filmFinder.model.MovieInfo;
import com.fz.filmFinder.filmFinder.repository.MovieInfoRepository;
import com.fz.filmFinder.filmFinder.service.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("")
@Slf4j
public class Finder {
    FinderApiService finderApiService;
    TmdbApiService tmdbApiService;
    ImdbDataService imdbDataService;
    MovieInfoRepository movieInfoRepository;
    CreateAMovieInfo createAMovieInfo;
    CURDService curdService;

    public Finder(FinderApiService finderApiService,
                  ImdbDataService imdbDataService,
                  TmdbApiService tmdbApiService,
                  MovieInfoRepository movieInfoRepository,
                  CreateAMovieInfo createAMovieInfo ) {
        this.tmdbApiService = tmdbApiService;
        this.imdbDataService = imdbDataService;
        this.finderApiService = finderApiService;
        this.movieInfoRepository = movieInfoRepository;
        this.createAMovieInfo = createAMovieInfo;
    }



    @GetMapping("/")
    public String home(Model model) throws JSONException, IOException {
        System.out.println("Home");
        MovieInfoDTO movieInfoDTO = new MovieInfoDTO(); // ایجاد شیء MovieInfoDTO و مقداردهی به آن

        List<MovieInfoDTO> moviesInfo = new ArrayList<>(); // ایجاد یک لیست از شیء MovieInfoDTO

//         _____________add attributes popular movies
        LinkedList<MovieInfoDTO> popularMoviesList = tmdbApiService.popularMoviesList();// create linked list for all popular movies
        model.addAttribute("popularMovies", popularMoviesList);


        //-------------- add attributes empty attributes for input movie name
        model.addAttribute("movieInfoDTO", movieInfoDTO);
        //-------------- add attributes empty attributes for movies information
        model.addAttribute("moviesInfo", moviesInfo);
        //-------------- add attributes empty attributes for movies name
        model.addAttribute("movieName", new String());
        //-------------- add attributes empty attributes for error message
        model.addAttribute("error", null);

        return "index";
    }

    @GetMapping("/finder")
    public String getMovieName(
            @PathVariable(required = false) String pathMovieName,
            @RequestParam(required = false) String title,
            @ModelAttribute MovieInfoDTO movieInfoDTO, Model model) throws JSONException, InterruptedException, IOException {
        log.info("Entering getMovieName method");

        String movieName = "";

        // Check if movieInfoDTO and its title are not null or empty
        if (movieInfoDTO != null && movieInfoDTO.getTitle() != null && !movieInfoDTO.getTitle().isEmpty()) {
            movieName = movieInfoDTO.getTitle();
        }
        // Check if pathMovieName is not null or empty
        else if (pathMovieName != null && !pathMovieName.isEmpty()) {
            log.info("Using pathMovieName");
            movieName = pathMovieName;
        }
        // Check if title parameter is not null or empty
        else if (title != null && !title.isEmpty()) {
            movieName = title;
        }

        // Retrieve the list of movies based on the movie name
        List<String> moviesList = finderApiService.getMoviesList(movieName);
        // Get complex data
        LinkedList complexData = tmdbApiService.getSmiliarListWithInfo(moviesList);
        //// Get data from TMDb API for similar movies
        LinkedList<MovieInfoDTO> similarMovieInfoDTOS = (LinkedList<MovieInfoDTO>) complexData.get(1);
        //get data of movie whose searched
        MovieInfoDTO serchedMovieInfoDTO = (MovieInfoDTO) complexData.get(0);
        ////////////////////////////////////////////////////////////////
        // If the list of similar movies is less than 2, add a "movie not found" entry
        if (similarMovieInfoDTOS.size() < 2) {
            MovieInfoDTO movieNotFound = new MovieInfoDTO("چیزی رو پیدا نکردیم یا شایدم سایت مشکلی پیدا کرده ببخشی :(", "./img/notFound.jpeg", 404, "");
            similarMovieInfoDTOS.add(movieNotFound);
        }
        //add data to database or not
        if (similarMovieInfoDTOS.size() > 2) {
//            log.info("adding data to database");
//            MovieInfo movieInfo = creatAMovieInfo.createMovieInfo(serchedMovieInfoDTO , similarMovieInfoDTOS);
//            curdService.addMovieInfoToDatabase(movieInfo);
        }
        // Add the list of similar movies to the model
        model.addAttribute("moviesInfo", similarMovieInfoDTOS);

        // Retrieve the list of popular movies
        LinkedList<MovieInfoDTO> popularMoviesList = tmdbApiService.popularMoviesList();

        // Add the list of popular movies to the model
        model.addAttribute("popularMovies", popularMoviesList);

        // Add the movie name to the model
        model.addAttribute("movieName", movieName);

        // Return the index view
        return "index";
    }

    @GetMapping("/404")
    public String get404() {
        return "404";
    }

}
