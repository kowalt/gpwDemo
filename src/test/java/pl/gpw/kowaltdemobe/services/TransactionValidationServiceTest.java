package pl.gpw.kowaltdemobe.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.gpw.kowaltdemobe.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class TransactionValidationServiceTest {

    private List<Transaction> transactionList;

    @Autowired
    private TransactionValidationService transactionValidationService;

    @BeforeEach
    void init() {
        transactionList = new ArrayList<>();
    }

    @Test
    public void allAttributesOK() {
        transactionList.add(new Transaction("RAFAL", "TOMEK", "1992-07-07", "900.004"));
        List<String> statusList = transactionValidationService.validate(transactionList);
        assertThrows(IndexOutOfBoundsException.class, () -> statusList.get(0));
    }

    @Test
    public void senderEqualsReceiver() {
        transactionList.add(new Transaction("KRZYS", "ZDZIS", "1992-07-07", "900.004"));
        transactionList.add(new Transaction("KRZYS", "KRZYS", "1992-07-07", "900.004"));
        List<String> statusList = transactionValidationService.validate(transactionList);

        assertEquals("Error(s) in line 2: sender cannot be the same as the receiver;", statusList.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> statusList.get(1));
    }

    @Test
    public void subjectsCorrectLength() {
        transactionList.add(new Transaction("KRZYSIEK", "ZDZIS", "1992-07-07", "900.004"));
        transactionList.add(new Transaction("KRZYS", "ZDZISIEK", "1992-07-07", "900.004"));

        List<String> statusList = transactionValidationService.validate(transactionList);

        assertEquals("Error(s) in line 1: sender has to have exactly 5 signs;", statusList.get(0));
        assertEquals("Error(s) in line 2: receiver has to have exactly 5 signs;", statusList.get(1));
    }

    @Test
    public void subjectOnlyCapitalLettersOrNumbers() {
        transactionList.add(new Transaction("Krzys", "Zdzis", "1992-07-07", "900.004"));
        transactionList.add(new Transaction("KRZY5", "ZDZ15", "1992-07-07", "900.004"));
        List<String> statusList = transactionValidationService.validate(transactionList);

        assertEquals("Error(s) in line 1: sender can only contain capital letters or integers; receiver can only contain capital letters or integers;", statusList.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> statusList.get(1));
    }

    @Test
    public void dateNotFuture() {
        transactionList.add(new Transaction("KRZYS", "ZDZIS", "2137-07-07", "900.004"));
        List<String> statusList = transactionValidationService.validate(transactionList);

        assertEquals("Error(s) in line 1: transaction date occurs in the future;", statusList.get(0));
    }

    @Test
    public void dateCorrectFormat() {
        transactionList.add(new Transaction("KRZYS", "ROKOK", "7/7/1992", "900.004"));
        List<String> statusList = transactionValidationService.validate(transactionList);
        assertEquals("Error(s) in line 1: date is not in yyyy-mm-dd format;", statusList.get(0));
    }

    @Test
    public void amountCorrectFormat() {
        transactionList.add(new Transaction("RAFAL", "TOMEK", "1992-07-07", "900"));
        transactionList.add(new Transaction("RAFAL", "TOMEK", "1992-07-07", "900.f2"));
        transactionList.add(new Transaction("RAFAL", "TOMEK", "1992-07-07", "900."));
        transactionList.add(new Transaction("RAFAL", "TOMEK", "1992-07-07", "900.51234"));

        List<String> statusList = transactionValidationService.validate(transactionList);

        assertEquals("Error(s) in line 2: Incorrect transaction value pattern;", statusList.get(0));
        assertEquals("Error(s) in line 3: Incorrect transaction value pattern;", statusList.get(1));
        assertEquals("Error(s) in line 4: Incorrect transaction value pattern;", statusList.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> statusList.get(3));
    }

    @Test
    public void amountNegative() {
        transactionList.add(new Transaction("RAFAL", "TOMEK", "1992-07-07", "-900.004"));
        List<String> statusList = transactionValidationService.validate(transactionList);
        assertEquals("Error(s) in line 1: Incorrect transaction value pattern;", statusList.get(0));
    }
}
