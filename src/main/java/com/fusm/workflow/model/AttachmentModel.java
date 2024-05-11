package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentModel {

    private Integer attachId;
    private String name;
    private Date createdDate;
    private String urlFile;
    private Integer status;
    private Integer dcStatus;
    private Integer vaStatus;
    private Integer acStatus;
    private String version;
    private Boolean enabled;
    private Integer roleId;
    private Boolean isOriginal;
    private String createdBy;
    private Integer attachFatherId;
    private Boolean isDeclined;
    private Boolean isSent;
    private List<AttachmentModel> attachmentChild;

}
