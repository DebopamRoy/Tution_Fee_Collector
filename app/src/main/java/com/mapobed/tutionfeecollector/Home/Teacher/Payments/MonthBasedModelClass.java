package com.mapobed.tutionfeecollector.Home.Teacher.Payments;

public class MonthBasedModelClass {
     String name;
     int pic;

    public MonthBasedModelClass(String name, int pic) {
        this.name = name;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
