package com.fusm.workflow.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface AttachmentDto {

    @Value("#{target.id_attach}")
    Integer getIdAttach();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.url}")
    String getUrl();

    @Value("#{target.created_at}")
    Date getCreatedAt();

    @Value("#{target.version}")
    String getVersion();

    @Value("#{target.attach_father}")
    Integer getAttachFather();

    @Value("#{target.enabled}")
    Boolean getEnabled();

    @Value("#{target.status}")
    Integer getStatus();

    @Value("#{target.role_id}")
    Integer getRoleId();

    @Value("#{target.is_original}")
    Boolean getIsOriginal();

    @Value("#{target.created_by}")
    String getCreatedBy();

    @Value("#{target.is_declined}")
    Boolean getIsDeclined();

    @Value("#{target.is_sent}")
    Boolean getIsSent();

}
