package com.fusm.workflow.external.impl;

import com.fusm.workflow.external.INotificationService;
import com.fusm.workflow.model.external.NotificationRequest;
import com.fusm.workflow.model.external.Template;
import com.fusm.workflow.webclient.WebClientConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private WebClientConnector webClientConnector;

    @Value("${ms-notification.complete-path}")
    private String NOTIFICATION_ROUTE;

    @Value("${ms-notification.notification.path}")
    private String NOTIFICATION_SERVICE;

    @Value("${ms-notification.template.path}")
    private String TEMPLATE_SERVICE;


    @Override
    public String sendNotification(NotificationRequest notificationRequest) {
        return webClientConnector.connectWebClient(NOTIFICATION_ROUTE)
                .post()
                .uri(NOTIFICATION_SERVICE)
                .bodyValue(notificationRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Template getTemplate(Integer templateId) {
        return webClientConnector.connectWebClient(NOTIFICATION_ROUTE)
                .get()
                .uri(TEMPLATE_SERVICE + "/" + templateId)
                .retrieve()
                .bodyToMono(Template.class)
                .block();
    }

}

