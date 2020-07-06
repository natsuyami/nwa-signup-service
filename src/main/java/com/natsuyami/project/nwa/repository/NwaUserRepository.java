package com.natsuyami.project.nwa.repository;

import com.natsuyami.project.nwa.model.NwaUserModel;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NwaUserRepository extends JpaRepository<NwaUserModel, Long> {
  List<NwaUserModel> findByCreated(Date created);
}
