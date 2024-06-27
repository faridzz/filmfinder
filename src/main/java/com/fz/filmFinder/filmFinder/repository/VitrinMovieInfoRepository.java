package com.fz.filmFinder.filmFinder.repository;

import com.fz.filmFinder.filmFinder.model.VitrinMovieInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VitrinMovieInfoRepository extends JpaRepository<VitrinMovieInfo , String> {
    VitrinMovieInfo findByForWhat(String forWhat);
}
