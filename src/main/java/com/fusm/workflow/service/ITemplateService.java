package com.fusm.workflow.service;

import com.fusm.workflow.model.TemplateModel;
import com.fusm.workflow.model.TemplateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITemplateService {

    List<TemplateModel> getTemplatesByStep(Integer stepId);
    void createTemplate(TemplateRequest templateRequest);
    void updateTemplate(TemplateRequest templateRequest, Integer templateId);

}
