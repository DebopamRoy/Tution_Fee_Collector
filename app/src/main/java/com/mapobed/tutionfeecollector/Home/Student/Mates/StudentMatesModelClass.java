package com.mapobed.tutionfeecollector.Home.Student.Mates;

public class StudentMatesModelClass {
    String name,email,phone,college;

    public StudentMatesModelClass() {
    }

    public StudentMatesModelClass(String name, String email, String phone, String college) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.college = college;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }
}
