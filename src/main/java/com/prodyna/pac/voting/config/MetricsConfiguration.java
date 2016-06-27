package com.prodyna.pac.voting.config;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
@EnableMetrics(proxyTargetClass = true)
public class MetricsConfiguration extends MetricsConfigurerAdapter
{

    private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";
    private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";
    private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";
    private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";
    private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";

    private final Logger log = LoggerFactory.getLogger(MetricsConfiguration.class);

    private final MetricRegistry metricRegistry = new MetricRegistry();

    private final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    @Inject
    private ApplicationProperties applicationProperties;

    @Override
    @Bean
    public MetricRegistry getMetricRegistry()
    {
        return this.metricRegistry;
    }

    @Override
    @Bean
    public HealthCheckRegistry getHealthCheckRegistry()
    {
        return this.healthCheckRegistry;
    }

    @PostConstruct
    public void init()
    {
        this.log.debug("Registering JVM gauges");
        this.metricRegistry.register(PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
        this.metricRegistry.register(PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
        this.metricRegistry.register(PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
        this.metricRegistry.register(PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
        this.metricRegistry.register(PROP_METRIC_REG_JVM_BUFFERS, new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
        if (this.applicationProperties.getMetrics().getJmx().isEnabled())
        {
            this.log.debug("Initializing Metrics JMX reporting");
            final JmxReporter jmxReporter = JmxReporter.forRegistry(this.metricRegistry).build();
            jmxReporter.start();
        }

        if (this.applicationProperties.getMetrics().getLogs().isEnabled())
        {
            this.log.info("Initializing Metrics Log reporting");
            final Slf4jReporter reporter = Slf4jReporter.forRegistry(this.metricRegistry).outputTo(LoggerFactory.getLogger("metrics"))
                    .convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build();
            reporter.start(this.applicationProperties.getMetrics().getLogs().getReportFrequency(), TimeUnit.SECONDS);
        }
    }

    @Configuration
    @ConditionalOnClass(Graphite.class)
    public static class GraphiteRegistry
    {
        private final Logger log = LoggerFactory.getLogger(GraphiteRegistry.class);

        @Inject
        private MetricRegistry metricRegistry;

        @Inject
        private ApplicationProperties applicationProperties;

        @PostConstruct
        private void init()
        {
            if (this.applicationProperties.getMetrics().getGraphite().isEnabled())
            {
                this.log.info("Initializing Metrics Graphite reporting");
                final String graphiteHost = this.applicationProperties.getMetrics().getGraphite().getHost();
                final Integer graphitePort = this.applicationProperties.getMetrics().getGraphite().getPort();
                final String graphitePrefix = this.applicationProperties.getMetrics().getGraphite().getPrefix();
                final Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
                final GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(this.metricRegistry).convertRatesTo(TimeUnit.SECONDS)
                        .convertDurationsTo(TimeUnit.MILLISECONDS).prefixedWith(graphitePrefix).build(graphite);
                graphiteReporter.start(1, TimeUnit.MINUTES);
            }
        }
    }

    //    @Configuration
    //    @ConditionalOnClass(SparkReporter.class)
    //    public static class SparkRegistry
    //    {
    //        private final Logger log = LoggerFactory.getLogger(SparkRegistry.class);
    //
    //    @Inject
    //        private MetricRegistry metricRegistry;
    //
    //    @Inject
    //        private ApplicationProperties applicationProperties;
    //
    //        @PostConstruct
    //        private void init()
    //        {
    //            if (this.applicationProperties.getMetrics().getSpark().isEnabled())
    //            {
    //                this.log.info("Initializing Metrics Spark reporting");
    //                final String sparkHost = this.applicationProperties.getMetrics().getSpark().getHost();
    //                final Integer sparkPort = this.applicationProperties.getMetrics().getSpark().getPort();
    //                final SparkReporter sparkReporter = SparkReporter.forRegistry(this.metricRegistry).convertRatesTo(TimeUnit.SECONDS)
    //                        .convertDurationsTo(TimeUnit.MILLISECONDS).build(sparkHost, sparkPort);
    //                sparkReporter.start(1, TimeUnit.MINUTES);
    //            }
    //        }
    //    }
}
