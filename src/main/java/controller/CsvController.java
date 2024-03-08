package controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import Service.BookService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/csv")
public class CsvController {

	private final BookService bookService;

	@Autowired
	public CsvController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) throws CsvException {

		List<Book> books = new ArrayList<>();
		List<String> invalidRecords = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.US_ASCII))) {

			br.readLine();

			String line;

			while ((line = br.readLine()) != null) {
				String[] attributes = line.split(",");

				Book book = createBook(attributes);

				if (book == null) {
					// Handle the case where the record length is invalid
					invalidRecords.add(Arrays.toString(attributes));
				} else {
					books.add(book);
				}

				// line = br.readLine();
			}
			if (!invalidRecords.isEmpty()) {
				// Return error response with details of invalid records
				String errorMessage = "Invalid records: " + String.join("; ", invalidRecords);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			for (Book b : books) {
				System.out.println(b);
			}

			// Save valid records to the database
			bookService.saveAll(books);

			return ResponseEntity.status(HttpStatus.OK).body(books);

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	private static Book createBook(String[] attributes) {

		try {
			// Assuming you have a Book class with appropriate constructor
			// Modify this method based on your Book class structure
			Book book = new Book();

			book.setName(attributes[0].trim().replaceAll("\"", ""));
			book.setCost(attributes[1].trim().replaceAll("\"", ""));
			book.setPurchaseTimePlanned(attributes[2].trim().replaceAll("\"", ""));

			return book;
		} catch (Exception e) {
			// Handle any parsing errors
			e.printStackTrace();
			return null;
		}
	}

}
