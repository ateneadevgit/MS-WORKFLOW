package com.fusm.workflow.external.impl;

import com.fusm.workflow.external.ICatalogService;
import com.fusm.workflow.webclient.WebClientConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CatalogService implements ICatalogService {

    @Autowired
    private WebClientConnector webClientConnector;

    @Value("${ms-catalogs.complete-path}")
    private String CATALOG_ROUTE;

    @Value("${ms-catalogs.catalog-item.path}")
    private String CATALOG_ITEM_SERVICE;


    @Override
    public String getCatalogItemValue(Integer catalogItemId) {
        return webClientConnector.connectWebClient(CATALOG_ROUTE)
                .get()
                .uri(CATALOG_ITEM_SERVICE + "/value/" + catalogItemId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
