package com.fusm.workflow.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequest {

    private String idUser;
    private String documentExtension;
    private String documentVersion;
    private String documentBytes;

}
