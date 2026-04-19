package com.lms.course.repository;

import com.lms.course.entity.CModule;
import com.lms.course.entity.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {
    List<ForumThread> findByModuleId(Long moduleId);
    List<ForumThread> findByModule(CModule module);
    List<ForumThread> findByAuthorId(String authorId);
}
