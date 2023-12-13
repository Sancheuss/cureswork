package com.suai.cureswork.controller;

import com.suai.cureswork.crud.entity.Cells;
import com.suai.cureswork.crud.entity.Groups;
import com.suai.cureswork.crud.entity.Subjects;
import com.suai.cureswork.crud.repo.CellsRepo;
import com.suai.cureswork.crud.repo.GroupsRepo;
import com.suai.cureswork.crud.repo.SubjectsRepo;
import com.suai.cureswork.service.CellsService;
import com.suai.cureswork.service.SubjectService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class TeacherController {
    @Autowired
    CellsRepo cellsRepo;
    @Autowired
    GroupsRepo groupsRepo;
    @Autowired
    SubjectsRepo subjectsRepo;
    @Autowired
    CellsService cellsService;
    @Autowired
    SubjectService subjectService;
    @GetMapping("/teacher")
    public ModelAndView showTeacherPage() {
        List<Groups> groups = groupsRepo.findAll();
        List<Subjects> subjects = subjectsRepo.findAll();
        List<Cells> cells = cellsRepo.findAll();

        ModelAndView model = new ModelAndView("teacher");
        model.addObject("groups", groups);
        model.addObject("subjects", subjects);
        model.addObject("cells", cells);

        return model;
    }
    @PostMapping("/teacher/createSheet")
    public ResponseEntity<String> createSheet(@RequestBody Map<String, String> requestData) {
        return subjectService.createSheet(requestData);
    }

    @PutMapping("/teacher/editSheet")
    public ResponseEntity<String> editSheet(@RequestBody Map<String, String> requestData) {
        return subjectService.editSheet(requestData);
    }

    @PostMapping("/teacher/changeTableSize")
    public ResponseEntity<String> changeTableSize(@RequestBody Map<String, String> requestData) {
        return subjectService.changeTableSize(requestData);
    }

    @PostMapping("/teacher/deleteSheet")
    @Transactional
    public ResponseEntity<String> deleteSheet(@RequestParam String group, @RequestParam Integer subjectId) {
        return subjectService.deleteSheet(group, subjectId);
    }
}
