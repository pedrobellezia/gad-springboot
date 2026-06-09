package com.example.gad.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.PostMedia;
import com.example.gad.models.projection.PostMediaProjection;

public interface PostMediaRepository extends JpaRepository<PostMedia, UUID> {

    List<PostMediaProjection> findProjectedByPost_Id(UUID postId);

    List<PostMediaProjection> findAllProjectedBy();
}
