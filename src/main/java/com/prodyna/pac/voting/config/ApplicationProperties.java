package com.prodyna.pac.voting.config;


import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;


/**
 * Properties specific to Voting application.
 *
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "voting", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Async async = new Async();

    private final Http http = new Http();

    private final Cache cache = new Cache();

    private final Mail mail = new Mail();

    private final Security security = new Security();

    private final Swagger swagger = new Swagger();

    private final Metrics metrics = new Metrics();

    private final CorsConfiguration cors = new CorsConfiguration();

    public Async getAsync() {
        return this.async;
    }

    public Http getHttp() {
        return this.http;
    }

    public Cache getCache() {
        return this.cache;
    }

    public Mail getMail() {
        return this.mail;
    }

    public Security getSecurity() {
        return this.security;
    }

    public Swagger getSwagger() {
        return this.swagger;
    }

    public Metrics getMetrics() {
        return this.metrics;
    }

    public CorsConfiguration getCors() {
        return this.cors;
    }

    public static class Async {

        private int corePoolSize = 2;

        private int maxPoolSize = 50;

        private int queueCapacity = 10000;

        public int getCorePoolSize() {
            return this.corePoolSize;
        }

        public void setCorePoolSize(final int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return this.maxPoolSize;
        }

        public void setMaxPoolSize(final int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return this.queueCapacity;
        }

        public void setQueueCapacity(final int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class Http {

        private final Cache cache = new Cache();

        public Cache getCache() {
            return this.cache;
        }

        public static class Cache {

            private int timeToLiveInDays = 1461;

            public int getTimeToLiveInDays() {
                return this.timeToLiveInDays;
            }

            public void setTimeToLiveInDays(final int timeToLiveInDays) {
                this.timeToLiveInDays = timeToLiveInDays;
            }
        }
    }

    public static class Cache {

        private int timeToLiveSeconds = 3600;
        private final Hazelcast hazelcast = new Hazelcast();


        public int getTimeToLiveSeconds() {
            return this.timeToLiveSeconds;
        }

        public void setTimeToLiveSeconds(final int timeToLiveSeconds) {
            this.timeToLiveSeconds = timeToLiveSeconds;
        }
        public Hazelcast getHazelcast() {
            return this.hazelcast;
        }

        public static class Hazelcast {

            private int backupCount = 1;

            public int getBackupCount() {
                return this.backupCount;
            }

            public void setBackupCount(final int backupCount) {
                this.backupCount = backupCount;
            }
        }
    }

    public static class Mail {

        private String from = "sample@localhost";

        public String getFrom() {
            return this.from;
        }

        public void setFrom(final String from) {
            this.from = from;
        }
    }

    public static class Security {

        private final RememberMe rememberMe = new RememberMe();

        public RememberMe getRememberMe() {
            return this.rememberMe;
        }

        public static class RememberMe {

            @NotNull
            private String key;

            public String getKey() {
                return this.key;
            }

            public void setKey(final String key) {
                this.key = key;
            }
        }
    }

    public static class Swagger {

        private String title = "sample API";

        private String description = "sample API documentation";

        private String version = "0.0.1";

        private String termsOfServiceUrl;

        private String contactName;

        private String contactUrl;

        private String contactEmail;

        private String license;

        private String licenseUrl;

        public String getTitle() {
            return this.title;
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(final String description) {
            this.description = description;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(final String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return this.termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(final String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContactName() {
            return this.contactName;
        }

        public void setContactName(final String contactName) {
            this.contactName = contactName;
        }

        public String getContactUrl() {
            return this.contactUrl;
        }

        public void setContactUrl(final String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getContactEmail() {
            return this.contactEmail;
        }

        public void setContactEmail(final String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getLicense() {
            return this.license;
        }

        public void setLicense(final String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return this.licenseUrl;
        }

        public void setLicenseUrl(final String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }
    }

    public static class Metrics {

        private final Jmx jmx = new Jmx();

        private final Spark spark = new Spark();

        private final Graphite graphite = new Graphite();

        private final Logs logs = new Logs();

        public Jmx getJmx() {
            return this.jmx;
        }

        public Spark getSpark() {
            return this.spark;
        }

        public Graphite getGraphite() {
            return this.graphite;
        }

        public Logs getLogs() {
            return this.logs;
        }


        public static class Jmx {

            private boolean enabled = true;

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(final boolean enabled) {
                this.enabled = enabled;
            }
        }

        public static class Spark {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 9999;

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(final boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return this.host;
            }

            public void setHost(final String host) {
                this.host = host;
            }

            public int getPort() {
                return this.port;
            }

            public void setPort(final int port) {
                this.port = port;
            }
        }

        public static class Graphite {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 2003;

            private String prefix = "sample";

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(final boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return this.host;
            }

            public void setHost(final String host) {
                this.host = host;
            }

            public int getPort() {
                return this.port;
            }

            public void setPort(final int port) {
                this.port = port;
            }

            public String getPrefix() {
                return this.prefix;
            }

            public void setPrefix(final String prefix) {
                this.prefix = prefix;
            }
        }

        public static  class Logs {

            private boolean enabled = false;

            private long reportFrequency = 60;

            public long getReportFrequency() {
                return this.reportFrequency;
            }

            public void setReportFrequency(final int reportFrequency) {
                this.reportFrequency = reportFrequency;
            }

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(final boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    private final Logging logging = new Logging();

    public Logging getLogging() { return this.logging; }

    public static class Logging {

        private final Logstash logstash = new Logstash();

        public Logstash getLogstash() { return this.logstash; }

        public static class Logstash {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 5000;

            private int queueSize = 512;

            public boolean isEnabled() { return this.enabled; }

            public void setEnabled(final boolean enabled) { this.enabled = enabled; }

            public String getHost() { return this.host; }

            public void setHost(final String host) { this.host = host; }

            public int getPort() { return this.port; }

            public void setPort(final int port) { this.port = port; }

            public int getQueueSize() { return this.queueSize; }

            public void setQueueSize(final int queueSize) { this.queueSize = queueSize; }
        }
    }

}
