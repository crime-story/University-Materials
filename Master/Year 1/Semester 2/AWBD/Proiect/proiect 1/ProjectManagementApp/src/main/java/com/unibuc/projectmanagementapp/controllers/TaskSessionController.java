package com.unibuc.projectmanagementapp.controllers;

import com.unibuc.projectmanagementapp.config.Log;
import com.unibuc.projectmanagementapp.domain.TaskSession;
import com.unibuc.projectmanagementapp.domain.security.User;
import com.unibuc.projectmanagementapp.exceptions.ResourceNotFoundException;
import com.unibuc.projectmanagementapp.services.TaskService;
import com.unibuc.projectmanagementapp.services.TaskSessionService;
import com.unibuc.projectmanagementapp.services.UserService;
import com.unibuc.projectmanagementapp.utils.UserAuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/taskSessions")
public class TaskSessionController {

    private final TaskSessionService sessionService;
    private final TaskService taskService;
    private final UserService userService;

    @Log
    @GetMapping
    public String calendar(@RequestParam(required = false) Integer year,
                           @RequestParam(required = false) Integer month,
                           Model model) {
        String email = UserAuthenticationUtils.getLoggedUsername();
        YearMonth ym = (year != null && month != null)
                ? YearMonth.of(year, month)
                : YearMonth.now();

        model.addAttribute("currentMonth", ym);
        model.addAttribute("prevMonth", ym.minusMonths(1));
        model.addAttribute("nextMonth", ym.plusMonths(1));

        Map<LocalDate, TaskSession> map = sessionService
                .findByUserEmail(email).stream()
                .filter(s -> YearMonth.from(s.getDate()).equals(ym))
                .collect(Collectors.toMap(TaskSession::getDate, s -> s));

        model.addAttribute("taskSessions", map);
        model.addAttribute("tasks", taskService.findAll(Pageable.ofSize(100)).getContent());
        model.addAttribute("taskSession", new TaskSession());
        return "taskSessions";
    }

    @PostMapping
    public String save(@ModelAttribute TaskSession ses) {
        String email = UserAuthenticationUtils.getLoggedUsername();
        User u = userService.getByEmail(email);
        ses.setUser(u);

        TaskSession exists = sessionService.findByUserAndDate(email, ses.getDate());
        if (exists != null) {
            ses.setId(exists.getId());
        }
        sessionService.save(ses);
        return "redirect:/taskSessions?year=" + ses.getDate().getYear() +
                "&month=" + ses.getDate().getMonthValue();
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<String> delete(@PathVariable String date) {
        String email = UserAuthenticationUtils.getLoggedUsername();
        TaskSession s = sessionService.findByUserAndDate(email, LocalDate.parse(date));
        if (s == null) throw new ResourceNotFoundException("Session not found: " + date);
        sessionService.delete(s.getId());
        return ResponseEntity.ok().build();
    }
}
