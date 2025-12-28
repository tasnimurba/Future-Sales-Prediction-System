package bd.edu.seu.futuresalespredictionjava.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class SalesEntry {
    @Id
    private String id;
    private LocalDate date;
    private  int unitsSold;


    public SalesEntry() {
    }

    public SalesEntry(LocalDate date, int unitsSold) {
        this.date = date;
        this.unitsSold = unitsSold;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(int unitsSold) {
        this.unitsSold = unitsSold;
    }
}
