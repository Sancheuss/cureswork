package com.suai.cureswork.controller;

import com.suai.cureswork.crud.entity.Cells;
import com.suai.cureswork.crud.entity.Groups;
import com.suai.cureswork.crud.entity.Subjects;
import com.suai.cureswork.crud.repo.CellsRepo;
import com.suai.cureswork.crud.repo.GroupsRepo;
import com.suai.cureswork.crud.repo.SubjectsRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class StudentController {
    @Autowired
    CellsRepo cellsRepo;
    @Autowired
    GroupsRepo groupsRepo;
    @Autowired
    SubjectsRepo subjectsRepo;

    @GetMapping("/student")
    public ModelAndView showStudentPage() {
        List<Groups> groups = groupsRepo.findAll();
        List<Subjects> subjects = subjectsRepo.findAll();
        List<Cells> cells = cellsRepo.findAll();

        ModelAndView model = new ModelAndView("student");
        model.addObject("groups", groups);
        model.addObject("subjects", subjects);
        model.addObject("cells", cells);
        return model;
    }
}