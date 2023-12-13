package com.suai.cureswork.service;

import com.suai.cureswork.crud.entity.Cells;
import com.suai.cureswork.crud.entity.Subjects;
import com.suai.cureswork.crud.repo.CellsRepo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final CellsRepo cellsRepo;

    public byte[] createWorkbook(Subjects subject, String subjectId, String saveFileName) throws IOException {
        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            int numRows = subject.getNumberRows();
            int numColumns = subject.getNumberColumns();

            for (int i = 0; i < numRows; i++) {
                Row dataRow = sheet.createRow(i);

                for (int j = 0; j < numColumns; j++) {
                    Cells cell = cellsRepo.findBySubjectIdAndRowAndColumn(Integer.valueOf(subjectId), i, j).orElse(null);
                    String cellValue = (cell != null) ? cell.getValue() : "";
                    dataRow.createCell(j).setCellValue(cellValue);
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(saveFileName)) {
                workbook.write(outputStream);
            }
        }

        return new FileInputStream(saveFileName).readAllBytes();
    }

    public boolean readExcelData(Subjects subject, MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int rowIndex = row.getRowNum();
                int numColumns = subject.getNumberColumns();

                for (int j = 0; j < numColumns; j++) {
                    Cells cell = cellsRepo.findBySubjectIdAndRowAndColumn(subject.getId(), rowIndex, j).orElse(null);

                    if (cell == null) {
                        cell = new Cells();
                        cell.setSubjectId(subject.getId());
                        cell.setRow(rowIndex);
                        cell.setColumn(j);
                    }

                    Cell excelCell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setValue(getCellValue(excelCell));
                    cellsRepo.save(cell);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
