package com.unibuc.projectmanagementapp.controllers;

import com.unibuc.projectmanagementapp.dtos.SkillDTO;
import com.unibuc.projectmanagementapp.services.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping
    public ResponseEntity<?> addSkill(@Valid @RequestBody SkillDTO dto, BindingResult br) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(br.getAllErrors());
        }
        return ResponseEntity.ok(skillService.saveSkill(dto.toEntity()));
    }
}