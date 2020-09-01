package pl.gpw.kowaltdemobe.controllers;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.gpw.kowaltdemobe.entity.Transaction;
import pl.gpw.kowaltdemobe.services.TransactionValidationService;
import com.opencsv.bean.CsvToBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionValidationService transactionValidationService;

    @PostMapping("/validate")
    public ResponseEntity validate(@RequestParam("file") MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<Transaction> csvToBean =
                    new CsvToBeanBuilder(reader).withType(Transaction.class).withIgnoreLeadingWhiteSpace(true).build();

            List<Transaction> transactions = csvToBean.parse();
            List<String> errorList = transactionValidationService.validate(transactions);

            if (errorList.isEmpty()) {
                return ResponseEntity.ok().build();
            }
            return new ResponseEntity<List<String>>(errorList, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (IOException ioe) {
            return ResponseEntity.badRequest().build();
        }
    }
}
