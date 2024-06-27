package com.fz.filmFinder.filmFinder.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fz.filmFinder.filmFinder.model.DTO.MovieInfoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vitrin_movie_Info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VitrinMovieInfo {
    @Id
    @Column(nullable = false)
    @NotNull
    private String forWhat;
    @Column(length = 20000)
    private String apiResult;
    @CreationTimestamp
    @Column(name = "creation_timestamp", updatable = false)
    private Date creationTimestamp;
    @UpdateTimestamp
    private Date updateTimestamp;


    public VitrinMovieInfo(String forWhat, String apiResult) {
        this.forWhat = forWhat;
        this.apiResult = apiResult;
    }
}
