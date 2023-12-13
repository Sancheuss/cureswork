package com.suai.cureswork.service;

import com.suai.cureswork.crud.entity.Cells;
import com.suai.cureswork.crud.entity.Subjects;
import com.suai.cureswork.crud.repo.CellsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CellsService {
    private final CellsRepo cellsRepo;
    public void createEmptyCells(Subjects subject, int numRows, int numColumns) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                Cells emptyCell = new Cells();
                emptyCell.setSubjectId(subject.getId());
                emptyCell.setRow(i);
                emptyCell.setColumn(j);
                emptyCell.setValue("");
                cellsRepo.save(emptyCell);
            }
        }
    }

    public void updateCells(Subjects subject, int numRows, int numColumns) {
        List<Cells> cells = cellsRepo.findBySubjectId(subject.getId());
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {

                int finalI = i;
                int finalJ = j;
                Cells existingCell = cells.stream()
                        .filter(cell -> cell.getRow() == finalI && cell.getColumn() == finalJ)
                        .findFirst()
                        .orElse(null);

                if (existingCell == null) {

                    Cells newCell = new Cells();
                    newCell.setSubjectId(subject.getId());
                    newCell.setRow(i);
                    newCell.setColumn(j);
                    newCell.setValue("");
                    cells.add(newCell);
                    cellsRepo.save(newCell);
                }

            }
        }
        cellsRepo.deleteAll(cells.stream().filter(cell -> cell.getRow() >= numRows || cell.getColumn() >= numColumns).collect(Collectors.toList()));
    }

}
