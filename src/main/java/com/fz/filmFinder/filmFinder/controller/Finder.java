package com.fz.filmFinder.filmFinder.controller;

import com.fz.filmFinder.filmFinder.model.DTO.MovieInfo;
import com.fz.filmFinder.filmFinder.service.FinderApiService;
import com.fz.filmFinder.filmFinder.service.ImdbDataService;
import com.fz.filmFinder.filmFinder.service.TmdbApiService;
import okhttp3.Response;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/")
public class Finder {
    FinderApiService finderApiService;
    TmdbApiService tmdbApiService;
    ImdbDataService imdbDataService;

    public Finder(FinderApiService finderApiService, ImdbDataService imdbDataService, TmdbApiService tmdbApiService) {
        this.tmdbApiService = tmdbApiService;
        this.imdbDataService = imdbDataService;
        this.finderApiService = finderApiService;
    }


    @GetMapping("/")
    public String home(Model model) throws JSONException, IOException {
        MovieInfo movieInfo = new MovieInfo(); // ایجاد شیء MovieInfo و مقداردهی به آن

        List<MovieInfo> moviesInfo = new ArrayList<>(); // ایجاد یک لیست از شیء MovieInfo

        // _____________add attributes popular movies
        LinkedList<MovieInfo> popularMoviesList = tmdbApiService.popularMoviesList();// create linked list for all popular movies
        model.addAttribute("popularMovies", popularMoviesList);

//        model.addAttribute("popularMovies", popularMoviesList);
        //-------------- add attributes empty attributes for input movie name
        model.addAttribute("movieInfo", movieInfo);
        //-------------- add attributes empty attributes for movies information
        model.addAttribute("moviesInfo", moviesInfo);
        //-------------- add attributes empty attributes for movies name
        model.addAttribute("movieName", new String());
        //-------------- add attributes empty attributes for error message
        model.addAttribute("error", null);

        return "index";
    }

    @GetMapping("/finder")
    public String getMovieName(@ModelAttribute MovieInfo movieInfo, Model model) throws JSONException, InterruptedException, IOException {
        //movie name that user specified
        String movieName = movieInfo.getTitle();
        //list of movies maybe null maybe not

        List<String> moviesList = finderApiService.getMoviesList(movieName);


        //get data from tdmb database for similar movies
        List<MovieInfo> similarMovieInfos = tmdbApiService.getSmiliarListWithInfo(moviesList);
        model.addAttribute("moviesInfo", similarMovieInfos);
        System.out.println("from controller --------->" + similarMovieInfos.toString());

        //get data from imdb database for similar movies
        //LinkedList<MovieInfo> similarMovieInfos =  imdbDataService.getImdbData(moviesList);


        // _____________add attributes popular movies
        LinkedList<MovieInfo> popularMoviesList = tmdbApiService.popularMoviesList();// create linked list for all popular movies
////////////////////////////////////////////////////////////////
        model.addAttribute("popularMovies", popularMoviesList);

        // add attributes for movie name
        model.addAttribute("movieName", movieName);
        return "index";

    }
}
