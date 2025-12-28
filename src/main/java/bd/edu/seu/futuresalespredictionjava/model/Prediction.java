package bd.edu.seu.futuresalespredictionjava.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;
@Document(collection = "predictions")

public class Prediction {

    @Id
    private String id;

    private String userId;          // link to User by userId
    private String productName;
    private LocalDate predictionDate;

    private Map<String, Integer> actualSales;     // store dates as String for simplicity
    private Map<String, Integer> predictedSales;

    // Constructors, getters and setters

    public Prediction() {}

    public Prediction(String userId, String productName, LocalDate predictionDate,
                      Map<String, Integer> actualSales, Map<String, Integer> predictedSales) {
        this.userId = userId;
        this.productName = productName;
        this.predictionDate = predictionDate;
        this.actualSales = actualSales;
        this.predictedSales = predictedSales;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDate getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(LocalDate predictionDate) {
        this.predictionDate = predictionDate;
    }

    public Map<String, Integer> getActualSales() {
        return actualSales;
    }

    public void setActualSales(Map<String, Integer> actualSales) {
        this.actualSales = actualSales;
    }

    public Map<String, Integer> getPredictedSales() {
        return predictedSales;
    }

    public void setPredictedSales(Map<String, Integer> predictedSales) {
        this.predictedSales = predictedSales;
    }
}
