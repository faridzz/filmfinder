package com.fz.filmFinder.filmFinder.model.DTO;

import com.fz.filmFinder.filmFinder.model.MovieInfo;
import lombok.Data;

@Data
public class MovieInfoDTO {


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
    private Long voteCount;
    private String movieOrTv;

    public MovieInfoDTO(){}
    public MovieInfoDTO(String title, String posterUrl , int year , String movieOrTv){
        this.title = title;
        this.posterUrl = posterUrl;
        this.year = year;
        this.movieOrTv = movieOrTv;
    }

    public MovieInfo movieInfoDtoToMovieInfo(){
        return new MovieInfo(this.title,this.year, this.posterUrl,this.movieOrTv);
    }
    // made a builder designed
    public MovieInfoDTO title(String title) {
        this.title = title;
        return this;
    }

    public MovieInfoDTO year(int year) {
        this.year = year;
        return this;
    }

    public MovieInfoDTO imdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    public MovieInfoDTO rank(int rank) {
        this.rank = rank;
        return this;
    }

    public MovieInfoDTO actors(String actors) {
        this.actors = actors;
        return this;
    }

    public MovieInfoDTO aka(String aka) {
        this.aka = aka;
        return this;
    }

    public MovieInfoDTO imdbUrl(String imdbUrl) {
        this.imdbUrl = imdbUrl;
        return this;
    }

    public MovieInfoDTO imdbIv(String imdbIv) {
        this.imdbIv = imdbIv;
        return this;
    }

    public MovieInfoDTO posterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
        return this;
    }

    public MovieInfoDTO posterWidth(int posterWidth) {
        this.posterWidth = posterWidth;
        return this;
    }

    public MovieInfoDTO posterHeight(int posterHeight) {
        this.posterHeight = posterHeight;
        return this;
    }

    public MovieInfoDTO voteCount(Long voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public MovieInfoDTO movieOrTv(String movieOrTv) {
        this.movieOrTv = movieOrTv;
        return this;
    }
    public MovieInfoDTO build() {
        MovieInfoDTO movieInfo = new MovieInfoDTO();
        movieInfo.setTitle(this.title);
        movieInfo.setYear(this.year);
        movieInfo.setImdbId(this.imdbId);
        movieInfo.setRank(this.rank);
        movieInfo.setActors(this.actors);
        movieInfo.setAka(this.aka);
        movieInfo.setImdbUrl(this.imdbUrl);
        movieInfo.setImdbIv(this.imdbIv);
        movieInfo.setPosterUrl(this.posterUrl);
        movieInfo.setPosterWidth(this.posterWidth);
        movieInfo.setPosterHeight(this.posterHeight);
        movieInfo.setVoteCount(this.voteCount);
        movieInfo.setMovieOrTv(this.movieOrTv);
        return movieInfo;
    }



}
