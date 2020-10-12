package com.mapobed.tutionfeecollector.Home.Teacher.Payments.StudentByMonth;

public class PaidStudentModelClass {
    String paid_name, paid_user_name, paid_mode, paid_date;

    public PaidStudentModelClass() {
    }

    public PaidStudentModelClass(String paid_name, String paid_user_name, String paid_mode, String paid_date) {
        this.paid_name = paid_name;
        this.paid_user_name = paid_user_name;
        this.paid_mode = paid_mode;
        this.paid_date = paid_date;
    }

    public String getPaid_name() {
        return paid_name;
    }

    public void setPaid_name(String paid_name) {
        this.paid_name = paid_name;
    }

    public String getPaid_user_name() {
        return paid_user_name;
    }

    public void setPaid_user_name(String paid_user_name) {
        this.paid_user_name = paid_user_name;
    }

    public String getPaid_mode() {
        return paid_mode;
    }

    public void setPaid_mode(String paid_mode) {
        this.paid_mode = paid_mode;
    }

    public String getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(String paid_date) {
        this.paid_date = paid_date;
    }
}
