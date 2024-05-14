package com.hendisantika.config;

import com.hendisantika.auth.handler.LoginSuccessHandler;
import com.hendisantika.service.JPAUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-invoice-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 15/09/21
 * Time: 05.34
 */
@Configuration
// Anotasi ini digunakan untuk mengaktifkan keamanan berbasis metode dalam aplikasi Spring.
// Anotasi ini untuk mengonfigurasi fitur keamanan berbasis metode seperti mengamankan metode atau class
// dengan menggunakan anotasi seperti @PreAuthorize, @PostAuthorize, @Secured, dll.
// Digunakan ketika memerlukan kontrol akses yang halus terhadap metode atau class individual dalam aplikasi.
@EnableMethodSecurity
// Anotasi ini digunakan untuk mengaktifkan keamanan web dalam aplikasi Spring Boot
// Anotasi ini memungkinkan Anda untuk mengkonfigurasi fitur keamanan web seperti autentikasi, otorisasi, perlindungan CSRF,
// pengelolaan sesi, dll.Digunakan ketika Anda perlu mengamankan titik akhir web dalam aplikasi Anda.
@EnableWebSecurity
// Tergantung pada kebutuhan, mungkin perlu menggunakan salah satu atau kedua anotasi ini dalam konfigurasi
// Spring Security.
public class SpringSecurityConfig {

    private final LoginSuccessHandler successHandler;
//    private final DataSource dataSource;
//    private final BCryptPasswordEncoder passwordEncoder;
    private final JPAUserDetailsService userDetailsService;

    public SpringSecurityConfig(LoginSuccessHandler successHandler, JPAUserDetailsService userDetailsService) {
        this.successHandler = successHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        //Here we specify the roles required to access
        //each route. The "long" way is to specify each path access here,
        //but it can also be done directly in the controller, using
        //the @Secured (role) annotation on each method
        // Set permissions on endpoints
        http.authorizeHttpRequests(
                // our public endpoints
                c -> c.requestMatchers("/", "/h2-console/**", "/css/**", "/js/**", "/img/**", "/clients",
                                "/locale").permitAll()
                        // our private endpoints
//                        .requestMatchers("/ver/**").hasAnyRole("USER")
//                        .requestMatchers("/uploads/**").hasAnyRole("USER")
//                        .requestMatchers("/form/**").hasAnyRole("ADMIN")
//                        .requestMatchers("/remove/**").hasAnyRole("ADMIN")
//                        .requestMatchers("/invoice/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
        );

        http.userDetailsService(userDetailsService)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .httpBasic(Customizer.withDefaults());

        http.formLogin(c -> c.successHandler(successHandler)
                //The .loginPage (path) method serves //to tell Spring which path to use
                .loginPage("/login").permitAll()
        );

        http.logout(c -> c.permitAll());
        http.exceptionHandling(c -> c.accessDeniedPage("/error_403"));
        return http.build();
    }

//    public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {
        //This code allows to implement users "in memory"
        //UserBuilder users = User.withDefaultPasswordEncoder(); //Spring Boot 1.5.10
		/*PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); //Spring Boot 2
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);	//Spring Boot 2
		build.inMemoryAuthentication()
		.withUser(
				users.username("admin")
				.password("admin")
				.roles("ADMIN", "USER"))
		.withUser(
				users.username("cristian")
				.password("cristian")
				.roles("USER"));*/

        //This code allows to implement users with database through JDBC
        //You have to indicate the queries to execute to login and obtain the user roles
		/*build.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("SELECT USERNAME, PASSWORD, ENABLED FROM USERS WHERE USERNAME=?")
		.authoritiesByUsernameQuery(
				"SELECT U.USERNAME, A.AUTHORITY "
						+ "FROM AUTHORITIES A INNER JOIN USERS U "
						+ "ON (A.USER_ID = U.ID) "
						+ "WHERE U.USERNAME=?");*/

        //This code allows to implement users with database through JPA.
//        build.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }

}
