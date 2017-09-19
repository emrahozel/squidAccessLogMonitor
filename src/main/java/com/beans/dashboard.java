package com.beans;

import com.model.accessLogModel;
import com.service.clientProvider;
import com.util.byteConverter;
import com.util.config;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;
import org.primefaces.extensions.component.gchart.model.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by emrahozel on 16.09.2017.
 */
@ManagedBean
@RequestScoped
public class dashboard implements Serializable {

    private static final long serialVersionUID = 253762400419864192L;

    private GChartModel topRegionChart = null;
    private GChartModel topWebChart = null;
    private List<accessLogModel> topWanHost = new ArrayList<>();
    private List<accessLogModel> topLanHost = new ArrayList<>();
    private List<accessLogModel> content = new ArrayList<>();


    @PostConstruct
    public void init() {
        Map<String, Object> colorAxis = new HashMap<String, Object>();
        colorAxis.put("colors", new String[] { "white", "orange" });
        topRegionChart = new GChartModelBuilder().setChartType(GChartType.GEO)
                .addColumns("Country", "Popularity")
                .addOption("colorAxis", colorAxis)
                .build();
        topWebChart = new GChartModelBuilder().setChartType(GChartType.PIE)
                .addColumns("Term","Count").build();
        clientProvider.instance().getClient()
                .admin().indices().prepareRefresh().execute().actionGet();
        config conf = new config();
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("*");

        SearchRequestBuilder searchRequestBuilder = clientProvider.instance().getClient().prepareSearch(conf.indexName);
        searchRequestBuilder.setTypes(conf.typeName);
        searchRequestBuilder.setSearchType(SearchType.QUERY_AND_FETCH);
        searchRequestBuilder.setQuery(queryBuilder).addSort("timestamp", SortOrder.DESC);
        searchRequestBuilder.setFrom(0).setSize(conf.maxLineSize);

        searchRequestBuilder.addAggregation(AggregationBuilders.terms("countryName").field("location.keyword"));
        searchRequestBuilder.addAggregation(AggregationBuilders.terms("topweb").field("dst_host.keyword").size(10));
        searchRequestBuilder.addAggregation(AggregationBuilders.terms("topWanHost").field("dst_ip.keyword").size(10)
                .subAggregation(AggregationBuilders.terms("country").field("location.keyword"))
                .subAggregation(AggregationBuilders.sum("byteSize").field("bytes")));
        searchRequestBuilder.addAggregation(AggregationBuilders.terms("topLanHost").field("src_ip.keyword").size(10)
                .subAggregation(AggregationBuilders.sum("lanByteSize").field("bytes")));
        searchRequestBuilder.addAggregation(AggregationBuilders.terms("content").field("content.keyword"));
        SearchResponse response = searchRequestBuilder.execute().actionGet();

        //Destination Region

        Terms countryNameTerm = response.getAggregations().get("countryName");
        chartData(countryNameTerm, topRegionChart);

        //TopWeb
        Terms topWebTerm = response.getAggregations().get("topweb");
        chartData(topWebTerm, topWebChart);

        //Top Wan Host
        Terms topWanHostTerm = response.getAggregations().get("topWanHost");

        for(Terms.Bucket entry : topWanHostTerm.getBuckets()){
            Sum bandwidthAgg = entry.getAggregations().get("byteSize");
            Terms countryTerm = entry.getAggregations().get("country");

            if(bandwidthAgg.getValue() != 0.0)
                topWanHost.addAll(countryTerm.getBuckets().stream().map(entry2 -> new accessLogModel(entry.getKeyAsString(),
                        byteConverter.convertToStringRepresentation((long) bandwidthAgg.getValue()),
                        entry.getDocCount(), entry2.getKeyAsString())).collect(Collectors.toList()));
        }

        //top Lan Host
        Terms topLanHostTerm = response.getAggregations().get("topLanHost");
        for(Terms.Bucket entry : topLanHostTerm.getBuckets()){
            Sum bandwidthAgg = entry.getAggregations().get("lanByteSize");
            topLanHost.add(new accessLogModel(entry.getKeyAsString(),
                    byteConverter.convertToStringRepresentation((long) bandwidthAgg.getValue()), entry.getDocCount()));
        }

        //Content
        Terms contentTerm = response.getAggregations().get("content");
        for(Terms.Bucket entry : contentTerm.getBuckets()){
            content.add(new accessLogModel(entry.getKeyAsString(),entry.getDocCount()));
        }
    }


    public void chartData(Terms term, GChartModel model){
        for(Terms.Bucket entry : term.getBuckets()){
            Collection<Object> o = new ArrayList<>();
            o.add(entry.getDocCount());
            GChartModelRow rows = new DefaultGChartModelRow(entry.getKeyAsString(),o);
            model.getRows().add(rows);
        }
    }

    public GChartModel getTopRegionChart() {
        return topRegionChart;
    }

    public GChartModel getTopWebChart() {
        return topWebChart;
    }

    public void setTopWebChart(GChartModel topWebChart) {
        this.topWebChart = topWebChart;
    }

    public List<accessLogModel> getTopWanHost() {
        return topWanHost;
    }

    public void setTopWanHost(List<accessLogModel> topWanHost) {
        this.topWanHost = topWanHost;
    }

    public List<accessLogModel> getTopLanHost() {
        return topLanHost;
    }

    public void setTopLanHost(List<accessLogModel> topLanHost) {
        this.topLanHost = topLanHost;
    }

    public List<accessLogModel> getContent() {
        return content;
    }

    public void setContent(List<accessLogModel> content) {
        this.content = content;
    }
}
