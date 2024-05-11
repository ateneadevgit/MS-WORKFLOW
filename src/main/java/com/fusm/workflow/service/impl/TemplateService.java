package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Step;
import com.fusm.workflow.entity.Template;
import com.fusm.workflow.model.TemplateModel;
import com.fusm.workflow.model.TemplateRequest;
import com.fusm.workflow.repository.IStepRepository;
import com.fusm.workflow.repository.ITemplateRepository;
import com.fusm.workflow.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TemplateService implements ITemplateService {

    @Autowired
    private ITemplateRepository templateRepository;

    @Autowired
    private IStepRepository stepRepository;


    @Override
    public List<TemplateModel> getTemplatesByStep(Integer stepId) {
        List<Template> templateList = templateRepository.findByStepId(stepId);
        Map<Integer, TemplateModel> templateFather = new HashMap<>();

        for (Template templateDto : templateList) {
            if (templateDto.getTemplateFatherId() == null) {
                templateFather.put(templateDto.getTemplateId(), buildTemplate(templateDto));
            }
        }

        for (Template templateDto : templateList) {
            if (templateDto.getTemplateFatherId() != null) {
                TemplateModel father = templateFather.get(templateDto.getTemplateFatherId().getTemplateId());
                if (father!= null) father.getTemplateChild().add(buildTemplate(templateDto));
            }
        }

        List<TemplateModel> templateModels = new ArrayList<>(templateFather.values());
        Comparator<TemplateModel> templateComparator = Comparator.comparing(TemplateModel::getTemplateName);
        templateModels.sort(templateComparator);
        return templateModels;
    }

    @Override
    public void createTemplate(TemplateRequest templateRequest) {
        Optional<Step> stepOptional = stepRepository.findById(templateRequest.getStepId());
        stepOptional.ifPresent(step -> templateRepository.save(
                Template.builder()
                        .templateName(" - ")
                        .description(templateRequest.getTemplate())
                        .stepId(step)
                        .enabled(templateRequest.getEnabled())
                        .build()
        ));
    }

    @Override
    public void updateTemplate(TemplateRequest templateRequest, Integer templateId) {
        Optional<Template> templateOptional = templateRepository.findById(templateId);
        if (templateOptional.isPresent()) {
            Template template = templateOptional.get();
            template.setDescription(templateRequest.getTemplate());
            template.setEnabled(templateRequest.getEnabled());
            templateRepository.save(template);
        }
    }

    private TemplateModel buildTemplate(Template templateDto) {
        return TemplateModel.builder()
                .templateName(templateDto.getTemplateName())
                .templateUrl(templateDto.getTemplateUrl())
                .description(templateDto.getDescription())
                .templateChild(new ArrayList<>())
                .build();
    }

}
