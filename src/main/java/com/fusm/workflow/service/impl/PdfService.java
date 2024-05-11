package com.fusm.workflow.service.impl;

import com.fusm.workflow.external.IDocumentManagerService;
import com.fusm.workflow.model.external.DocumentRequest;
import com.fusm.workflow.service.IPdfService;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class PdfService implements IPdfService {

    @Autowired
    private IDocumentManagerService documentManagerService;

    @Override
    public String savePdf(String pfdTemplate) throws DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(pfdTemplate);
        renderer.layout();
        renderer.createPDF(byteArrayOutputStream);
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        String base64EncodedPDF = Base64.getEncoder().encodeToString(pdfBytes);
        return documentManagerService.saveFile(
                DocumentRequest.builder()
                        .idUser(" - ")
                        .documentExtension("pdf")
                        .documentVersion("1")
                        .documentBytes(base64EncodedPDF)
                        .build()
        );
    }

}
