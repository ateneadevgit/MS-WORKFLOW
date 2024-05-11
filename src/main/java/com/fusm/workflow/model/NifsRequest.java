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
public class NifsRequest {

    private FileModel image;
    private String content;
    private Integer type;
    private List<NifsRequest> sections;
    private String createdBy;

}
