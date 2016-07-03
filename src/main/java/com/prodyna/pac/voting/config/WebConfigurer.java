package com.prodyna.pac.voting.config;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.codahale.metrics.MetricRegistry;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.web.SessionListener;
import com.hazelcast.web.spring.SpringAwareWebFilter;
import com.prodyna.pac.voting.web.filter.CachingHttpHeadersFilter;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    @Inject
    private Environment env;

    @Inject
    private ApplicationProperties applicationProperties;

    @Autowired(required = false)
    private MetricRegistry metricRegistry;

    // Hazelcast instance is injected to force its initialization before the Servlet filter uses it.
    @Inject
    private HazelcastInstance hazelcastInstance;

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        if (this.env.getActiveProfiles().length != 0) {
            this.log.info("Web application configuration, using profiles: {}", Arrays.toString(this.env.getActiveProfiles()));
        }
        final EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        this.initClusteredHttpSessionFilter(servletContext, disps);
        this.initMetrics(servletContext, disps);
        if (this.env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            this.initCachingHttpHeadersFilter(servletContext, disps);
        }
        this.log.info("Web application fully configured");
    }

    /**
     * Initializes the Clustered Http Session filter
     */
    private void initClusteredHttpSessionFilter(final ServletContext servletContext, final EnumSet<DispatcherType> disps) {
        this.log.debug("Registering Clustered Http Session Filter");
        servletContext.addListener(new SessionListener());

        final FilterRegistration.Dynamic hazelcastWebFilter = servletContext.addFilter("hazelcastWebFilter", new SpringAwareWebFilter());
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("instance-name", "voting_application");
        // Name of the distributed map storing your web session objects
        parameters.put("map-name", "clustered-http-sessions");

        // How is your load-balancer configured?
        // Setting "sticky-session" to "true" means all requests of a session
        // are routed to the node where the session is first created.
        // This is excellent for performance.
        // If "sticky-session" is set to "false", then when a session is updated
        // on a node, entries for this session on all other nodes are invalidated.
        // You have to know how your load-balancer is configured before
        // setting this parameter. Default is true.
        parameters.put("sticky-session", "true");

        // Name of session id cookie
        parameters.put("cookie-name", "hazelcast.sessionId");

        // Are you debugging? Default is false.
        if (this.env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            parameters.put("debug", "false");
        } else {
            parameters.put("debug", "true");
        }

        // Do you want to shutdown HazelcastInstance during
        // web application undeploy process?
        // Default is true.
        parameters.put("shutdown-on-destroy", "true");

        hazelcastWebFilter.setInitParameters(parameters);
        hazelcastWebFilter.addMappingForUrlPatterns(disps, true, "/*");
        hazelcastWebFilter.setAsyncSupported(true);
    }

    /**
     * Set up Mime types and, if needed, set the document root.
     */
    @Override
    public void customize(final ConfigurableEmbeddedServletContainer container) {
        final MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
        mappings.add("html", "text/html;charset=utf-8");
        // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
        mappings.add("json", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);

        // When running in an IDE or with ./mvnw spring-boot:run, set location of the static web assets.
        this.setLocationForStaticAssets(container);
    }

    private void setLocationForStaticAssets(final ConfigurableEmbeddedServletContainer container) {
        File root;
        final String prefixPath = this.resolvePathPrefix();
        if (this.env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            root = new File(prefixPath + "target/www/");
        } else {
            root = new File(prefixPath + "src/main/webapp/");
        }
        if (root.exists() && root.isDirectory()) {
            container.setDocumentRoot(root);
        }
    }

    /**
     *  Resolve path prefix to static resources.
     */
    private String resolvePathPrefix() {
        final String fullExecutablePath = this.getClass().getResource("").getPath();
        final String rootPath = Paths.get(".").toUri().normalize().getPath();
        final String extractedPath = fullExecutablePath.replace(rootPath, "");
        final int extractionEndIndex = extractedPath.indexOf("target/");
        if(extractionEndIndex <= 0) {
            return "";
        }
        return extractedPath.substring(0, extractionEndIndex);
    }

    /**
     * Initializes the caching HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(final ServletContext servletContext,
            final EnumSet<DispatcherType> disps) {
        this.log.debug("Registering Caching HTTP Headers Filter");
        final FilterRegistration.Dynamic cachingHttpHeadersFilter =
                servletContext.addFilter("cachingHttpHeadersFilter",
                        new CachingHttpHeadersFilter(this.applicationProperties));

        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/content/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/app/*");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }

    /**
     * Initializes Metrics.
     */
    private void initMetrics(final ServletContext servletContext, final EnumSet<DispatcherType> disps) {
        this.log.debug("Initializing Metrics registries");
        servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE,
                this.metricRegistry);
        servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY,
                this.metricRegistry);

        this.log.debug("Registering Metrics Filter");
        final FilterRegistration.Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter",
                new InstrumentedFilter());

        metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
        metricsFilter.setAsyncSupported(true);

        this.log.debug("Registering Metrics Servlet");
        final ServletRegistration.Dynamic metricsAdminServlet =
                servletContext.addServlet("metricsServlet", new MetricsServlet());

        metricsAdminServlet.addMapping("/management/jhipster/metrics/*");
        metricsAdminServlet.setAsyncSupported(true);
        metricsAdminServlet.setLoadOnStartup(2);
    }

    @Bean
    @ConditionalOnProperty(name = "jhipster.cors.allowed-origins")
    public CorsFilter corsFilter() {
        this.log.debug("Registering CORS filter");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = this.applicationProperties.getCors();
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/v2/api-docs", config);
        source.registerCorsConfiguration("/oauth/**", config);
        return new CorsFilter(source);
    }
}
