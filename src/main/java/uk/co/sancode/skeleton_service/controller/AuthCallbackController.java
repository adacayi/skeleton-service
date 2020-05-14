package uk.co.sancode.skeleton_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uk.co.sancode.skeleton_service.service.AuthenticationException;
import uk.co.sancode.skeleton_service.service.AuthenticationService;
import uk.co.sancode.skeleton_service.utilities.OpenIdUrlConfig;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping
class AuthCallbackController {
    private static final int SECONDS = 60;
    private AuthenticationService authenticationService;
    private OpenIdUrlConfig openIdUrlConfig;

    private int sessionTimeout;

    AuthCallbackController(final @Autowired AuthenticationService authenticationService,
                           final @Autowired OpenIdUrlConfig openIdUrlConfig,
                           final @Value("${session-timeout}") int sessionTimeout) {
        this.authenticationService = authenticationService;
        this.openIdUrlConfig = openIdUrlConfig;
        this.sessionTimeout = sessionTimeout;
    }

    @GetMapping("/auth-callback")
    public RedirectView authenticateGet(@RequestParam(name = "id_token", required = false) final String idToken,
                                        final HttpServletRequest request,
                                        final RedirectAttributes attributes) throws
            AuthenticationException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            InvalidAlgorithmParameterException {
        return authenticate(idToken, request);
    }

    @PostMapping("/auth-callback")
    public ModelAndView authenticatePost(@RequestParam(name = "id_token", required = false) final String idToken,
                                         final HttpServletRequest request,
                                         final RedirectAttributes attributes) throws
            AuthenticationException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            InvalidAlgorithmParameterException {

        return new ModelAndView(authenticate(idToken, request));
    }

    RedirectView authenticate(final String idToken,
                              final HttpServletRequest request) throws
            AuthenticationException,
            IllegalBlockSizeException,
            NoSuchPaddingException,
            BadPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException, InvalidAlgorithmParameterException {

        var authenticationToken = authenticationService.authenticate(idToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        request.getSession().setMaxInactiveInterval(sessionTimeout * SECONDS);

        return new RedirectView(openIdUrlConfig.getLoggedInRedirectUrl(), true, false);
    }
}
