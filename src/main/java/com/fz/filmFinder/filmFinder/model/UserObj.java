//package com.fz.onlineshoestore.model;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.NonNull;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.validator.constraints.UniqueElements;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.sql.Array;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Table(name = "users")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "userId")
//public class UserObj implements UserDetails {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long userId;
//    @Column
//    @NotNull
//    private String username;
//    @Column
//    @NotNull
//    private String password;
//    @Column
//
//    @Email
//    private String email;
//    @CreationTimestamp
//    private Date creationTimestamp;
//    @ManyToMany
//    @JoinTable(name = "users_shoes",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "shoe_id"))
//    private List<Shoe> shoes;
//
//
//    public UserObj(String username, String password, String email) {
//        this.username = username;
//        this.password = password;
//        this.email = email;
//
//    }
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
