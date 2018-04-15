package org.mitre.discovery.web;

import java.util.HashMap;
import java.util.Map;

import org.mitre.openid.connect.web.DynamicClientRegistrationEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Injects malicious end-points into the OpenID Connect Discovery configuration.
 *
 * @author amar
 *
 */
public interface MaliciousEndpointInjector {

    /**
     * Inject malicious end-points into the provided discovery configuration.
     *
     * @param discoveryConfiguration Map of discovery configuration attributes
     * @return The post-injection configuration
     */
    Map<String, Object> inject(Map<String, Object> discoveryConfiguration);

    /**
     * A pass-through {@link MaliciousEndpointInjector} that does nothing to the configuration.
     */
    static class DoNothingMaliciousEndpointInjector implements MaliciousEndpointInjector {
        @Override
        public Map<String, Object> inject(final Map<String, Object> discoveryConfiguration) {
            return discoveryConfiguration;
        }
    }

    /**
     * A {@link MaliciousEndpointInjector} that implements the broken end-user authentication attack.
     */
    static class BrokenEndUserAuthEndpointInjector implements MaliciousEndpointInjector {

        private static final Logger LOGGER = LoggerFactory.getLogger(BrokenEndUserAuthEndpointInjector.class);

        private final String honestIssuerUri;

        public BrokenEndUserAuthEndpointInjector(final String honestIssuerUri) {
            this.honestIssuerUri = honestIssuerUri;
        }

        /**
         * Injects the honest-op's URIs for the 'registration_endpoint' and 'authorization_endpoint' (as shown in
         * Listing 2 of the Mainka et al. paper).
         *
         * @param discoveryConfiguration Map of discovery configuration attributes
         * @return The post-injection configuration
         */
        @Override
        public Map<String, Object> inject(final Map<String, Object> discoveryConfiguration) {

            final Map<String, Object> maliciousEndpoints = new HashMap<>();

            maliciousEndpoints.put("registration_endpoint", honestIssuerUri + DynamicClientRegistrationEndpoint.URL);
            maliciousEndpoints.put("authorization_endpoint", honestIssuerUri + "authorize");

            LOGGER.info("Injecting malicious endpoints: {}", maliciousEndpoints);

            discoveryConfiguration.putAll(maliciousEndpoints);

            return discoveryConfiguration;
        }

    }
}