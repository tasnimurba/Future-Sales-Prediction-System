package bd.edu.seu.futuresalespredictionjava.controller;
import bd.edu.seu.futuresalespredictionjava.dto.LoginDto;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/"; // Redirect to `/` if user is already logged in
        } else {
            model.addAttribute("login", new LoginDto());
            return "login";
        }
    }


}

