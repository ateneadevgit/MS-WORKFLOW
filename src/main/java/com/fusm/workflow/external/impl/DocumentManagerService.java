package com.fusm.workflow.external.impl;

import com.fusm.workflow.external.IDocumentManagerService;
import com.fusm.workflow.model.external.DocumentRequest;
import com.fusm.workflow.webclient.WebClientConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DocumentManagerService implements IDocumentManagerService {

    @Autowired
    private WebClientConnector webClientConnector;

    @Value("${ms-document.complete-path}")
    private String DOCUMENT_MANAGER_ROUTE;

    @Value("${ms-document.path}")
    private String DOCUMENT_MANAGER_SERVICE;

    @Override
    public String saveFile(DocumentRequest documentRequest) {
        return webClientConnector.connectWebClient(DOCUMENT_MANAGER_ROUTE)
                .post()
                .uri(DOCUMENT_MANAGER_SERVICE)
                .bodyValue(documentRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public String getTemplate(Integer templateId) {
        return webClientConnector.connectWebClient(DOCUMENT_MANAGER_ROUTE)
                .get()
                .uri(DOCUMENT_MANAGER_SERVICE + "/pdf/" + templateId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
