package com.suai.cureswork.serviceTest;

import com.suai.cureswork.crud.entity.Cells;
import com.suai.cureswork.crud.entity.Subjects;
import com.suai.cureswork.crud.repo.CellsRepo;
import com.suai.cureswork.service.CellsService;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class CellsServiceTest {
    @Mock
    private CellsRepo cellsRepo;

    @InjectMocks
    private CellsService cellsService;

    @Test
    void testCreateEmptyCells() {
        Subjects subject = new Subjects();
        subject.setId(1);
        int numRows = 2;
        int numColumns = 3;
        cellsService.createEmptyCells(subject, numRows, numColumns);
        Mockito.verify(cellsRepo, Mockito.times(numRows * numColumns)).save(any(Cells.class));
    }

    @Test
    void testUpdateCells() {
        Subjects subject = new Subjects();
        subject.setId(1);
        int numRows = 6;
        int numColumns = 9;

        List<Cells> existingCells = new ArrayList<>();
        Cells existingCell = new Cells();
        existingCell.setSubjectId(subject.getId());
        existingCell.setRow(0);
        existingCell.setColumn(0);
        existingCell.setValue("existingValue");
        existingCells.add(existingCell);

        Mockito.when(cellsRepo.findBySubjectId(subject.getId())).thenReturn(existingCells);

        cellsService.updateCells(subject, numRows, numColumns);

        // Ожидаемое количество вызовов метода save
        int expectedSaveCalls = numRows * numColumns;
        expectedSaveCalls -= Math.min(numRows, existingCells.get(0).getRow() + 1) * Math.min(numColumns, existingCells.get(0).getColumn() + 1);

        // Проверяем, что метод save вызывается нужное количество раз
        Mockito.verify(cellsRepo, Mockito.times(expectedSaveCalls)).save(Mockito.any(Cells.class));
    }


}

