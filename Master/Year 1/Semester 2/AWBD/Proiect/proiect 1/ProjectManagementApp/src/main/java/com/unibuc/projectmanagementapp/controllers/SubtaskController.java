package com.unibuc.projectmanagementapp.controllers;

import com.unibuc.projectmanagementapp.config.Log;
import com.unibuc.projectmanagementapp.domain.Skill;
import com.unibuc.projectmanagementapp.domain.Subtask;
import com.unibuc.projectmanagementapp.dtos.SkillDTO;
import com.unibuc.projectmanagementapp.dtos.SubtaskDTO;
import com.unibuc.projectmanagementapp.dtos.SubtaskCreateDTO;
import com.unibuc.projectmanagementapp.exceptions.ResourceNotFoundException;
import com.unibuc.projectmanagementapp.services.SubtaskService;
import com.unibuc.projectmanagementapp.services.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subtasks")
public class SubtaskController {

    private final SubtaskService subtaskService;
    private final SkillService skillService;

    @Log
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("subtask", new SubtaskCreateDTO());
        model.addAttribute("skills", skillService.getAllSkills());
        return "create-subtask";
    }

    @PostMapping
    public String saveSubtask(@Valid SubtaskCreateDTO dto,
                              BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("skills", skillService.getAllSkills());
            return "create-subtask";
        }

        var skills = new HashSet<>(skillService.findAllById(dto.getSkillIds()));
        if (skills.size() != new HashSet<>(dto.getSkillIds()).size()) {
            br.rejectValue("skillIds", "invalid", "Unele skill-uri nu există.");
            model.addAttribute("skills", skillService.getAllSkills());
            return "create-subtask";
        }

        Subtask st = dto.toEntity(skills);
        subtaskService.saveSubtask(st);
        return "redirect:/subtasks";
    }


    @GetMapping
    public String listSubtasks(@RequestParam(defaultValue = "asc") String sortDir,
                               Pageable page, Model model) {
        if (page.getPageSize()>10 || page.getPageNumber()<0) {
            page = PageRequest.of(0,10);
        }
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc")? Sort.Direction.ASC:Sort.Direction.DESC,"name");
        page = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);

        Page<SubtaskDTO> dtos = subtaskService.findAll(page);
        model.addAttribute("subtasks", dtos);
        model.addAttribute("pageSize", page.getPageSize());
        model.addAttribute("pageNumber", page.getPageNumber());
        model.addAttribute("sortDir", sortDir);
        return "subtasks";
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        try {
            subtaskService.deleteSubtask(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest()
                    .body("Nu se poate șterge subtask-ul, există deja înregistrări legate.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body("Eroare neașteptată la ștergere.");
        }
    }
}