package com.example.todoapp.infrastructure.repository.jpa;

import com.example.todoapp.infrastructure.entity.TodoHistoryEntity;
import com.example.todoapp.infrastructure.entity.TodoHistoryId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoHistoryJpaRepository extends JpaRepository<TodoHistoryEntity, TodoHistoryId> {
    List<TodoHistoryEntity> findByInternalId(Integer internalId);
}
