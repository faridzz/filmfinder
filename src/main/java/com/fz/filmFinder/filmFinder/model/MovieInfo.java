package com.fz.filmFinder.filmFinder.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class MovieInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column
    private int year;

    @Column(name = "image_link")
    private String imgLink;

    @Column
    private String movieOrTv;

    @CreationTimestamp
    @Column(name = "creation_timestamp", updatable = false)
    private Date creationTimestamp;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_related_movies",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "related_movie_id")
    )
    private List<MovieInfo> relatedMovies;

    public static class Builder {
        private String title;
        private int year;
        private String imgLink;
        private String movieOrTv;
        private List<MovieInfo> relatedMovies;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder imgLink(String imgLink) {
            this.imgLink = imgLink;
            return this;
        }

        public Builder movieOrTv(String movieOrTv) {
            this.movieOrTv = movieOrTv;
            return this;
        }

        public Builder relatedMovies(List<MovieInfo> relatedMovies) {
            this.relatedMovies = relatedMovies;
            return this;
        }

        public MovieInfo build() {
            MovieInfo movieInfo = new MovieInfo();
            movieInfo.title = this.title;
            movieInfo.year = this.year;
            movieInfo.imgLink = this.imgLink;
            movieInfo.movieOrTv = this.movieOrTv;
            movieInfo.relatedMovies = this.relatedMovies;
            return movieInfo;
        }
    }

    public MovieInfo(String title, int year, String imgLink, String movieOrTv) {
        this.title = title;
        this.year = year;
        this.imgLink = imgLink;
        this.movieOrTv = movieOrTv;
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", imgLink='" + imgLink + '\'' +
                ", movieOrTv='" + movieOrTv + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
}
