package uk.co.sancode.skeleton_service.utilities;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Validated
public class OpenIdUrlConfig {

    private static final int MAX_PORT_NUMBER = 65535;

    @NotBlank(message = "scheme")
    private String scheme;
    @NotBlank(message = "host")
    private String host;
    @NotBlank(message = "responseMode")
    private String responseMode;
    @NotBlank(message = "clientId")
    private String clientId;
    @NotBlank(message = "policy")
    private String policy;
    @NotBlank(message = "passwordResetPolicy")
    private String passwordResetPolicy;
    @NotBlank(message = "redirectUrlScheme")
    private String redirectUrlScheme;
    @NotBlank(message = "redirectUrlHost")
    private String redirectUrlHost;
    @Min(1)
    @Max(MAX_PORT_NUMBER)
    private int redirectUrlPort;
    @NotBlank(message = "redirectUrlPath")
    private String redirectUrlPath;
    @NotBlank(message = "logoutRedirectUrlPath")
    private String logoutRedirectUrlPath;
    @NotBlank(message = "loggedInRedirectUrl")
    private String loggedInRedirectUrl;
    @NotBlank(message = "jwkUrl")
    private String jwkUrl;
    @NotBlank(message = "issuer")
    private String issuer;
    @Min(value = 0, message = "leewayInSeconds")
    private int leewayInSeconds;

    @SuppressWarnings("squid:S00107")
    public OpenIdUrlConfig(final String scheme,
                           final String host,
                           final String responseMode,
                           final String clientId,
                           final String policy,
                           final String passwordResetPolicy,
                           final String redirectUrlScheme,
                           final String redirectUrlHost,
                           final int redirectUrlPort,
                           final String logoutRedirectUrlPath,
                           final String redirectUrlPath,
                           final String loggedInRedirectUrl,
                           final String jwkUrl,
                           final String issuer,
                           final int leewayInSeconds) {
        this.scheme = scheme;
        this.host = host;
        this.responseMode = responseMode;
        this.clientId = clientId;
        this.policy = policy;
        this.passwordResetPolicy = passwordResetPolicy;
        this.redirectUrlScheme = redirectUrlScheme;
        this.redirectUrlHost = redirectUrlHost;
        this.redirectUrlPath = redirectUrlPath;
        this.logoutRedirectUrlPath = logoutRedirectUrlPath;
        this.redirectUrlPort = redirectUrlPort;
        this.loggedInRedirectUrl = loggedInRedirectUrl;
        this.jwkUrl = jwkUrl;
        this.issuer = issuer;
        this.leewayInSeconds = leewayInSeconds;
    }

    public OpenIdUrlConfig() {
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(final String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getResponseMode() {
        return responseMode;
    }

    public void setResponseMode(final String responseMode) {
        this.responseMode = responseMode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(final String policy) {
        this.policy = policy;
    }

    public String getPasswordResetPolicy() {
        return passwordResetPolicy;
    }

    public void setPasswordResetPolicy(final String passwordResetPolicy) {
        this.passwordResetPolicy = passwordResetPolicy;
    }

    public String getRedirectUrlScheme() {
        return redirectUrlScheme;
    }

    public void setRedirectUrlScheme(final String redirectUrlScheme) {
        this.redirectUrlScheme = redirectUrlScheme;
    }

    public String getRedirectUrlHost() {
        return redirectUrlHost;
    }

    public void setRedirectUrlHost(final String redirectUrlHost) {
        this.redirectUrlHost = redirectUrlHost;
    }

    public int getRedirectUrlPort() {
        return redirectUrlPort;
    }

    public void setRedirectUrlPort(final int redirectUrlPort) {
        this.redirectUrlPort = redirectUrlPort;
    }

    public String getRedirectUrlPath() {
        return redirectUrlPath;
    }

    public void setRedirectUrlPath(final String redirectUrlPath) {
        this.redirectUrlPath = redirectUrlPath;
    }

    public String getLogoutRedirectUrlPath() {
        return logoutRedirectUrlPath;
    }

    public void setLogoutRedirectUrlPath(final String logoutRedirectUrlPath) {
        this.logoutRedirectUrlPath = logoutRedirectUrlPath;
    }

    public String getLoggedInRedirectUrl() {
        return loggedInRedirectUrl;
    }

    public void setLoggedInRedirectUrl(final String loggedInRedirectUrl) {
        this.loggedInRedirectUrl = loggedInRedirectUrl;
    }

    public String getJwkUrl() {
        return jwkUrl;
    }

    public void setJwkUrl(final String jwkUrl) {
        this.jwkUrl = jwkUrl;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public int getLeewayInSeconds() {
        return leewayInSeconds;
    }

    public void setLeewayInSeconds(final int leewayInSeconds) {
        this.leewayInSeconds = leewayInSeconds;
    }
}
