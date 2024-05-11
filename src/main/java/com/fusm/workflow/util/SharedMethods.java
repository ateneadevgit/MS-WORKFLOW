package com.fusm.workflow.util;

import com.fusm.workflow.external.ICatalogService;
import com.fusm.workflow.external.IDocumentManagerService;
import com.fusm.workflow.external.ISettingsService;
import com.fusm.workflow.model.ActionModel;
import com.fusm.workflow.model.FileModel;
import com.fusm.workflow.model.external.DocumentRequest;
import com.fusm.workflow.model.external.SettingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class SharedMethods {

    @Autowired
    private ISettingsService settingsService;

    @Autowired
    private ICatalogService catalogService;

    @Autowired
    private IDocumentManagerService documentManagerService;


    public Integer getSettingValue(String settingName) {
        return Integer.parseInt(
                settingsService.getSetting(
                        SettingRequest.builder()
                                .settingName(settingName)
                                .build()
                )
        );
    }

    public String getSettingValueOnString(String settingName) {
        return settingsService.getSetting(
                        SettingRequest.builder()
                                .settingName(settingName)
                                .build()
                );
    }

    public Boolean hasAction (Integer actionId, List<ActionModel> actions) {
        boolean hasAction = false;
        for (ActionModel action : actions) {
            if (Objects.equals(action.getActionId(), actionId)) {
                hasAction = true;
                break;
            }
        }
        return hasAction;
    }

    public String getCampusValue(List<Integer> campusIdList) {
        StringBuilder campus = new StringBuilder();
        for (Integer id : campusIdList) {
            campus.append(catalogService.getCatalogItemValue(id)).append(", ");
        }
        campus.deleteCharAt(campus.length() - 2);
        return campus.toString();
    }

    public String getModalityValue(List<Integer> modalityIdList) {
        StringBuilder modalities = new StringBuilder();
        for (Integer id : modalityIdList) {
            modalities.append(catalogService.getCatalogItemValue(id)).append(", ");
        }
        modalities.deleteCharAt(modalities.length() - 2);
        return modalities.toString();
    }

    public String saveFile(FileModel fileModel, String createdBy) {
        return documentManagerService.saveFile(
                DocumentRequest.builder()
                        .documentBytes(fileModel.getFileContent())
                        .documentExtension(fileModel.getFileExtension())
                        .documentVersion("1")
                        .idUser(createdBy)
                        .build()
        );
    }

    public String getPeriod() {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        String period = (currentMonth <= 6) ? "-I" : "-II";
        return currentYear + period;
    }

}
