package bd.edu.seu.futuresalespredictionjava.dto;
//graph part prediction er jonno
public class PredictionTrendDto {
    private String date;
    private Integer actual;
    private Integer predicted;

    public PredictionTrendDto(String date, Integer actual, Integer predicted) {
        this.date = date;
        this.actual = actual;
        this.predicted = predicted;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Integer getActual() { return actual; }
    public void setActual(Integer actual) { this.actual = actual; }

    public Integer getPredicted() { return predicted; }
    public void setPredicted(Integer predicted) { this.predicted = predicted; }
}

