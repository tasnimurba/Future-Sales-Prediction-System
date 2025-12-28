package bd.edu.seu.futuresalespredictionjava.service;

import bd.edu.seu.futuresalespredictionjava.dto.PredictionTrendDto;
import bd.edu.seu.futuresalespredictionjava.dto.SalesPredictionDto;
import bd.edu.seu.futuresalespredictionjava.model.Prediction;
import bd.edu.seu.futuresalespredictionjava.model.SalesEntry;
import bd.edu.seu.futuresalespredictionjava.repository.PredictionRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PredictionService {
    private final ChatClient chatClient;

    private final PredictionRepository predictionRepository; //graph er jonno

    public PredictionService(ChatClient.Builder chatClientBuilder, PredictionRepository predictionRepository) {
        this.chatClient = chatClientBuilder.build();
        this.predictionRepository = predictionRepository;
    }

    public String predictSales(SalesPredictionDto dto) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("I have a product named ").append(dto.getProductName()).append(". ");
        prompt.append("Here is its past sales data (date, units sold):\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (SalesEntry entry : dto.getEntries()) {
            prompt.append(entry.getDate().format(formatter))
                    .append(" - ")
                    .append(entry.getUnitsSold())
                    .append(" units\n");
        }

//        prompt.append("Based on this, predict the sales for the next week. ");
//        prompt.append("Also mention how weather might affect sales and suggest how to increase sales.");

//

        prompt.append("Based on this, predict the sales for the next week in an organizing way.<br>");
        prompt.append("Then explain how weather may affect sales in an organizing way.<br>");
        prompt.append("Finally, give suggestions to increase sales in an organizing way.<br>");
        prompt.append("Respond in clean and structured HTML using <h3>, <p>, <ul>, <li> tags. Do not return markdown. Do not use code block formatting.");

        ChatResponse chatResponse = chatClient
                .prompt()
                .user(prompt.toString())
                .call()
                .chatResponse();

        return chatResponse.getResult().getOutput().getText();
    }


//prediction save kortese (graph part) ///////////////////////////////////

    public void savePredictionForUser(String userId, SalesPredictionDto dto) {
        // Convert SalesPredictionDto entries to Maps for actual and predicted sales
        Map<String, Integer> actualSales = new HashMap<>();
        Map<String, Integer> predictedSales = new HashMap<>();

        dto.getEntries().forEach(entry -> {
            if (entry.getDate() != null) {
                String dateStr = entry.getDate().toString();
                actualSales.put(dateStr, entry.getUnitsSold());
                // For now, predicted sales is empty or some logic to predict sales per date
                // Let's just copy actual to predicted for simplicity or implement your prediction here
                //predictedSales.put(dateStr, entry.getUnitsSold()); // Replace with your predicted value
                //predictedSales.put(dateStr, (int)(entry.getUnitsSold() * 1.1)); // 🔁 For now, just simulate +10% as prediction for demo
                int predictedValue = (int) Math.round(entry.getUnitsSold() * 1.1);
                predictedSales.put(dateStr, predictedValue);
            }
        });

        Prediction prediction = new Prediction(
                userId,
                dto.getProductName(),
                LocalDate.now(),
                actualSales,
                predictedSales
        );

        predictionRepository.save(prediction);
    }

    public List<Prediction> getAllPredictionsForUser(String userId) {
        return predictionRepository.findAllByUserId(userId);
    }

//    public List<PredictionTrendDto> getTrendsForUser(String userId) {
//        List<Prediction> predictions = predictionRepository.findAllByUserId(userId);
//
//        List<PredictionTrendDto> trendList = new ArrayList<>();
//
//        for (Prediction prediction : predictions) {
//            for (Map.Entry<String, Integer> entry : prediction.getPredictedSales().entrySet()) {
//                String date = entry.getKey();
//                Integer predicted = entry.getValue();
//                Integer actual = prediction.getActualSales().getOrDefault(date, 0);
//                trendList.add(new PredictionTrendDto(date, actual, predicted));
//            }
//        }
//
//        return trendList;
//    }

    public List<PredictionTrendDto> getTrendsForUser(String userId) {
        List<Prediction> predictions = predictionRepository.findAllByUserId(userId);

        Map<String, PredictionTrendDto> trendMap = new TreeMap<>(); // TreeMap keeps keys sorted

        for (Prediction prediction : predictions) {
            for (Map.Entry<String, Integer> entry : prediction.getPredictedSales().entrySet()) {
                String date = entry.getKey();
                Integer predicted = entry.getValue();
                Integer actual = prediction.getActualSales().getOrDefault(date, 0);

                // If date exists, you may want to aggregate or overwrite, here overwrite
                trendMap.put(date, new PredictionTrendDto(date, actual, predicted));
            }
        }

        return new ArrayList<>(trendMap.values());
    }



    //image prediction
    public String analyzeImage(byte[] imageBytes) {
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        String prompt = "You are analyzing a product image (e.g. a shirt). "
                + "Based on this image (base64-encoded), suggest improvements. "
                + "Mention if the color could be better, if a different size or logo placement would work better, etc. "
                + "Here is the image: data:image/jpeg;base64," + base64Image
                + ". Return suggestions in clean HTML format using <ul> <li> <p>.";

        ChatResponse response = chatClient
                .prompt()
                .user(prompt)
                .call()
                .chatResponse();

        return response.getResult().getOutput().getText();
    }


}

