package bd.edu.seu.futuresalespredictionjava.controller;

import bd.edu.seu.futuresalespredictionjava.dto.LoginDto;
import bd.edu.seu.futuresalespredictionjava.model.User;
import bd.edu.seu.futuresalespredictionjava.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }


    @PostMapping("/register")
    public String objectSubmitForm(@ModelAttribute User u, Model model) {
        System.out.println(u.getUserId() + " " + u.getName());
        u.setRoles(new ArrayList<>());
        u.getRoles().add("ROLE_USER");
        u.getRoles().add("ROLE_ADMIN");

        userService.save(u);

        model.addAttribute("login", new LoginDto()); // Add this line
        return "login";
    }


}
