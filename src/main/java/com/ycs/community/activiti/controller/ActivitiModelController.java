package com.ycs.community.activiti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ycs.community.activiti.domain.dto.ActivitiModelRequestDto;
import com.ycs.community.activiti.domain.dto.ActivitiModelResponseDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageResponseDto;
import com.ycs.community.activiti.service.ActivitiModelService;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import io.swagger.annotations.Api;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Api(tags = "流程管理: 流程管理")
public class ActivitiModelController {
    @Autowired
    private ActivitiModelService activitiModelService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 分页查询流程列表
     * @param request
     * @return
     */
    @GetMapping("/model/queryPage")
    public QryActivitiModelPageResponseDto queryModelPage(QryActivitiModelPageRequestDto request) {
        QryActivitiModelPageResponseDto responseDto = new QryActivitiModelPageResponseDto();
        responseDto = activitiModelService.qryActivitiModelPage(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 删除流程
     * @param id
     * @return
     */
    @DeleteMapping("/model/{id}")
    public ActivitiModelResponseDto delModel(@PathVariable("id") String id) {
        ActivitiModelResponseDto responseDto = new ActivitiModelResponseDto();
        repositoryService.deleteModel(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 部署流程
     * @param request
     * @return
     * @throws IOException
     */
    @PutMapping("/model/deploy")
    public ActivitiModelResponseDto deployModel(@RequestBody ActivitiModelRequestDto request) throws IOException {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        ActivitiModelResponseDto responseDto = new ActivitiModelResponseDto();
        activitiModelService.deployModel(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 新增流程
     * @param modelId
     * @param name
     * @param json_xml
     * @param svg_xml
     * @param description
     */
    @PostMapping(value = { "/model/{modelId}" })
    public void addModel(@PathVariable String modelId, @RequestParam("name") String name,
                          @RequestParam("json_xml") String json_xml, @RequestParam("svg_xml") String svg_xml,
                          @RequestParam("description") String description) {
        try {
            Model model = repositoryService.getModel(modelId);
            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelJson.put("name", name);
            modelJson.put("description", description);
            model.setMetaInfo(modelJson.toString());
            model.setName(name);

            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));

            InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);
            transcoder.transcode(input, output);

            byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();
        } catch (Exception e) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_FLOW_FAIL, "添加流程失败");
        }
    }

    /**
     * 打开创建流程视图
     * @param request
     * @param response
     */
    @PostMapping("/model")
    public void createModel(@RequestBody HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, "name");
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "description");

            Model model = repositoryService.newModel();
            model.setMetaInfo(modelObjectNode.toString());
            model.setName("name");
            model.setKey(StringUtils.defaultString("key"));

            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes("utf-8"));

            request.setAttribute("modelId", model.getId());
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + model.getId());
        } catch (Exception e) {
            throw new CustomizeBusinessException(HiMsgCdConstants.OPEN_CREATE_FLOW_VIEW_FAIL, "打开创建流程视图失败");
        }
    }
}
