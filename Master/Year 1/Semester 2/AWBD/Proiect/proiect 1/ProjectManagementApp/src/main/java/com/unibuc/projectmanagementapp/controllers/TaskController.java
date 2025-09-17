package com.unibuc.projectmanagementapp.controllers;

import com.unibuc.projectmanagementapp.config.Log;
import com.unibuc.projectmanagementapp.domain.Task;
import com.unibuc.projectmanagementapp.dtos.TaskCreateDTO;
import com.unibuc.projectmanagementapp.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Log
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new TaskCreateDTO());
        return "create-task";
    }

    @PostMapping
    public String saveTask(@Valid @ModelAttribute("task") TaskCreateDTO dto,
                           BindingResult br, Model model) {
        if (br.hasErrors()) {
            return "create-task";
        }
        taskService.saveTask(dto.toEntity());
        return "redirect:/tasks";
    }

    @GetMapping
    public String listTasks(@RequestParam(defaultValue = "asc") String sortDir, Pageable page, Model model) {
        if (page.getPageSize()>10 || page.getPageNumber()<0) {
            page = PageRequest.of(0,10);
        }
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc")? Sort.Direction.ASC:Sort.Direction.DESC,"name");
        page = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);

        Page<Task> tasks = taskService.findAll(page);
        model.addAttribute("tasks", tasks);
        model.addAttribute("pageSize", page.getPageSize());
        model.addAttribute("pageNumber", page.getPageNumber());
        model.addAttribute("sortDir", sortDir);
        return "tasks";
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("Task folosit în sesiuni, nu poate fi șters.");
        }
    }
}