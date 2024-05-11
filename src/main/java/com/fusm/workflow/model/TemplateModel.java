package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateModel {

    private String templateName;
    private String templateUrl;
    private String description;
    private List<TemplateModel> templateChild;

}
