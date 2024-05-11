package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubjectData {

    private Integer subjectId;
    private String name;
    private Integer coreId;
    private String coreName;
    private Integer credits;
    private Double qalification;
    private String period;

}
