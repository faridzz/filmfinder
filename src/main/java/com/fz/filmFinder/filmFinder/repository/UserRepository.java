//package com.fz.onlineshoestore.repository;
//
//import com.fz.onlineshoestore.model.UserObj;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;
//
//import java.util.Optional;
//
//public interface UserRepository extends CrudRepository<UserObj, Long> {
//    UserObj findByUsername(String username);
//    boolean existsByUsername(String username);
//    boolean existsByEmail (String email);
//}
