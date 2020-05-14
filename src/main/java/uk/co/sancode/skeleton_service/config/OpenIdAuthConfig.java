package uk.co.sancode.skeleton_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.sancode.skeleton_service.utilities.OpenIdUrlBuilder;
import uk.co.sancode.skeleton_service.utilities.OpenIdUrlConfig;
import uk.co.sancode.skeleton_service.utilities.OpenIdUrlType;

@Configuration
public class OpenIdAuthConfig {

    @Bean
    @ConfigurationProperties(prefix = "openid.auth")
    public OpenIdUrlConfig openIdUrlConfig() {
        return new OpenIdUrlConfig();
    }

    @Bean
    public OpenIdUrlBuilder openIdLogoutUrlBuilder(final @Autowired OpenIdUrlConfig openIdUrlConfig) {
        return new OpenIdUrlBuilder(openIdUrlConfig).withType(OpenIdUrlType.LOGOUT);
    }
}
