package com.fusm.workflow.model;

import lombok.*;

import javax.persistence.Column;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectActivityRequest {

    private Integer subjectActivityId;
    private Integer session;
    private Date activityDate;
    private String result;
    private String topic;
    private String syncActivities;
    private String previusActivities;
    private String strategies;
    private String url;
    private Boolean enabled;
    private String createdBy;
    private Integer roleId;
    private Boolean canUpdate;

}
