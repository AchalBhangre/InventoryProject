package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.exceptions.CsvException;


import Service.CsvUploadResult;
import Service.FinanceReportService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	//private final BookService bookService;
	
	private final FinanceReportService financeReportService;

	@Autowired
	public InventoryController(FinanceReportService financeReportService) {
		this.financeReportService = financeReportService;
	}
	
	private static final Logger logger = Logger.getLogger(InventoryController.class.getName());
	
	@PostMapping("/uploadcsv")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            CsvUploadResult result = financeReportService.processCsv(file);
            return ResponseEntity.status(result.getStatusCode()).body(result.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Internal server error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
        }
    }

	
	
	

}
