package com.fusm.workflow.service;

import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;

@Service
public interface IPdfService {

    String savePdf(String pfdTemplate) throws DocumentException;

}
