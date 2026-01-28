package com.example.todoapp.presentation.controller;

import com.example.todoapp.application.TodoService;
import com.example.todoapp.domain.model.todo.Todo;
import com.example.todoapp.presentation.form.TodoForm;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Todo の Web UI 用コントローラ。 */
@Controller
@RequestMapping("/todos")
public class TodoController {

    private static final String VIEW_TODOS_LIST = "todos/list";
    private static final String VIEW_TODOS_EDIT = "todos/edit";
    private static final String REDIRECT_TODOS = "redirect:/todos";

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // ================================================================
    // 一覧表示
    // ================================================================
    @GetMapping
    public String list(Model model) {
        List<Todo> todos = todoService.listActiveTodos();
        model.addAttribute("todos", todos);
        return VIEW_TODOS_LIST;
    }

    // ================================================================
    // 新規作成フォーム表示
    // ================================================================
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("todoForm", new TodoForm());
        model.addAttribute("isEdit", false);
        return VIEW_TODOS_EDIT;
    }

    // ================================================================
    // 新規作成実行
    // ================================================================
    @PostMapping
    public String create(
            @Valid @ModelAttribute TodoForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // バリデーションエラーがある場合
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return VIEW_TODOS_EDIT;
        }

        // 新規作成実行
        todoService.createTodo(form.getTitle(), form.getDetail(), form.getDueDate());

        redirectAttributes.addFlashAttribute("message", "Todoを作成しました");
        return REDIRECT_TODOS;
    }

    // ================================================================
    // 編集フォーム表示
    // ================================================================
    @GetMapping("/{publicId}/edit")
    public String showEditForm(@PathVariable String publicId, Model model) {
        Todo todo = todoService.getTodo(publicId);

        // TodoFormに変換
        TodoForm form = new TodoForm();
        form.setPublicId(todo.getPublicId().value());
        form.setTitle(todo.getTitle());
        form.setDetail(todo.getDetail());
        form.setDueDate(todo.getDueDate().value());

        model.addAttribute("todoForm", form);
        model.addAttribute("isEdit", true);
        return VIEW_TODOS_EDIT;
    }

    // ================================================================
    // 編集実行
    // ================================================================
    @PostMapping("/{publicId}")
    public String update(
            @PathVariable String publicId,
            @Valid @ModelAttribute TodoForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // バリデーションエラーがある場合
        if (result.hasErrors()) {
            form.setPublicId(publicId); // publicIdを保持
            model.addAttribute("isEdit", true);
            return VIEW_TODOS_EDIT;
        }

        // 編集実行
        todoService.updateTodo(publicId, form.getTitle(), form.getDetail(), form.getDueDate());

        redirectAttributes.addFlashAttribute("message", "Todoを更新しました");
        return REDIRECT_TODOS;
    }

    // ================================================================
    // 完了処理
    // ================================================================
    @PostMapping("/{publicId}/complete")
    public String complete(@PathVariable String publicId, RedirectAttributes redirectAttributes) {
        todoService.completeTodo(publicId);

        redirectAttributes.addFlashAttribute("message", "Todoを完了しました");
        return REDIRECT_TODOS;
    }

    // ================================================================
    // 削除処理
    // ================================================================
    @PostMapping("/{publicId}/delete")
    public String delete(@PathVariable String publicId, RedirectAttributes redirectAttributes) {
        todoService.deleteTodo(publicId);

        redirectAttributes.addFlashAttribute("message", "Todoを削除しました");
        return REDIRECT_TODOS;
    }
}
