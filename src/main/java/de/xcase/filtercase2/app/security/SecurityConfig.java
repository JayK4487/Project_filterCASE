package de.xcase.filtercase2.app.security;

import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import de.xcase.filtercase2.views.ErrorView;
import de.xcase.filtercase2.views.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * All allowed url's that can be accessed without authentication.
     */
    private static final String[] ALLOWED_URLS_REGEX = {
            "/frontend/.*",
            "/VAADIN/.*"
    };

    /**
     * All allowed views that can be accessed without authentication.
     * Must use the same classes as {@link SecurityConfig#ALLOWED_CLASSES_REGEX}.
     */
    private static final Class[] ALLOWED_CLASSES = {
            LoginView.class,
            ErrorView.class
    };

    /**
     * All allowed view as their regex entry.
     * Must use the same classes as {@link SecurityConfig#ALLOWED_CLASSES}.
     */
    private static final String[] ALLOWED_CLASSES_REGEX = {
            //"/$", //Root view has special regex
            "/" + LoginView.VIEW_NAME + ".*",
            "/" + ErrorView.VIEW_NAME + ".*"

    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable() //csrf handled by vaadin
                .exceptionHandling()
                    .accessDeniedPage("/" + ErrorView.VIEW_NAME) //set error page
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/" + LoginView.VIEW_NAME))
                .and()
                    .logout()
                    .logoutSuccessUrl("/" + LoginView.VIEW_NAME)
                .and()
                    .authorizeRequests()
                    //allow Vaadin URLs and the views without authentication
                    .regexMatchers(ALLOWED_URLS_REGEX)
                        .permitAll()
                    .regexMatchers(ALLOWED_CLASSES_REGEX)
                        .permitAll()
                    .requestMatchers(this::isFrameworkInternalRequest)
                        .permitAll()
                    // deny any other URL until authenticated
                    .anyRequest()
                        .authenticated();
        /*
             Note that anonymous authentication is enabled by default, therefore;
             SecurityContextHolder.getContext().getAuthentication().isAuthenticated() always will return true.
             Look at LoginView.beforeEnter method.
             more info: https://docs.spring.io/spring-security/site/docs/4.0.x/reference/html/anonymous.html
             */
    }
    /**
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider =
                new ActiveDirectoryLdapAuthenticationProvider(
                        "x-case.local",
                        "ldap://192.168.108.21:389",
                        "OU=SBSUsers,OU=Users,OU=MyBusiness,DC=x-case,DC=local"
                );

        activeDirectoryLdapAuthenticationProvider.setConvertSubErrorCodesToExceptions(true);
        auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider);
    }
    **/
    /**
     * Checks if a class is a public navigation target.
     *
     * @param target the corresponding class of the navigation target
     * @return false if the class is part of the {@link SecurityConfig#ALLOWED_CLASSES}
     */
    public static Boolean isAuthenticationRequired(final Class target) {
        return !Arrays.asList(ALLOWED_CLASSES).contains(target);
    }

    /**
     * Expose the AuthenticationManager (to be used in login)
     *
     * @return the authentication manager
     * @throws Exception if authentication manager can't be returned
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // user and pass: admin
        // TODO Remove hardcoded login
        auth
                .inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin").password("$2a$10$obstjyWMAVfsNoKisfyCjO/DNfO9OoMOKNt5a6GRlVS7XNUzYuUbO").authorities("X-CASE-Benutzer");
    }

    /**
     * Tests if the request is an internal framework request. The test consists of
     * checking if the request parameter is present and if its value is consistent
     * with any of the request types know.
     *
     * @param request {@link HttpServletRequest}
     * @return true if is an internal framework request. False otherwise.
     */
    private boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(ServletHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    /**
    @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        return new ActiveDirectoryLdapAuthenticationProvider(null, "ldap://192.168.108.21:389/");
    }
    **/

}
