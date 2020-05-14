package uk.co.sancode.skeleton_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Service;
import uk.co.sancode.skeleton_service.utilities.OpenIdUrlConfig;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class AuthenticationService {
    private TokenValidationService tokenValidationService;
    private OpenIdUrlConfig config;
    static final int CONNECTION_TIMEOUT = 5000;
    static final int READ_TIMEOUT = 10000;

    AuthenticationService(@Autowired final TokenValidationService tokenValidationService,
                          @Autowired final OpenIdUrlConfig config) {
        this.tokenValidationService = tokenValidationService;
        this.config = config;
    }

    public AbstractAuthenticationToken authenticate(final String idToken) throws
            AuthenticationException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            InvalidAlgorithmParameterException {
//        var decodedToken = tokenValidationService.getDecodedToken(idToken);
//        var jwkProvider = tokenValidationService.getJwkProvider(config.getJwkUrl(), CONNECTION_TIMEOUT, READ_TIMEOUT);
//        var publicKey = tokenValidationService.getPublicKey(jwkProvider, decodedToken);
        return tokenValidationService.validateTokenSignature(
                null,
                idToken,
                config.getIssuer(),
                config.getClientId(),
                config.getLeewayInSeconds());
    }
}
