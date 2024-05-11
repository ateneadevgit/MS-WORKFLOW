package com.fusm.workflow.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Template {

    private Integer templateId;
    private String templateName;
    private String description;
    private String subject;
    private String emailBody;

}
