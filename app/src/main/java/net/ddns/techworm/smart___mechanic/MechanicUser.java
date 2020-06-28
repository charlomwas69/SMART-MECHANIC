package net.ddns.techworm.smart___mechanic;

class MechanicUser {
    private String phoneNumber, location, name , image;


    public MechanicUser(String phoneNumber, String location, String name , String image) {
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.name = name;
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }
}
