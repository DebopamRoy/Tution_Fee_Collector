package com.mapobed.tutionfeecollector.Home.Student.Payment.PaymentPortal;

public class StudentPaymentModelClass {
    String name, username, mode, payment_date;
    int amount, semester;

    public StudentPaymentModelClass() {
    }

    public StudentPaymentModelClass(String name, String username, String mode, int amount, int semester, String payment_date) {
        this.name = name;
        this.username = username;
        this.payment_date = payment_date;
        this.mode = mode;
        this.amount = amount;
        this.semester = semester;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}