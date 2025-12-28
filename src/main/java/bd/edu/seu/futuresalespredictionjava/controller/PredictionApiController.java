package bd.edu.seu.futuresalespredictionjava.controller;

import bd.edu.seu.futuresalespredictionjava.dto.PredictionTrendDto;
import bd.edu.seu.futuresalespredictionjava.model.Prediction;
import bd.edu.seu.futuresalespredictionjava.model.User;
import bd.edu.seu.futuresalespredictionjava.repository.UserRepository;
import bd.edu.seu.futuresalespredictionjava.service.PredictionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//graph part er jonno
@RestController
@RequestMapping("/api")
public class PredictionApiController {

    private final PredictionService predictionService;
    private final UserRepository userRepository;

    public PredictionApiController(PredictionService predictionService, UserRepository userRepository) {
        this.predictionService = predictionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/prediction-trends")
    public List<PredictionTrendDto> getPredictionTrends(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findFirstByEmail(email).orElseThrow();
        return predictionService.getTrendsForUser(user.getUserId());
    }
}

