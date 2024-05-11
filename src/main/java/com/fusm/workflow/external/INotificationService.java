package com.fusm.workflow.external;

import com.fusm.workflow.model.external.NotificationRequest;
import com.fusm.workflow.model.external.Template;
import org.springframework.stereotype.Service;

@Service
public interface INotificationService {

    String sendNotification(NotificationRequest notificationRequest);
    Template getTemplate(Integer templateId);

}
