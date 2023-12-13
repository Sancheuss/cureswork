package com.suai.cureswork.controller;
import com.suai.cureswork.crud.entity.Cells;
import com.suai.cureswork.crud.entity.Subjects;
import com.suai.cureswork.crud.repo.CellsRepo;
import com.suai.cureswork.crud.repo.SubjectsRepo;
import com.suai.cureswork.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SubjectController {
    CellsRepo cellsRepo;
    SubjectsRepo subjectsRepo;
    ExcelService excelService;

    @GetMapping("/subject/{subjectId}")
    @ResponseBody
    public Map<String, Object> getStudentsBySubject(@PathVariable("subjectId") Integer subjectId) {
        Map<String, Object> result = new HashMap<>();

        List<Cells> cells = cellsRepo.findBySubjectId(subjectId);
        Subjects subject = subjectsRepo.findById(subjectId).orElse(null);

        if (subject != null) {
            result.put("numRows", subject.getNumberRows());
            result.put("numColumns", subject.getNumberColumns());
        }

        result.put("cells", cells);
        return result;
    }

    @PostMapping("/downloadExcel")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody Map<String, String> requestData) throws IOException {
        String saveFileName = requestData.get("saveFileName");
        String subjectId = requestData.get("subjectId");
        Subjects subject = subjectsRepo.findById(Integer.valueOf(subjectId)).orElse(null);
        if (subject == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte[] file = excelService.createWorkbook(subject, subjectId, saveFileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", saveFileName + ".xls");

            return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }


    @PostMapping("/uploadExcel")
    public ResponseEntity<String> uploadExcel(
            @RequestPart("file") MultipartFile file,
            @RequestPart("subjectId") String subjectId
    ) throws IOException {
        Subjects subject = subjectsRepo.findById(Integer.valueOf(subjectId)).orElse(null);
        if (subject == null) {
            return new ResponseEntity<>("Subject not found", HttpStatus.NOT_FOUND);
        }
        boolean success = excelService.readExcelData(subject, file);
        if (success) {
            return new ResponseEntity<>("Data successfully read from Excel", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to read data from Excel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}