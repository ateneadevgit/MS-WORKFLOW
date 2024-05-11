package com.fusm.workflow.external.impl;

import com.fusm.workflow.external.ISinuService;
import com.fusm.workflow.model.UserGroup;
import com.fusm.workflow.model.external.CodeModel;
import com.fusm.workflow.model.external.SubjectData;
import com.fusm.workflow.model.external.UserByRoleModel;
import com.fusm.workflow.model.external.UserSinuData;
import com.fusm.workflow.webclient.WebClientConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SinuService implements ISinuService {

    @Autowired
    private WebClientConnector webClientConnector;

    @Value("${ms-sinu.complete-path}")
    private String SINU_ROUTE;

    @Value("${ms-sinu.path}")
    private String SINU_SERVICE;


    @Override
    public List<UserByRoleModel> getUserByRole(Integer roleId) {
        return webClientConnector.connectWebClient(SINU_ROUTE)
                .get()
                .uri(SINU_SERVICE + "/user/by-role/" + roleId)
                .retrieve()
                .bodyToFlux(UserByRoleModel.class)
                .collectList()
                .block();
    }

    @Override
    public CodeModel getSubjectsCodeByUserId(String userId) {
        return webClientConnector.connectWebClient(SINU_ROUTE)
                .post()
                .uri(SINU_SERVICE + "/subject/by-user")
                .bodyValue(userId)
                .retrieve()
                .bodyToMono(CodeModel.class)
                .block();
    }

    @Override
    public CodeModel getTeacherIdBySubjectCode(String subjectCode) {
        return webClientConnector.connectWebClient(SINU_ROUTE)
                .post()
                .uri(SINU_SERVICE + "/user/subject-code")
                .bodyValue(subjectCode)
                .retrieve()
                .bodyToMono(CodeModel.class)
                .block();
    }

    @Override
    public CodeModel getTeacherOfStudentBYSubjectCode(UserGroup userGroup) {
        return webClientConnector.connectWebClient(SINU_ROUTE)
                .post()
                .uri(SINU_SERVICE + "/user/student/subject-code")
                .bodyValue(userGroup)
                .retrieve()
                .bodyToMono(CodeModel.class)
                .block();
    }

    @Override
    public List<SubjectData> getCurrentSubject(String userId) {
        return webClientConnector.connectWebClient(SINU_ROUTE)
                .get()
                .uri(SINU_SERVICE + "/current-subject/user/" + userId)
                .retrieve()
                .bodyToFlux(SubjectData.class)
                .collectList()
                .block();
    }

    @Override
    public List<SubjectData> getHistorySubject(String userId) {
        return webClientConnector.connectWebClient(SINU_ROUTE)
                .get()
                .uri(SINU_SERVICE + "/history-subject/user/" + userId)
                .retrieve()
                .bodyToFlux(SubjectData.class)
                .collectList()
                .block();
    }

    @Override
    public UserSinuData getUserData(String userId) {
        return webClientConnector.connectWebClient(SINU_ROUTE)
                .get()
                .uri(SINU_SERVICE + "/user/" + userId + "/data")
                .retrieve()
                .bodyToMono(UserSinuData.class)
                .block();
    }
}

