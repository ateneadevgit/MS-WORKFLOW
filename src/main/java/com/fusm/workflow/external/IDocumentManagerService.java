package com.fusm.workflow.external;

import com.fusm.workflow.model.external.DocumentRequest;
import org.springframework.stereotype.Service;

@Service
public interface IDocumentManagerService {

    String saveFile(DocumentRequest documentRequest);
    String getTemplate(Integer templateId);

}
