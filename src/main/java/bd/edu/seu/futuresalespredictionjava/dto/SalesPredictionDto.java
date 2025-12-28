package bd.edu.seu.futuresalespredictionjava.dto;

import bd.edu.seu.futuresalespredictionjava.model.SalesEntry;

import java.util.ArrayList;
import java.util.List;

public class SalesPredictionDto {
    private String productName;
    private List<SalesEntry> entries = new ArrayList<>();

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<SalesEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<SalesEntry> entries) {
        this.entries = entries;
    }
}
