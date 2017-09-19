package com.beans;

import com.model.accessLogModel;
import com.service.clientProvider;
import com.util.config;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emrahozel on 18.09.2017.
 */
@ManagedBean
@ViewScoped
public class report implements Serializable {

    private List<accessLogModel> reportList = new ArrayList<>();
    private String wildcard = "*";


    @PostConstruct
    public void init(){
        prepareReportList();
    }

    public void prepareReportList(){
        reportList.clear();
        accessLogModel model;
        config conf = new config();
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(wildcard);

        SearchRequestBuilder searchRequestBuilder = clientProvider.instance().getClient().prepareSearch(conf.indexName);
        searchRequestBuilder.setTypes(conf.typeName);
        searchRequestBuilder.setSearchType(SearchType.QUERY_AND_FETCH);
        searchRequestBuilder.setQuery(queryBuilder).addSort("timestamp", SortOrder.DESC);
        searchRequestBuilder.setFrom(0).setSize(conf.maxLineSize);
        SearchResponse response = searchRequestBuilder.execute().actionGet();

        if(response != null){
            for(SearchHit hit : response.getHits().getHits()){
                model = new accessLogModel();
                model.setTimestamp(hit.getSource().get("timestamp").toString());
                model.setUser(hit.getSource().get("user").toString());
                model.setSrc_ip(hit.getSource().get("src_ip").toString());
                model.setDst_ip(hit.getSource().get("dst_ip").toString());
                model.setDst_host(hit.getSource().get("dst_host").toString());
                model.setRequest_url(hit.getSource().get("request_url").toString());
                model.setStatus_code(hit.getSource().get("status_code").toString());
                model.setMethod(hit.getSource().get("method").toString());
                model.setContent(hit.getSource().get("content").toString());
                model.setLocation(hit.getSource().get("location").toString());
                model.setBytes(Long.parseLong(hit.getSource().get("bytes").toString()));

                reportList.add(model);
            }
        }
    }

    public void search(){
        wildcard = "*"+wildcard+"*";
        prepareReportList();
    }

    public List<accessLogModel> getReportList() {
        return reportList;
    }

    public void setReportList(List<accessLogModel> reportList) {
        this.reportList = reportList;
    }

    public String getWildcard() {
        return wildcard;
    }

    public void setWildcard(String wildcard) {
        this.wildcard = wildcard;
    }
}
