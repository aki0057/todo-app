package com.example.todoapp.application;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoapp.domain.exception.TodoNotFoundException;
import com.example.todoapp.domain.model.todo.Todo;
import com.example.todoapp.domain.model.todo.value.PublicId;
import com.example.todoapp.domain.repository.TodoDomainRepository;
import com.example.todoapp.infrastructure.entity.TodoHistoryEntity;
import com.example.todoapp.infrastructure.repository.jpa.TodoHistoryJpaRepository;

@Service
public class TodoService {

	private final TodoDomainRepository todoRepository;
	private final TodoHistoryJpaRepository historyRepository;

	public TodoService(TodoDomainRepository todoRepository,
					   TodoHistoryJpaRepository historyRepository) {
		this.todoRepository = todoRepository;
		this.historyRepository = historyRepository;
	}

	// ---------------------------------------------------------------------
	// Create
	// ---------------------------------------------------------------------
	@Transactional
	public Todo createTodo(String title, String detail, LocalDate dueDate) {
		Todo newTodo = Todo.create(title, detail, dueDate);
		Todo saved = todoRepository.save(newTodo);
		// 履歴を保存
		saveHistory(saved);
		return saved;
	}

	// ---------------------------------------------------------------------
	// Update
	// ---------------------------------------------------------------------
	@Transactional
	public Todo updateTodo(String publicId, String title, String detail, LocalDate dueDate) {
		Todo todo = todoRepository.findByPublicId(new PublicId(publicId))
				.orElseThrow(() -> new TodoNotFoundException("Todo not found: " + publicId));

		int beforeVersion = todo.getVersionNumber().value();
		todo.update(title, detail, dueDate);
		Todo saved = todoRepository.save(todo);

		// 版数が増えた場合のみ履歴保存
		if (saved.getVersionNumber().value() > beforeVersion) {
			saveHistory(saved);
		}
		return saved;
	}

	// ---------------------------------------------------------------------
	// Complete
	// ---------------------------------------------------------------------
	@Transactional
	public Todo completeTodo(String publicId) {
		Todo todo = todoRepository.findByPublicId(new PublicId(publicId))
				.orElseThrow(() -> new TodoNotFoundException("Todo not found: " + publicId));

		int beforeVersion = todo.getVersionNumber().value();
		todo.complete();
		Todo saved = todoRepository.save(todo);

		if (saved.getVersionNumber().value() > beforeVersion) {
			saveHistory(saved);
		}
		return saved;
	}

	// ---------------------------------------------------------------------
	// Delete (logical)
	// ---------------------------------------------------------------------
	@Transactional
	public Todo deleteTodo(String publicId) {
		Todo todo = todoRepository.findByPublicId(new PublicId(publicId))
				.orElseThrow(() -> new TodoNotFoundException("Todo not found: " + publicId));

		int beforeVersion = todo.getVersionNumber().value();
		todo.delete();
		Todo saved = todoRepository.save(todo);

		if (saved.getVersionNumber().value() > beforeVersion) {
			saveHistory(saved);
		}
		return saved;
	}

	// ---------------------------------------------------------------------
	// Query
	// ---------------------------------------------------------------------
	@Transactional(readOnly = true)
	public Todo getTodo(String publicId) {
		return todoRepository.findByPublicId(new PublicId(publicId))
				.orElseThrow(() -> new TodoNotFoundException("Todo not found: " + publicId));
	}

	@Transactional(readOnly = true)
	public List<Todo> listActiveTodos() {
		return todoRepository.findAll()
				.stream()
				.filter(t -> !t.isDeleted())
				.collect(Collectors.toList());
	}

	// ---------------------------------------------------------------------
	// Helper
	// ---------------------------------------------------------------------
	private void saveHistory(Todo todo) {
		if (todo.getInternalId() == null) {
			// 念のため internalId がない場合は保存しない（JPA 採番前）
			return;
		}
		TodoHistoryEntity history = new TodoHistoryEntity();
		history.setInternalId(todo.getInternalId().value());
		history.setVersionNumber(todo.getVersionNumber().value());
		history.setPublicId(todo.getPublicId().value());
		history.setTitle(todo.getTitle());
		history.setDetail(todo.getDetail());
		history.setDueDate(todo.getDueDate().value());
		history.setCompletedFlag(todo.isCompleted());
		history.setDeletedFlag(todo.isDeleted());
		// createdAt / updatedAt は @CreationTimestamp / @UpdateTimestamp で管理

		historyRepository.save(history);
	}
}
