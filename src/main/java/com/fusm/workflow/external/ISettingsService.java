package com.fusm.workflow.external;

import com.fusm.workflow.model.external.SettingRequest;
import org.springframework.stereotype.Service;

@Service
public interface ISettingsService {
    String getSetting(SettingRequest settingRequest);
}
