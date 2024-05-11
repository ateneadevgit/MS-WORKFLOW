package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Campus;
import com.fusm.workflow.repository.ICampusRepository;
import com.fusm.workflow.service.ICampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusService implements ICampusService {

    @Autowired
    private ICampusRepository campusRepository;


    @Override
    public void disableCampus(List<Integer> campusId, Integer programId) {
        List<Campus> campusList = campusRepository.findCampusNotIncluded(campusId.toArray(new Integer[0]), programId);
        for (Campus campus : campusList) {
            campus.setEnabled(false);
            campusRepository.save(campus);
        }
    }

}
