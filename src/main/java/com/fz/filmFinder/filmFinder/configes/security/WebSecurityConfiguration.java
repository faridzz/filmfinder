//package com.fz.onlineshoestore.configs.securityConfig;
//
//
//import com.fz.onlineshoestore.model.UserObj;
//import com.fz.onlineshoestore.repository.UserRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.ObjectPostProcessor;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfiguration {
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    UserDetailsService userDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return username -> {
//            UserObj user = userRepository.findByUsername(username);
//            if (user.getUsername().contains("admin") || user.getUsername() == "admin") {
//                UserDetails userBuilder = User.builder().username(user.getUsername())
//                        .password(user.getPassword())
//                        .roles("ADMIN").build();
//                return userBuilder;
//            } else if (user != null) {
//                UserDetails userBuilder = User.builder().username(user.getUsername())
//                        .password(user.getPassword())
//                        .roles("USER").build();
//
//                return userBuilder;
//            }
//            throw new UsernameNotFoundException("user " + username + " not found");
//        };
//
//    }
//
//    //    @Bean
////    public UserDetailsService userDetailsService() throws Exception {
////        User.UserBuilder users = User.withDefaultPasswordEncoder();
////        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
////        manager.createUser(users.username("admin").password("password").roles("USER", "ADMIN").build());
////        return manager;
////
////    }
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(csrf -> csrf.disable())
//                .authorizeRequests(authorize -> authorize
//                                .requestMatchers(("/api/registrations")).permitAll()
////                        .requestMatchers("/signin","/signin/").permitAll()
//                                .requestMatchers("/api/public/**").permitAll()
//                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
//                                .requestMatchers("/login").permitAll()
//                                .requestMatchers("/api/registrations").permitAll()
//                                .requestMatchers("/success").hasAnyRole("USER", "ADMIN")
//
//                )
//                .formLogin
//                        (form -> form.loginProcessingUrl("/login")
//                                .defaultSuccessUrl("/success")
//                                .permitAll())
//                .sessionManagement
//                        (session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
//
//
////                        )
//                .logout(logout -> logout
//                        .deleteCookies("JSESSIONID")
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login")
//                        .permitAll())
//
//        ;
//
//        return http.build();
//    }
//
//}