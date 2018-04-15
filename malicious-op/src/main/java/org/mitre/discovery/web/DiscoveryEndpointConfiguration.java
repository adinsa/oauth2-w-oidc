package org.mitre.discovery.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configures a profile-specific {@link MaliciousEndpointInjector} for {@link DiscoveryEndpoint}.
 *
 * @author amar
 *
 */
@Configuration
public class DiscoveryEndpointConfiguration {

    @Value("${honest.issuer.uri}")
    private String honestIssuerUri;

    @Bean
    @Profile("broken-end-user-auth")
    public MaliciousEndpointInjector brokenEndUserAuthInjector() {
        return new MaliciousEndpointInjector.BrokenEndUserAuthEndpointInjector(honestIssuerUri);
    }

    @Bean
    @Profile("code-injection")
    public MaliciousEndpointInjector codeInjectionInjector() {
        return new MaliciousEndpointInjector.DoNothingMaliciousEndpointInjector();
    }
}
