package com.ycs.community.coobo.service.impl;

import cn.hutool.json.JSONUtil;
import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.coobo.domain.dto.ESPageRequestDto;
import com.ycs.community.coobo.domain.dto.ESPageResponseDto;
import com.ycs.community.coobo.domain.dto.ESRequestDto;
import com.ycs.community.coobo.domain.po.JDDocumentPo;
import com.ycs.community.coobo.service.ESService;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.postTags("</span>");
        //highlightBuilder.requireFieldMatch(false); // 关闭除了第一个匹配词高亮
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
                resultMap.put("hlName", highLightName);
            }

            list.add(resultMap);
        }
        responsePageDto.setData(list);
        int total = (int) response.getHits().getTotalHits().value;
        responsePageDto.setTotal(total);
        return responsePageDto;
    }

    @Override
    public boolean addDoc(ESRequestDto request) throws IOException {
        JDDocumentPo documentPo = BeanUtil.trans2Entity(request, JDDocumentPo.class);
        documentPo.setImg(new Date().getTime() + "");
        IndexRequest indexRequest = new IndexRequest(Constants.JD_INDEX, Constants.JD_TYPE);
        indexRequest.id(documentPo.getId());
        indexRequest.source(JSONUtil.toJsonStr(documentPo), XContentType.JSON);
        indexRequest.timeout(TimeValue.timeValueSeconds(10));
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        RestStatus status = response.status();
        if (status.getStatus() != 200) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_DOC_FAIL, "添加文档失败");
        }
        return true;
    }

    @Override
    public boolean delDocById(String id) throws IOException {
        GetRequest request = new GetRequest(Constants.JD_INDEX, Constants.JD_TYPE, id);
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        // 判断文档是否存在
        if (!response.isExists()) {
            throw new CustomizeBusinessException(HiMsgCdConstants.DOC_NOT_EXIST, "文档不存在");
        }
        // 删除文档
        DeleteRequest deleteRequest = new DeleteRequest(Constants.JD_INDEX, Constants.JD_TYPE, id);
        deleteRequest.timeout(TimeValue.timeValueSeconds(6));
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        RestStatus status = deleteResponse.status();
        if (status.getStatus() != 200) {
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_DOC_FAIL, "删除文档失败");
        }
        return true;
    }

    @Override
    public boolean updDoc(ESRequestDto request) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(Constants.JD_INDEX, Constants.JD_TYPE, request.getId());
        JDDocumentPo documentPo = BeanUtil.trans2Entity(request, JDDocumentPo.class);
        updateRequest.doc(JSONUtil.toJsonStr(documentPo), XContentType.JSON);
        updateRequest.timeout(TimeValue.timeValueSeconds(10));
        UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        RestStatus status = response.status();
        if (status.getStatus() != 200) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_DOC_FAIL, "更新文档失败");
        }
        return true;
    }
}
