package Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvException;

//import controller.Book;
import controller.InventoryController;
import dao.Finance;

import repository.FinanceOpRepository;

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
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FinanceReportService {

	private static final Logger logger = Logger.getLogger(FinanceReportService.class.getName());

	// private final BookRepository bookRepository;

	private final FinanceOpRepository financeOpRepository;

	@Autowired
	public FinanceReportService(FinanceOpRepository financeOpRepository) {
		this.financeOpRepository = financeOpRepository;
	}

	public List<Finance> saveAll(List<Finance> books) {
		// Save all valid records to the database
		return financeOpRepository.saveAll(books);
	}

	public CsvUploadResult processCsv(MultipartFile file) throws CsvException, IOException {

		List<Finance> validBooks = new ArrayList<>();
		List<String> invalidRecords = new ArrayList<>();

		 try (BufferedReader br = new BufferedReader(
	                new InputStreamReader(file.getInputStream(), StandardCharsets.US_ASCII))) {

			// ... (same code as before)

			br.readLine();

			String line;

			while ((line = br.readLine()) != null) {
				String[] attributes = line.split(",");

				// Validate each attribute and collect validation messages
				List<String> validationMessages = validateRecord(attributes);

				// Check if the record has the expected number of attributes
				if (!validationMessages.isEmpty()) {
					// Add invalid records to the list
					logger.log(Level.WARNING, "Invalid record: {0}", Arrays.toString(attributes));
					invalidRecords.add("Invalid record: " + Arrays.toString(attributes) + ". Validation errors: "
							+ String.join("; ", validationMessages));
					continue;
				}

				Finance book = createBook(attributes);

				if (book != null) {
					validBooks.add(book);
				}

				// line = br.readLine();
			}

			if (!validBooks.isEmpty()) {
				logger.log(Level.INFO, "persiting records in db....");
				// bookService.saveAll(books);
				financeOpRepository.saveAll(validBooks);
			}
			// Build the response message
			String responseMessage = "File uploaded successfully.";
			if (!invalidRecords.isEmpty()) {
				responseMessage += " Some records were skipped due to validation errors.";
			}
			logger.log(Level.INFO, "File uploaded successfully. Total Successful Records: {0}, Invalid Records: {1}",
					new Object[] { validBooks.size(),
							(invalidRecords.size() > 0 ? String.join("; ", invalidRecords) : 0) });

			// Build the response entity
			//return ResponseEntity.status(HttpStatus.OK)
			//		.body(responseMessage + " Total Successful Records: " + validBooks.size() + ". Invalid Records: "
			//				+ (invalidRecords.size() > 0 ? String.join("; ", invalidRecords) : 0));
			
			//return new CsvUploadResponse(HttpStatus.OK, responseMessage + " Total Successful Records: " + validBooks.size() +
                  //  ". Invalid Records: " + (invalidRecords.size() > 0 ? String.join("; ", invalidRecords) : 0));
			return new CsvUploadResult(HttpStatus.OK, responseMessage + " Total Successful Records: " + validBooks.size() +
                    ". Invalid Records: " + (invalidRecords.size() > 0 ? String.join("; ", invalidRecords) : 0));



		} catch (IOException e) {
			e.printStackTrace();
			return new CsvUploadResult(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");

		}

		/*
		 * if (!validBooks.isEmpty()) { financeOpRepository.saveAll(validBooks); }
		 * 
		 * // Build the response message String responseMessage =
		 * "File uploaded successfully."; if (!invalidRecords.isEmpty()) {
		 * responseMessage += " Some records were skipped due to validation errors."; }
		 * 
		 * logger.log(Level.INFO,
		 * "File uploaded successfully. Total Successful Records: {0}, Invalid Records: {1}"
		 * , new Object[] { validBooks.size(), (invalidRecords.size() > 0 ?
		 * String.join("; ", invalidRecords) : 0) });
		 * 
		 * // Build the response entity //return new CsvUploadResult(HttpStatus.OK,
		 * responseMessage + " Total Successful Records: " + validBooks.size() // +
		 * ". Invalid Records: " + (invalidRecords.size() > 0 ? String.join("; ",
		 * invalidRecords) : 0));
		 * 
		 * return new CsvUploadResult(HttpStatus.OK, responseMessage +
		 * " Total Successful Records: " + validBooks.size() + ". Invalid Records: " +
		 * (invalidRecords.size() > 0 ? String.join("; ", invalidRecords) : 0));
		 */
	}

	private static Finance createBook(String[] attributes) {

		try {
			// Assuming you have a Book class with appropriate constructor
			// Modify this method based on your Book class structure
			Finance book = new Finance();

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

	private List<String> validateRecord(String[] attributes) {
		List<String> validationMessages = new ArrayList<>();

		// Validate each attribute and add messages to the list
		if (attributes.length > 0) {
			if (!validateName(attributes[0])) {
				validationMessages.add("Name is required and cannot be empty.");
			}
		}
		if (attributes.length > 1) {
			if (!validateCost(attributes[1])) {
				if (!attributes[1].trim().isEmpty()) {
					validationMessages.add("Cost value is Absent.");
				} else {
					validationMessages.add("Cost must be a positive number.");
				}

			}
		}
		if (attributes.length > 2) {
			if (!validatePurchaseTimePlanned(attributes[2].trim().replaceAll("\"", ""))) {
				validationMessages.add("Purchase time planned must be in the format yyyy-MM-dd'T'HH:mm:ss.");
			}
		}

		return validationMessages;
	}

	private boolean validateName(String name) {
		// Add your name validation logic here
		// Example: Check if the name is not empty
		return !name.trim().isEmpty();
	}

	private boolean validateCost(String cost) {
		// Add your cost validation logic here
		// Example: Check if the cost is a positive number
		try {

			double costValue = Double.parseDouble(cost);
			return costValue > 0.00;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean validatePurchaseTimePlanned(String purchaseTimePlanned) {
		// Validate against the format "yyyy-MM-dd'T'HH:mm:ss"
		try {
			LocalDateTime.parse(purchaseTimePlanned, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}
