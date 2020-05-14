package uk.co.sancode.skeleton_service.integration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uk.co.sancode.skeleton_service.utilities.OpenIdUrlBuilder;
import uk.co.sancode.skeleton_service.utilities.OpenIdUrlConfig;

@EnableWebSecurity
@Profile("!mocksecurity")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String AUDITOR_ROLE = "auditor";

    @Autowired
    private OpenIdUrlConfig openIdUrlConfig;

    @Value("${app.analytics.hash}")
    private String analyticsHash;

    @Value("${server-scheme}")
    private String scheme;

    @Value("${server-host}")
    private String host;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        String openIdLoginUrlStr = new OpenIdUrlBuilder(openIdUrlConfig).build();

        http
                .headers()
                .contentSecurityPolicy("default-src 'none'; "
                        + "base-uri 'self'; "
                        + "font-src 'self' data:; "
                        + "frame-src 'none'; "
                        + "frame-ancestors 'none'; "
                        + "img-src 'self' data: "
                        + openIdUrlConfig.getHost() + " "
                        + "https://www.google-analytics.com; "
                        + "style-src 'self'; "
                        + "script-src 'self' "
                        + "'sha256-" + analyticsHash + "='");

        http
                .cors()
                .and()
                .csrf()
                .ignoringAntMatchers("/auth-callback/**")
                .and()
                .authorizeRequests()
                .antMatchers("/" + openIdUrlConfig.getRedirectUrlPath(),
                        "/b2c",
                        "/assets/**",
                        "/goodbye/**",
                        "/actuator/poll")
                .permitAll()
                .antMatchers("/internal-dashboard")
                .hasRole(AUDITOR_ROLE)
                .antMatchers("/", "/users")
                .not()
                .hasAnyRole("ANONYMOUS", AUDITOR_ROLE)
                .anyRequest()
                .authenticated()
                .and()
                .openidLogin()
                .loginPage(openIdLoginUrlStr)
                .and()
                .logout()
                .logoutSuccessUrl(scheme + "://" + host + "/assets/images/logout.png")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }
}
