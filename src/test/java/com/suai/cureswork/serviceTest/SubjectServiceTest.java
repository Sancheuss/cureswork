package com.suai.cureswork.serviceTest;

import com.suai.cureswork.crud.entity.*;
import com.suai.cureswork.crud.repo.*;
import com.suai.cureswork.service.CellsService;
import com.suai.cureswork.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class SubjectServiceTest {
    @Mock
    private CellsRepo cellsRepo;
    @Mock
    private SubjectsRepo subjectsRepo;
    @Mock
    private GroupsRepo groupsRepo;
    @Mock
    private CellsService cellsService;
    @InjectMocks
    private SubjectService subjectService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSheet() {
        // Mock data
        Map<String, String> requestData = new HashMap<>();
        requestData.put("numRows", "2");
        requestData.put("numColumns", "3");
        requestData.put("group", "Group1");
        requestData.put("subject", "Subject1");

        Groups existingGroup = null;
        when(groupsRepo.findById(anyString())).thenReturn(Optional.ofNullable(existingGroup));

        when(subjectsRepo.findByGroupAndSubject(anyString(), anyString())).thenReturn(null);

        ResponseEntity<String> response = subjectService.createSheet(requestData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ведомость успешно создана", response.getBody());
    }

    @Test
    void testEditSheet() {
        // Mock data
        Map<String, String> requestData = new HashMap<>();
        requestData.put("row", "1");
        requestData.put("column", "2");
        requestData.put("subjectId", "1");
        requestData.put("value", "NewValue");

        Cells existingCell = new Cells();
        when(cellsRepo.findBySubjectIdAndRowAndColumn(anyInt(), anyInt(), anyInt())).thenReturn(Optional.of(existingCell));

        ResponseEntity<String> response = subjectService.editSheet(requestData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }

    @Test
    void testChangeTableSize() {
        // Mock data
        Map<String, String> requestData = new HashMap<>();
        requestData.put("numberRows", "4");
        requestData.put("numberColumns", "5");
        requestData.put("subjectId", "1");

        Subjects existingSubject = new Subjects();
        when(subjectsRepo.findById(anyInt())).thenReturn(Optional.of(existingSubject));

        ResponseEntity<String> response = subjectService.changeTableSize(requestData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Размеры ведомости успешно изменены", response.getBody());
    }

    @Test
    void testDeleteSheet() {
        String group = "Group1";
        Integer subjectId = 1;

        when(subjectsRepo.findByGroup(anyString())).thenReturn(List.of(new Subjects()));
        when(groupsRepo.existsById(anyString())).thenReturn(true);

        ResponseEntity<String> response = subjectService.deleteSheet(group, subjectId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Удаление ведомости прошло успешно!", response.getBody());
    }
}

