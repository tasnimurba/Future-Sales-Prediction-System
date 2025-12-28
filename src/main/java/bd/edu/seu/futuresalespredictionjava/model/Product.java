package bd.edu.seu.futuresalespredictionjava.model;

import org.springframework.data.annotation.Id;

public class Product {
    @Id
    private String id;
    private int pId;
    private String name;
    private String category;
    private double price;
    private String location;

    // New fields
    private Double latitude;
    private Double longitude;






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }




//    public int getpId() {
//        return pId;
//    }
//
//    public void setpId(int pId) {
//        this.pId = pId;
//    }
//public int getPId() {
//    return pId;
//}
//
//    public void setPId(int pId) {
//        this.pId = pId;
//    }


    public int getPId() {
        return pId;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}




