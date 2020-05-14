package uk.co.sancode.skeleton_service.service;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static uk.co.sancode.skeleton_service.model.LogCategory.AUTHORIZATION;


@Service
public class TokenValidationService {

    public static final String DISPLAY_NAME_KEY = "displayName";
    public static final String USER_DETAILS_TYPE = "User Details";
    public static final String JWT_TOKEN_DISPLAY_NAME_KEY = "name";
    public static final String JWT_TOKEN_DATA_PROVIDER_ID_KEY = "dataProviderId";
    public static final String JWT_TOKEN_DATA_PROVIDER_AUTH_TOKEN_KEY = "dataProviderAuthToken";
    public static final String JWT_TOKEN_DATA_PROVIDER_LICENSES_KEY = "license";
    public static final String JWT_TOKEN_GROUPS_KEY = "groups";

    public DecodedJWT getDecodedToken(final String tokenStr) throws AuthenticationException {
        if (tokenStr == null) {
            throw new AuthenticationException(String.format("%s token empty", AUTHORIZATION), new IOException());
        }

        try {
            return JWT.decode(tokenStr);
        } catch (JWTDecodeException e) {
            throw new AuthenticationException(String.format("%s invalid token", AUTHORIZATION), e);
        }

    }

    public JwkProvider getJwkProvider(final String jwkUrl, final int connectTimeout, final int readTimeout)
            throws AuthenticationException {
        try {
            return new UrlJwkProvider(new URL(jwkUrl), connectTimeout, readTimeout);
        } catch (MalformedURLException e) {
            throw new AuthenticationException(String.format("%s error setting up JWT Key Provider", AUTHORIZATION), e);
        }
    }

    public PublicKey getPublicKey(final JwkProvider jwkProvider, final DecodedJWT decodedToken)
            throws AuthenticationException {
        try {
            return jwkProvider.get(decodedToken.getKeyId()).getPublicKey();
        } catch (JwkException e) {
            throw new AuthenticationException(String.format("%s error retrieving public key", AUTHORIZATION), e);
        }
    }

    public OpenIDAuthenticationToken validateTokenSignature(
            final PublicKey publicKey,
            final String idToken,
            final String issuer,
            final String audience,
            final int leeWayInSeconds) throws
            AuthenticationException,
            IllegalBlockSizeException,
            InvalidKeyException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            BadPaddingException, InvalidAlgorithmParameterException {

        String errorMessage = "error validating token";
        DecodedJWT validatedToken;

//        try {
//            validatedToken = JWT.require(Algorithm.RSA256((RSAPublicKey) publicKey, null))
//                    .withAudience(audience)
//                    .withIssuer(issuer)
//                    .acceptLeeway(leeWayInSeconds)
//                    .build()
//                    .verify(idToken);
//        } catch (Exception e) {
//            throw new AuthenticationException(String.format("%s %s", AUTHORIZATION, errorMessage), e);
//        }

        validatedToken = JWT.decode(idToken); // skipped validation
        var nameClaim = validatedToken.getClaim(JWT_TOKEN_DISPLAY_NAME_KEY);
        var dataProviderIdClaim = validatedToken.getClaim(JWT_TOKEN_DATA_PROVIDER_ID_KEY);
        var groupsClaim = validatedToken.getClaim(JWT_TOKEN_GROUPS_KEY);

        var attributes = List.of(
                new OpenIDAttribute(DISPLAY_NAME_KEY,
                        USER_DETAILS_TYPE,
                        List.of(nameClaim.isNull() ? "" : nameClaim.asString())));
        var roles = new ArrayList<String>();

        if (!dataProviderIdClaim.isNull()) {
            roles.add(dataProviderIdClaim.asString());
        }

        if (!groupsClaim.isNull()) {
            roles.addAll(List.of(groupsClaim.asArray(String.class)));
            roles = roles.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
        }

        return new OpenIDAuthenticationToken(
                validatedToken.getSubject(),
                roles.stream().map(x -> new SimpleGrantedAuthority("ROLE_" + x))
                        .collect(Collectors.toList()),
                "",
                attributes);
    }
}
