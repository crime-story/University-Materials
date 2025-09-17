package com.unibuc.tasksession.controllers;

import com.unibuc.tasksession.config.Log;
import com.unibuc.tasksession.domain.TaskSession;
import com.unibuc.tasksession.domain.security.User;
import com.unibuc.tasksession.exceptions.ResourceNotFoundException;
import com.unibuc.tasksession.services.TaskSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/taskSessions")
public class TaskSessionController {

    private final TaskSessionService sessionService;

    @GetMapping("/{date}")
    public ResponseEntity<Map<String, Object>> getTaskSessionByDate(@PathVariable String date) {
        TaskSession session = sessionService.getTaskSessionByDate(LocalDate.parse(date));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", session.getId());
        response.put("date", session.getDate());
        response.put("notes", session.getNotes());
        response.put("status", session.getStatus());
        response.put("taskId", session.getTask().getId());

        return ResponseEntity.ok(response);
    }
}
