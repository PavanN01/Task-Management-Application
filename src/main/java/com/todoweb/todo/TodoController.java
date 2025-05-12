package com.todoweb.todo;


import com.todoweb.todo.Model.TodoItem;
import com.todoweb.todo.Repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private final TodoItemRepository todoItemRepository;

    @Autowired
    public TodoController(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    @GetMapping
    public String listTodos(Model model) {
        List<TodoItem> todos = todoItemRepository.findAll();
        model.addAttribute("todos", todos);
        model.addAttribute("newTodo", new TodoItem()); // For the form
        return "todo-list";
    }

    @PostMapping("/add")
    public String addTodo(@ModelAttribute("newTodo") TodoItem todoItem) {
        todoItemRepository.save(todoItem);
        return "redirect:/todos";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable("id") Long id) {
        TodoItem todo = todoItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid todo Id:" + id));
        todo.setCompleted(!todo.isCompleted());
        todoItemRepository.save(todo);
        return "redirect:/todos";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("id") Long id) {
        todoItemRepository.deleteById(id);
        return "redirect:/todos";
    }
}