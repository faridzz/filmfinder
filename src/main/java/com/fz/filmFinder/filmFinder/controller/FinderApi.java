//package com.fz.filmFinder.filmFinder.controller;
//
//
//import com.fz.filmFinder.filmFinder.service.FinderApiService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/api/v1/finder")
//public class FinderApi {
//    FinderApiService finderApiService;
//    public FinderApi(FinderApiService finderApiService){
//        this.finderApiService = finderApiService;
//    }
//
//    @GetMapping("/movieMap/{movieName}")
//    public List<String> getMovieMap(@PathVariable String movieName) {
//        return finderApiService.getMoviesList(movieName);
//    }
//}
