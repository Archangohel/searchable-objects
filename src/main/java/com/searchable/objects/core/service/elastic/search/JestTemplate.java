package com.searchable.objects.core.service.elastic.search;

import com.searchable.objects.utils.prop.PropReader;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.params.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Archan
 */
@Component
public class JestTemplate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PropReader propReader;

    @Autowired
    private IdentityService identityService;

    private JestClientFactory jestClientFactory;

    private Integer numberOfRecordsToReturn;

    public JestTemplate() {
    }

    private JestClient getJestClient() {
        initializeClientFactory();
        return jestClientFactory.getObject();
    }

    private void closeJestClient(JestClient jestClient) {
        try {
            jestClient.close();
        } catch (IOException e) {
            logger.error("Error in closing the jest client.", e);
        }
    }

    private void init(String url) {
        this.jestClientFactory = new JestClientFactory();
        Integer readTimeOut = Integer.parseInt(propReader.getProperty("elastic.cluster.http.read.timeout.milliseconds"));
        this.jestClientFactory.setHttpClientConfig(new HttpClientConfig.Builder(url)
                .multiThreaded(true).readTimeout(readTimeOut).build());
    }

    private void initializeClientFactory() {
        if (this.jestClientFactory == null) {
            String endPointUrl = propReader.getProperty("elastic.cluster.http.endpoint");

            if (StringUtils.hasText(endPointUrl)) {
                init(endPointUrl);
            } else {
                throw new IllegalStateException("Default endpoint url is not defined! " +
                        "Use constructor with the ES HTTP endpoint URL");
            }
            this.numberOfRecordsToReturn = Integer.parseInt(propReader.getProperty("elastic.cluster.http.default.return.size"));
        }
    }


    /**
     * It will create the index to elastic cluster if not exists. If index exists then it will not do anything
     * and return success.
     *
     * @param indexName
     * @return Status of the operation. TRUE represent successful and FALSE represent failure.
     */
    public boolean createIndex(String indexName) {
        JestClient jestClient = getJestClient();
        logger.info("Creating ES index :: " + indexName);
        boolean indexExists = false;
        try {
            indexExists = jestClient.execute(new IndicesExists.Builder(indexName).build()).isSucceeded();
            if (!indexExists) {
                JestResult result = jestClient.execute(new CreateIndex.Builder(indexName).build());
                JestHttpResultErrorHandler.handle(result);
            }
        } catch (Exception e) {
            logger.error("Error in creating ES index :: " + indexName, e);
            return false;
        } finally {
            closeJestClient(jestClient);
        }
        return true;
    }


    /**
     * This method will create mapping for the type specified against the index specified.
     * Mapping settings needs to be passed as json string.
     *
     * @param indexName
     * @param type
     * @param mapping
     * @return Status of the operation. TRUE represent success and FALSE represent failure.
     */
    public boolean putMapping(String indexName, String type, String mapping) {
        JestClient jestClient = getJestClient();
        logger.info("Put Mapping to ES index [ " + indexName + " ] , type [ " + type
                + " ] and mappings [ " + mapping + " ]");
        try {
            JestResult result = jestClient.execute(new PutMapping.Builder(indexName, type, mapping).build());
            JestHttpResultErrorHandler.handle(result);
        } catch (Exception e) {
            logger.error("Error in adding mapping to ES index [ " + indexName + " ] , type [ " + type
                    + " ] and mappings [ " + mapping + " ]", e);
            return false;
        } finally {
            closeJestClient(jestClient);
        }
        return true;
    }

    /**
     * Deletes the index specified.
     *
     * @param indexName
     * @return Status of the operation. TRUE represent successful and FALSE represent failure.
     */
    public boolean deleteIndex(String indexName) {
        JestClient jestClient = getJestClient();
        logger.info("Deleting ES index :: " + indexName);
        try {
            JestResult result = jestClient.execute(new DeleteIndex.Builder(indexName).build());
            JestHttpResultErrorHandler.handle(result);
        } catch (Exception e) {
            logger.error("Error in deleting ES index :: " + indexName, e);
            return false;
        } finally {
            closeJestClient(jestClient);
        }
        return true;
    }

    /**
     * This method will add objects to the type under the specified index.
     *
     * @param indexName
     * @param type
     * @param objects
     * @return Status of the operation. TRUE represent successful and FALSE represent failure.
     */
    public <T> boolean addToIndex(String indexName, String type, List<T> objects) {
        logger.info("loading ES index [ " + indexName + " ] , type [ " + type + " ]");
        long ts1 = 0;
        if (logger.isDebugEnabled()) {
            ts1 = System.currentTimeMillis();
        }
        boolean hasAtleastOneElementToProcess = false;
        Bulk.Builder bulkBuilder = new Bulk.Builder();
        for (T object : objects) {
            String key = identityService.getKeyAsString(object);
            if (!StringUtils.isEmpty(key)) {
                bulkBuilder.addAction(new Index.Builder(object).index(indexName).type(type).id(key).build());
                hasAtleastOneElementToProcess = true;
            }
        }
        if (hasAtleastOneElementToProcess) {
            JestClient jestClient = getJestClient();
            try {
                JestResult result = jestClient.execute(bulkBuilder.build());
                JestHttpResultErrorHandler.handle(result);
            } catch (Exception e) {
                logger.error("Error in loading ES index :: " + indexName, e);
                return false;
            } finally {
                closeJestClient(jestClient);
            }
            if (logger.isDebugEnabled()) {
                long ts2 = System.currentTimeMillis();
                logger.debug("Time taken for loading " + objects.size() + " [ " + type + " ] :: " + (ts2 - ts1) / 1000 + " (seconds)");
            }
            return true;
        } else {
            logger.info("No element to process");
            return false;
        }
    }

    /**
     * This method will remove objects to the type under the specified index.
     *
     * @param indexName
     * @param type
     * @param objects
     * @return Status of the operation. TRUE represent successful and FALSE represent failure.
     */
    public <T> boolean removeFromIndex(String indexName, String type, List<T> objects) {
        logger.info("Removing from ES index [ " + indexName + " ] , type [ " + type + " ]");
        long ts1 = 0;
        if (logger.isDebugEnabled()) {
            ts1 = System.currentTimeMillis();
        }
        boolean hasAtleastOneElementToProcess = false;
        Bulk.Builder bulkBuilder = new Bulk.Builder();
        for (T object : objects) {
            String key = identityService.getKeyAsString(object);
            if (!StringUtils.isEmpty(key)) {
                bulkBuilder.addAction(new Delete.Builder(key).index(indexName).type(type).build());
                hasAtleastOneElementToProcess = true;
            }
        }
        if (hasAtleastOneElementToProcess) {
            JestClient jestClient = getJestClient();
            try {
                JestResult result = jestClient.execute(bulkBuilder.build());
                JestHttpResultErrorHandler.handle(result);
            } catch (Exception e) {
                logger.error("Error in delete from ES index :: " + indexName, e);
                return false;
            } finally {
                closeJestClient(jestClient);
            }
            if (logger.isDebugEnabled()) {
                long ts2 = System.currentTimeMillis();
                logger.debug("Time taken for deleting " + objects.size() + " [ " + type + " ] :: " + (ts2 - ts1) / 1000 + " (seconds)");
            }
            return true;
        } else {
            logger.info("No element to process");
            return false;
        }
    }

    /**
     * Search the specified index and type with the query and return the results.
     * In case of error it will get logged by the custom error handler.
     *
     * @param indexName
     * @param type
     * @param query     It is the query in Json format.
     * @return SearchResult {@link SearchResult} object from the Jest framework.
     * @throws JestResultException
     * @throws IOException
     */
    public SearchResult search(String indexName, String type, String query) throws JestResultException, IOException {

        Search.Builder searchBuilder = new Search.Builder(query).addIndex(indexName).addType(type).
                setParameter(Parameters.SIZE, this.numberOfRecordsToReturn);
        JestClient jestClient = getJestClient();

        SearchResult result = null;
        try {
            result = jestClient.execute(searchBuilder.build());
            JestHttpResultErrorHandler.handle(result);
        } catch (JestResultException e) {
            logger.error("JestResultException in searching for query [ " + query + " ] in ES index [ " + indexName + "] , type [ " + type + " ]", e);
            throw e;
        } catch (IOException e) {
            logger.error("IOException in searching for query [ " + query + " ] in ES index [ " + indexName + "] , type [ " + type + " ]", e);
            throw e;
        } finally {
            closeJestClient(jestClient);
        }
        return result;
    }

    /**
     * This is same operation as search on single index {@link JestTemplate#search(String, String, String)}.
     * Only difference is it performs search across multiple indexes (includes all the types).
     *
     * @param indexes
     * @param query
     * @return SearchResult {@link SearchResult} object from the Jest framework.
     * @throws JestResultException
     * @throws IOException
     */
    protected SearchResult search(List<String> indexes, String query) throws JestResultException, IOException {
        Search.Builder searchBuilder = new Search.Builder(query).setParameter(Parameters.SIZE, this.numberOfRecordsToReturn).addIndices(indexes);

        SearchResult result = null;
        JestClient jestClient = getJestClient();
        try {
            result = jestClient.execute(searchBuilder.build());
            JestHttpResultErrorHandler.handle(result);
        } catch (JestResultException e) {
            logger.error("JestResultException in searching for query [ " + query + " ] in all ES indexes.", e);
            throw e;
        } catch (IOException e) {
            logger.error("IOException in searching for query [ " + query + " ] in all ES indexes. ", e);
            throw e;
        } finally {
            closeJestClient(jestClient);
        }
        return result;
    }

    /**
     * Same as search on multiple indexes {@link JestTemplate#search(List, String)}. Additionally this method will check
     * for valid indexes and only search agains those indexes.
     *
     * @param indexes
     * @param query
     * @param checkIndexValidity
     * @return SearchResult {@link SearchResult} object from the Jest framework.
     * @throws JestResultException
     * @throws IOException
     */
    public SearchResult search(List<String> indexes, String query, boolean checkIndexValidity) throws JestResultException, IOException {

        if (checkIndexValidity) {
            List<String> validIndexes = getValidIndexes(indexes);
            if (validIndexes.size() != indexes.size()) {
                logger.debug("Not all indexes are valid indexes. Passed Indexes are [ " + indexes + " ] and valid Indexes are [ "
                        + validIndexes + " ]");
            }
            return this.search(validIndexes, query);
        } else {
            return this.search(indexes, query);
        }
    }

    /**
     * Checks for existence of the index.
     *
     * @param indexName
     * @return Status of the operation. TRUE represent successful and FALSE represent failure.
     */
    public boolean isExists(String indexName) {
        JestResult result = null;
        JestClient jestClient = getJestClient();
        try {
            result = jestClient.execute(new IndicesExists.Builder(indexName).build());
            JestHttpResultErrorHandler.handle(result);
            return result.isSucceeded();
        } catch (IOException e) {
            logger.error("IOException while checking index existence [ " + indexName + " ]", e);
        } catch (JestResultException e) {
            //consuming as the handle method will log the error. and return failure.
        } finally {
            closeJestClient(jestClient);
        }
        return false;

    }

    private List<String> getValidIndexes(List<String> indexes) {
        List<String> validIndexes = new ArrayList<>();
        JestClient jestClient = getJestClient();
        try {
            for (String index : indexes) {
                boolean result = jestClient.execute(new IndicesExists.Builder(index).build()).isSucceeded();
                if (result) {
                    validIndexes.add(index);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeJestClient(jestClient);
        }
        return validIndexes;
    }

}
