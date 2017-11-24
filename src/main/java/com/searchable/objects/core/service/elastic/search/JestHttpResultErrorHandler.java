package com.searchable.objects.core.service.elastic.search;

import io.searchbox.client.JestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to handle the response from the http call to elastic.
 *
 * @author Archan
 */
public class JestHttpResultErrorHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(JestHttpResultErrorHandler.class);

    /**
     * Error will get logged with root cause sent back by elastic search http call.
     * Success will be logged too.
     *
     * @param result
     * @throws JestResultException
     */
    public static void handle(JestResult result) throws JestResultException {
        // 2xx series status codes indicate success
        if (result.getResponseCode() < 200 || result.getResponseCode() >= 300) {
            String errorMessage = result.getErrorMessage();
            LOGGER.error("JestResult :: [ " + errorMessage + " ]");
            if (result.getJsonObject().get("error") != null) {
                errorMessage = result.getJsonObject().get("error").getAsJsonObject().get("root_cause").toString();
            }
            throw new JestResultException(errorMessage);
        } else {
            LOGGER.debug("JestResult SUCCESS");
            LOGGER.debug("JestResult SUCCESS:: [ " + result.getJsonObject().toString() + " ]");
        }
    }

}
