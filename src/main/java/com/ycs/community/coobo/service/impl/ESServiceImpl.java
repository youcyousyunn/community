package com.ycs.community.coobo.service.impl;

import com.ycs.community.coobo.domain.dto.ESPageRequestDto;
import com.ycs.community.coobo.domain.dto.ESPageResponseDto;
import com.ycs.community.coobo.service.ESService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ESServiceImpl implements ESService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public ESPageResponseDto qryContentPage(ESPageRequestDto request) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        // 构建查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 分页
        searchSourceBuilder.from(request.getCurrentPage());
        searchSourceBuilder.size(request.getPageSize());

        // 精确匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", request.getKeyword());
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(20));

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.requireFieldMatch(false); // 关闭除了第一个匹配词高亮
        searchSourceBuilder.highlighter(highlightBuilder);

        // 执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 组装结果集
        ESPageResponseDto responsePageDto = new ESPageResponseDto();
        List<Map<String, Object>> list = new ArrayList<>();
        for(SearchHit item : response.getHits().getHits()) {
            Map<String, Object> resultMap = item.getSourceAsMap();
            // 获取高亮字段
            Map<String, HighlightField> map = item.getHighlightFields();
            HighlightField highlightField = map.get("name");
            if (!StringUtils.isEmpty(highlightBuilder)) {
                Text[] texts = highlightField.fragments();
                String highLightName = "";
                for(Text text : texts) {
                    highLightName += text;
                }
                resultMap.put("name", highLightName);
            }

            list.add(resultMap);
        }
        responsePageDto.setData(list);
        responsePageDto.setTotal(list.size());
        return responsePageDto;
    }
}
