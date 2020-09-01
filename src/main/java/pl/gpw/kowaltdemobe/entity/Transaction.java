package pl.gpw.kowaltdemobe.entity;

import com.opencsv.bean.CsvBindByName;

public class Transaction {
    @CsvBindByName(column = "nadawca")
    private String sender;
    @CsvBindByName(column = "odbiorca")
    private String receiver;
    @CsvBindByName(column = "data")
    private String date;
    @CsvBindByName(column = "kwota")
    private String value;

    public Transaction(String sender, String receiver, String date, String value) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.value = value;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
