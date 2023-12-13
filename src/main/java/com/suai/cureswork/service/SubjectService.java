package com.suai.cureswork.service;

import com.suai.cureswork.crud.entity.Cells;
import com.suai.cureswork.crud.entity.Groups;
import com.suai.cureswork.crud.entity.Subjects;
import com.suai.cureswork.crud.repo.CellsRepo;
import com.suai.cureswork.crud.repo.GroupsRepo;
import com.suai.cureswork.crud.repo.SubjectsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class SubjectService {
    @Autowired
    CellsRepo cellsRepo;
    @Autowired
    SubjectsRepo subjectsRepo;
    @Autowired
    GroupsRepo groupsRepo;
    @Autowired
    CellsService cellsService;
    public ResponseEntity<String> createSheet(Map<String, String> requestData) {
        int numberRows = Integer.parseInt(requestData.get("numRows"));
        int numberColumns = Integer.parseInt(requestData.get("numColumns"));
        String group = requestData.get("group");
        String subjectName = requestData.get("subject");

        Groups existingGroup = groupsRepo.findById(group).orElse(null);

        if (existingGroup == null) {
            Groups newGroup = new Groups();
            newGroup.setGroup(group);
            groupsRepo.save(newGroup);
        }

        Subjects existingSubject = subjectsRepo.findByGroupAndSubject(group, subjectName);

        if (existingSubject != null) {
            return ResponseEntity.badRequest().body("Такая ведомость уже существует");
        }

        Subjects newSubject = new Subjects();
        newSubject.setSubject(subjectName);
        newSubject.setGroup(group);
        newSubject.setNumberRows(numberRows);
        newSubject.setNumberColumns(numberColumns);
        subjectsRepo.save(newSubject);

        cellsService.createEmptyCells(newSubject, numberRows, numberColumns);
        return ResponseEntity.ok("Ведомость успешно создана");
    }

    public ResponseEntity<String> editSheet(Map<String, String> requestData) {
        int numberRows = Integer.parseInt(requestData.get("row"));
        int numberColumns = Integer.parseInt(requestData.get("column"));
        int subjectId = Integer.parseInt(requestData.get("subjectId"));
        String value = requestData.get("value");
        try {
            Optional<Cells> optionalCell = cellsRepo.findBySubjectIdAndRowAndColumn(subjectId, numberRows, numberColumns);
            if (optionalCell.isPresent()) {
                Cells cell = optionalCell.get();
                cell.setValue(value);
                cellsRepo.save(cell);
                return ResponseEntity.ok("Success");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cell not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating cell");
        }
    }

    public ResponseEntity<String> changeTableSize(Map<String, String> requestData) {
        String numberRows = requestData.get("numberRows");
        String numberColumns = requestData.get("numberColumns");
        String subjectId = requestData.get("subjectId");
        try {
            Optional<Subjects> subjectOptional = subjectsRepo.findById(Integer.valueOf(subjectId));
            if (subjectOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ведомость не найдена.");
            }
            Subjects subject = subjectOptional.get();
            cellsService.updateCells(subject, Integer.parseInt(numberRows), Integer.parseInt(numberColumns));
            subject.setNumberRows(Integer.parseInt(numberRows));
            subject.setNumberColumns(Integer.parseInt(numberColumns));
            subjectsRepo.save(subject);
            return ResponseEntity.ok("Размеры ведомости успешно изменены");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при изменении размеров ведомости: " + e.getMessage());
        }
    }

    public ResponseEntity<String> deleteSheet(String group, Integer subjectId) {
        try {
            cellsRepo.deleteAllBySubjectId(subjectId);
            subjectsRepo.deleteById(subjectId);

            List<Subjects> subjectsInGroup = subjectsRepo.findByGroup(group);

            if (subjectsInGroup.isEmpty()) {
                groupsRepo.deleteById(group);
            }

            return ResponseEntity.ok("Удаление ведомости прошло успешно!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении ведомости");
        }
    }
}
