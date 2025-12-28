package bd.edu.seu.futuresalespredictionjava.controller;

import bd.edu.seu.futuresalespredictionjava.service.PredictionService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
public class ImagePredictionController {

    private final PredictionService predictionService;

    public ImagePredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }


    @GetMapping("/image-upload")
    public String imageUploadPage() {
        return "image-upload";
    }

    @PostMapping("/analyze-image")
    public String analyzeImage(@RequestParam("imageFile") MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("prediction", "Please upload an image.");
            return "image-upload";
        }

        String prediction = predictionService.analyzeImage(file.getBytes());
        model.addAttribute("prediction", prediction);
        return "image-upload";
    }

}
