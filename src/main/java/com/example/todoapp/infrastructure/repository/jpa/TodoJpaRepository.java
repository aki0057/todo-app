package com.example.todoapp.infrastructure.repository.jpa;

import com.example.todoapp.infrastructure.entity.TodoEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoJpaRepository extends JpaRepository<TodoEntity, Integer> {
    Optional<TodoEntity> findByInternalId(Integer internalId);

    Optional<TodoEntity> findByPublicId(String publicId);
}
