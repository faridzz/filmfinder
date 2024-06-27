package com.fz.filmFinder.filmFinder.repository;

import com.fz.filmFinder.filmFinder.model.MovieInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieInfoRepository extends JpaRepository<MovieInfo , Long> {
        MovieInfo findByTitle(String title);

}
