package net.ddns.techworm.smart___mechanic;

class Driver_User {
    private String name,phone,datee,picha;

    Driver_User(String name, String phone, String datee,String picha) {
        this.name = name;
        this.phone = phone;
        this.datee = datee;
        this.picha = picha;
    }

    public String getPicha() {
        return picha;
    }

    public void setPicha(String picha) {
        this.picha = picha;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDatee() {
        return datee;
    }
    public void setDatee(String datee) {
        this.datee = datee;
    }
}
