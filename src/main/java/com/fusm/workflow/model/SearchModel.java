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
public class SearchModel {

    private String problem;
    private Integer semester;
    private Integer roleId;
    private List<Integer> programId;

}
