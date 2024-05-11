package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Nifs;
import com.fusm.workflow.model.NifsModel;
import com.fusm.workflow.model.NifsRequest;
import com.fusm.workflow.repository.INifsRepository;
import com.fusm.workflow.service.INifsService;
import com.fusm.workflow.util.SharedMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NifsService implements INifsService {

    @Autowired
    private INifsRepository nifsRepository;

    @Autowired
    private SharedMethods sharedMethods;


    @Override
    public void createNifsData(NifsRequest nifsRequest) {
        Nifs mainNifs = nifsRepository.save(saveNifs(nifsRequest, null, nifsRequest.getCreatedBy()));
        for (NifsRequest request : nifsRequest.getSections()) nifsRepository.save(saveNifs(request, mainNifs, nifsRequest.getCreatedBy()));
    }

    @Override
    public void updateNifsData(NifsRequest nifsRequest, Integer nifId) {
        Optional<Nifs> nifsOptional = nifsRepository.findById(nifId);
        if (nifsOptional.isPresent()) {
            Nifs nifs = nifsOptional.get();
            if (nifsRequest.getContent() != null) nifs.setContent(nifsRequest.getContent());
            if (nifsRequest.getImage() != null) nifs.setImage(sharedMethods.saveFile(nifsRequest.getImage(), nifsRequest.getCreatedBy()));
            nifsRepository.save(nifs);
        }
    }

    @Override
    public void addSection(List<NifsRequest> nifsRequests, Integer nifId) {
        Optional<Nifs> nifsOptional = nifsRepository.findById(nifId);
        if (nifsOptional.isPresent()) {
            Nifs nifs = nifsOptional.get();
            for (NifsRequest request : nifsRequests) nifsRepository.save(saveNifs(request, nifs, request.getCreatedBy()));
        }
    }

    @Override
    public void disableSection(Integer nifId) {
        Optional<Nifs> nifsOptional = nifsRepository.findById(nifId);
        if (nifsOptional.isPresent()) {
            Nifs nifs = nifsOptional.get();
            nifs.setEnabled(false);
            nifsRepository.save(nifs);
        }
    }

    @Override
    public NifsModel viewNifsData(Integer type) {
        List<Nifs> nifMain = nifsRepository.findAllByType(type);
        NifsModel nifsModel = new NifsModel();
        if (!nifMain.isEmpty()) {
            Nifs root = nifMain.get(0);
            nifsModel = NifsModel.builder()
                    .nifsId(root.getNifsId())
                    .image(root.getImage())
                    .content(root.getContent())
                    .sections(findNifsSection(root.getNifsId()))
                    .build();
        }
        return nifsModel;
    }

    private Nifs saveNifs(NifsRequest nifsRequest, Nifs father, String createdBy) {
        return Nifs.builder()
                .image(sharedMethods.saveFile(nifsRequest.getImage(), createdBy))
                .content(nifsRequest.getContent())
                .type(nifsRequest.getType())
                .createdBy(createdBy)
                .createdAt(new Date())
                .nifFather(father)
                .enabled(true)
                .build();
    }

    private List<NifsModel> findNifsSection(Integer nifsId) {
        return nifsRepository.findAllByNifFather_NifsIdAndEnabled(nifsId, true).stream().map(
                nifs -> NifsModel.builder()
                        .nifsId(nifs.getNifsId())
                        .image(nifs.getImage())
                        .content(nifs.getContent())
                        .build()
        ).toList();
    }

}
