package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramUserData {

    private String programName;
    private String code;
    private String email;
    private int doneCredits;
    private int totalCredits;
    private double totalProgress;

}
