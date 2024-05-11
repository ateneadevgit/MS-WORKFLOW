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
public class SubcoreNifsModel {

    private Integer totalCredits;
    private Integer totalSubject;
    private List<CurriculumRequest> subjects;

}
