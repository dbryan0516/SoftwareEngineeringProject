package edu.ncsu.csc.itrust2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

/**
 * Class that manages which users are allowed to access the system and which
 * role they have. Different users are allowed to have different roles which can
 * be enforced by the different controllers to restrict access to only users of
 * the correct type.
 *
 *
 * @author Kai Presler-Marshall
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * DataSource used for connecting to and interacting with the database.
     */
    @Autowired
    DataSource dataSource;

    /**
     * Login configuration for iTrust2.
     *
     * @param auth
     *            AuthenticationManagerBuilder to use to configure the
     *            Authentication.
     */
    @Autowired
    public void configureGlobal ( final AuthenticationManagerBuilder auth ) {
        auth.authenticationEventPublisher( defaultAuthenticationEventPublisher() );
        auth.authenticationProvider( authenticationProvider() );
    }

    /**
     * Method responsible for the Login page. Can be extended to explicitly
     * override other automatic functionality as desired.
     */
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {

        final String[] patterns = new String[] { "/login*" };

        http.authorizeRequests().antMatchers( patterns ).anonymous().anyRequest().authenticated().and().formLogin()
                .loginPage( "/login" ).defaultSuccessUrl( "/" ).and().csrf()
                .csrfTokenRepository( CookieCsrfTokenRepository
                        .withHttpOnlyFalse() ); /*
                                                 * Credit to
                                                 * https://medium.com/spektrakel
                                                 * -blog/angular2-and-spring-a-
                                                 * friend-in-
                                                 * security-need-is-a-friend-
                                                 * against-csrf-indeed-
                                                 * 9f83eaa9ca2e and
                                                 * http://docs.spring.io/spring-
                                                 * security/site/docs/current/
                                                 * reference/
                                                 * html/csrf.html#csrf-cookie
                                                 * for information on how to
                                                 * make Angular work properly
                                                 * with CSRF protection
                                                 */

    }

    /**
     * Bean used to generate a PasswordEncoder to hash the user-provided
     * password.
     *
     * @return The password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationEventPublisher used to assist with authentication
     *
     * @return The AuthenticationEventPublisher.
     */
    @Bean
    public DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher () {
        return new DefaultAuthenticationEventPublisher();
    }

    private AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }
}
