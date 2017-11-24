package com.searchable.objects.core.service.elastic.search;

/**
 * @auther Archan on 24/11/17.
 */
public class SearchQuery {

    public enum Type {
        DEFAULT("{\"query\":{\"query_string\":{\"query\":\"{queryString}\",\"default_operator\":\"and\"}}}"),
        OTHER("{\"query\":{\"query_string\":{\"query\":\"{queryString}\",\"default_operator\":\"and\"}}}");

        private String queryTemplate;

        Type(String queryTemplate) {
            this.queryTemplate = queryTemplate;
        }

        public String getQueryTemplate() {
            return queryTemplate;
        }
    }

    ;

    public static String getSearchQuery(Type searchType, String queryString) {
        return searchType.getQueryTemplate().replaceAll("\\{queryString\\}", queryString);
    }

    public static String getSearchQuery(String queryString) {
        return Type.DEFAULT.getQueryTemplate().replaceAll("\\{queryString\\}", queryString);
    }
}

