package com.example.gad.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.PostMedia;

public interface PostMediaRepository extends JpaRepository<PostMedia, UUID> {

    List<PostMedia> findByPost_Id(UUID postId);
}