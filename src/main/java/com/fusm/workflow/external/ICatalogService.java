package com.fusm.workflow.external;

import org.springframework.stereotype.Service;

@Service
public interface ICatalogService {

    String getCatalogItemValue(Integer catalogItemId);

}
