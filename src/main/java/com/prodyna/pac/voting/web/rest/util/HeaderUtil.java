package com.prodyna.pac.voting.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 *
 */
public class HeaderUtil
{
    public static HttpHeaders createAlert(final String message, final String param)
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-VotingApp-alert", message);
        headers.add("X-VotingApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(final String entityName, final String param)
    {
        return createAlert("VotingApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(final String entityName, final String param)
    {
        return createAlert("VotingApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(final String entityName, final String param)
    {
        return createAlert("VotingApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(final String entityName, final String errorKey, final String defaultMessage)
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-VotingApp-error", "error." + errorKey);
        headers.add("X-VotingApp-params", entityName);
        return headers;
    }
}
