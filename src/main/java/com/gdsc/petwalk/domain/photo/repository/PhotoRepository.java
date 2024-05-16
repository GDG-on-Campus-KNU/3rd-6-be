package com.gdsc.petwalk.domain.photo.repository;

import com.gdsc.petwalk.domain.photo.entity.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByWalkInvitationId(Long walkInvitationId);
}
