package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachRequest {

    private String name;
    private String version;
    private String extension;
    private String bytes;
    private Integer fatherId;
    private String feedback;

}
