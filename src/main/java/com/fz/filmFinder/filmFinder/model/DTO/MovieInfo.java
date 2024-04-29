package com.fz.filmFinder.filmFinder.model.DTO;

import lombok.Data;

@Data
public class MovieInfo {

    private String title;
    private int year;
    private String imdbId;
    private int rank;
    private String actors;
    private String aka;
    private String imdbUrl;
    private String imdbIv;
    private String posterUrl;
    private int posterWidth;
    private int posterHeight;


}
