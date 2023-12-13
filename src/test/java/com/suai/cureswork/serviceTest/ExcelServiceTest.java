package com.suai.cureswork.serviceTest;
import com.suai.cureswork.crud.entity.Cells;
import com.suai.cureswork.crud.entity.Subjects;
import com.suai.cureswork.crud.repo.CellsRepo;
import com.suai.cureswork.service.ExcelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcelServiceTest {
    @Mock
    private CellsRepo cellsRepo;
    @InjectMocks
    private ExcelService excelService;

    @Test
    void testCreateWorkbook() throws IOException {
        Subjects subject = new Subjects();
        subject.setNumberRows(2);
        subject.setNumberColumns(3);
        subject.setId(1);
        String subjectId = "1";
        String saveFileName = "testWorkbook.xls";
        when(cellsRepo.findBySubjectIdAndRowAndColumn(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Optional.empty());
        byte[] result = excelService.createWorkbook(subject, subjectId, saveFileName);
        assertNotNull(result);
        Workbook workbook = WorkbookFactory.create(new FileInputStream(saveFileName));
        assertNotNull(workbook);
        assertEquals(subject.getNumberRows(), workbook.getSheetAt(0).getPhysicalNumberOfRows());
        assertEquals(subject.getNumberColumns(), workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells());
    }

    @Test
    void testReadExcelData() throws IOException {
        Subjects subject = new Subjects();
        subject.setId(1);
        subject.setNumberColumns(3);
        subject.setNumberRows(3);
        subject.setGroup("1111");
        subject.setSubject("Sub");

        File tempFile = File.createTempFile("temp", ".xls");
        String filePath = tempFile.getAbsolutePath();

        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("TestValue");

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
        MultipartFile file = new MockMultipartFile("testFile", new FileInputStream(filePath));

        boolean result = excelService.readExcelData(subject, file);
        assertTrue(result);
    }


    @Test
    void testGetCellValue() {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("TestValue");
        String result = excelService.getCellValue(cell);
        assertEquals("TestValue", result);
    }
}


