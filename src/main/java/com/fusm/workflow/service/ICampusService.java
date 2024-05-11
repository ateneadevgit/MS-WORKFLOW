package com.fusm.workflow.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICampusService {

    void disableCampus(List<Integer> campusId, Integer programId);

}
