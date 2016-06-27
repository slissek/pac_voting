package com.prodyna.pac.voting.web.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.prodyna.pac.voting.config.ApplicationProperties;

/**
 * This filter is used in production, to put HTTP cache headers with a long (1 month) expiration time.
 */
public class CachingHttpHeadersFilter implements Filter {

    // We consider the last modified date is the start up time of the server
    private final static long LAST_MODIFIED = System.currentTimeMillis();

    private long CACHE_TIME_TO_LIVE = TimeUnit.DAYS.toMillis(1461L);

    private final ApplicationProperties applicationProperties;

    public CachingHttpHeadersFilter(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.CACHE_TIME_TO_LIVE = TimeUnit.DAYS.toMillis(this.applicationProperties.getHttp().getCache().getTimeToLiveInDays());
    }

    @Override
    public void destroy() {
        // Nothing to destroy
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Cache-Control", "max-age=" + this.CACHE_TIME_TO_LIVE + ", public");
        httpResponse.setHeader("Pragma", "cache");

        // Setting Expires header, for proxy caching
        httpResponse.setDateHeader("Expires", this.CACHE_TIME_TO_LIVE + System.currentTimeMillis());

        // Setting the Last-Modified header, for browser caching
        httpResponse.setDateHeader("Last-Modified", LAST_MODIFIED);

        chain.doFilter(request, response);
    }
}
