package com.fusm.workflow.external;

import com.fusm.workflow.model.UserGroup;
import com.fusm.workflow.model.external.CodeModel;
import com.fusm.workflow.model.external.SubjectData;
import com.fusm.workflow.model.external.UserByRoleModel;
import com.fusm.workflow.model.external.UserSinuData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISinuService {

    List<UserByRoleModel> getUserByRole(Integer roleId);
    CodeModel getSubjectsCodeByUserId(String userId);
    CodeModel getTeacherIdBySubjectCode(String subjectCode);
    CodeModel getTeacherOfStudentBYSubjectCode(UserGroup userGroup);
    List<SubjectData> getCurrentSubject(String userId);
    List<SubjectData> getHistorySubject(String userId);
    UserSinuData getUserData(String userId);

}
