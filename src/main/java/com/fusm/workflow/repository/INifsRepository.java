package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Nifs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INifsRepository extends JpaRepository<Nifs, Integer> {

    List<Nifs> findAllByType(Integer type);

    List<Nifs> findAllByNifFather_NifsIdAndEnabled(Integer fatherId, Boolean enabled);

}
