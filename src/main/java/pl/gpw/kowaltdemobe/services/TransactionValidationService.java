package pl.gpw.kowaltdemobe.services;

import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;
import pl.gpw.kowaltdemobe.entity.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionValidationService {

    public List<String> validate(List<Transaction> transactions) {
        List<String> statusList = new ArrayList<>();

        for(int i = 0; i < transactions.size(); i++) {
            String sender = transactions.get(i).getSender();
            String receiver = transactions.get(i).getReceiver();
            StringBuilder errorStatusBuilder = new StringBuilder();

            if (sender.equals(receiver)) {
                errorStatusBuilder.append("sender cannot be the same as the receiver; ");
            }

            validateSubject(sender, "sender", errorStatusBuilder);
            validateSubject(receiver, "receiver", errorStatusBuilder);

            String rawDate = transactions.get(i).getDate();
            if (!GenericValidator.isDate(rawDate, "yyyy-mm-dd", true)) {
                errorStatusBuilder.append("date is not in yyyy-mm-dd format; ");
            }
            else {
                Date current = new Date();
                try {
                    if (current.compareTo(new SimpleDateFormat("yyyy-mm-dd").parse(rawDate)) < 0) {
                        errorStatusBuilder.append("transaction date occurs in the future; ");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String rawValue = transactions.get(i).getValue();
            if (!rawValue.matches("[0-9]*\\.?[0-9]{1,4}")) {
                errorStatusBuilder.append("Incorrect transaction value pattern; ");
            }

            if (errorStatusBuilder.length() != 0) {
                errorStatusBuilder.insert(0,"Error(s) in line " + (i+1) + ": ");
                statusList.add(errorStatusBuilder.toString().trim());
            }
        }

        return statusList;
    }

    private StringBuilder validateSubject(String subject, String subjectType, StringBuilder errorStatusBuilder) {
        if (subject.length() != 5) {
            errorStatusBuilder.append(subjectType + " has to have exactly 5 signs; ");
        }

        if(!subject.matches("[A-Z0-9]*")) {
            errorStatusBuilder.append(subjectType + " can only contain capital letters or integers; ");
        }

        return errorStatusBuilder;
    }
}
