package com.unibuc.task.controllers;

import com.unibuc.task.domain.Task;
import com.unibuc.task.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTaskSessionByDate(@PathVariable UUID id) {
        Task task = taskService.getTaskById(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", task.getId());
        response.put("description", task.getDescription());
        response.put("siez", task.getSize());
        response.put("priority", task.getPriority());
        response.put("name", task.getName());
        response.put("version", "dev");

        return ResponseEntity.ok(response);

    }
}
