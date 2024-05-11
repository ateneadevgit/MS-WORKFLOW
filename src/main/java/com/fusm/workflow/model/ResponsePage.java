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
public class ResponsePage<T> {

    private List<T> content;
    private Integer numberOfPage;
    private Integer itemsPerPage;
    private Integer itemsOnThisPage;
    private Integer totalNumberPages;
    private Long totalNumberItems;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;

}
