package uk.co.sancode.skeleton_service.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static uk.co.sancode.skeleton_service.utilities.OpenIdUrlType.RESET_PASSWORD;

public class OpenIdUrlBuilder {
    private static final String POLICY_QUERY_PARAM_NAME = "p";
    private static final String STRATEGY_QUERY_PARAM_NAME = "nonce";
    private static final String SCOPE_QUERY_PARAM_NAME = "scope";
    private static final String RESPONSE_TYPE_QUERY_PARAM_NAME = "response_type";
    private static final String RESPONSE_MODE_QUERY_PARAM_NAME = "response_mode";
    private static final String CLIENT_ID_QUERY_PARAM_NAME = "client_id";
    private static final String REDIRECT_URL_QUERY_PARAM_NAME = "redirect_uri";

    private static final String STRATEGY_QUERY_PARAM_VALUE = "defaultNonce";
    private static final String SCOPE_QUERY_PARAM_VALUE = "openid";
    private static final String RESPONSE_TYPE_QUERY_PARAM_VALUE = "id_token";

    private OpenIdUrlType urlType = OpenIdUrlType.LOGIN;

    @Autowired
    private OpenIdUrlConfig openIdUrlConfig;

    public OpenIdUrlBuilder(final OpenIdUrlConfig openIdUrlConfig) {
        this.openIdUrlConfig = openIdUrlConfig;
    }

    public OpenIdUrlBuilder withType(final OpenIdUrlType urlType) {
        this.urlType = urlType;
        return this;
    }

    public String build() throws URISyntaxException {
        final String redirectUrl = new RedirectUrlBuilder(
                openIdUrlConfig.getRedirectUrlScheme(),
                openIdUrlConfig.getRedirectUrlHost(),
                openIdUrlConfig.getRedirectUrlPort(),
                getPath())
                .withEncoding(Charset.forName("utf-8"))
                .build();

        return UriComponentsBuilder
                .newInstance()
                .scheme(openIdUrlConfig.getScheme())
                .host(openIdUrlConfig.getHost())
                .path(urlType.getEndPoint())
                .queryParam(POLICY_QUERY_PARAM_NAME,
                        urlType == RESET_PASSWORD
                                ? openIdUrlConfig.getPasswordResetPolicy()
                                : openIdUrlConfig.getPolicy())
                .queryParam(STRATEGY_QUERY_PARAM_NAME, STRATEGY_QUERY_PARAM_VALUE)
                .queryParam(SCOPE_QUERY_PARAM_NAME, SCOPE_QUERY_PARAM_VALUE)
                .queryParam(RESPONSE_TYPE_QUERY_PARAM_NAME, RESPONSE_TYPE_QUERY_PARAM_VALUE)
                .queryParam(RESPONSE_MODE_QUERY_PARAM_NAME, openIdUrlConfig.getResponseMode())
                .queryParam(CLIENT_ID_QUERY_PARAM_NAME, openIdUrlConfig.getClientId())
                .queryParam(REDIRECT_URL_QUERY_PARAM_NAME, redirectUrl)
                .build()
                .toUriString();
    }

    private String getPath() {
        switch (urlType) {
            case LOGIN:
                return openIdUrlConfig.getRedirectUrlPath();
            case LOGOUT:
                return openIdUrlConfig.getLogoutRedirectUrlPath();
            case RESET_PASSWORD:
                return openIdUrlConfig.getLoggedInRedirectUrl();
            default:
                return "unidentified path";
        }
    }
}
