package bd.edu.seu.futuresalespredictionjava.controller;


import bd.edu.seu.futuresalespredictionjava.dto.SalesPredictionDto;
import bd.edu.seu.futuresalespredictionjava.model.Prediction;
import bd.edu.seu.futuresalespredictionjava.model.Product;
import bd.edu.seu.futuresalespredictionjava.model.SalesEntry;
import bd.edu.seu.futuresalespredictionjava.model.User;
import bd.edu.seu.futuresalespredictionjava.repository.ProductRepository;
import bd.edu.seu.futuresalespredictionjava.repository.UserRepository;
import bd.edu.seu.futuresalespredictionjava.service.PredictionService;
import bd.edu.seu.futuresalespredictionjava.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final PredictionService predictionService;
    private final ProductService productService;
    private final ProductRepository productRepository;

    public DashboardController(UserRepository userRepository, PredictionService predictionService, ProductService productService, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.predictionService = predictionService;
        this.productService = productService;
        this.productRepository = productRepository;
    }

//    @GetMapping("/dashboard")
//    public String logoutPage() {
//        // Logout er por ei page e redirect korbe
//        return "dashboard";
//    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal)  //Principal diye currently logged-in user ke identify korechi
    {
        String email = principal.getName();       //Logged in user-er email
        User user = userRepository.findFirstByEmail(email).orElseThrow();
        model.addAttribute("user", user);       //User data
        model.addAttribute("productList", user.getProducts());  // <=== Add this line
        model.addAttribute("product", new Product());   //Add new product form-er jonno



        //Eta thakte hobe
        SalesPredictionDto dto = new SalesPredictionDto();
        dto.setEntries(Arrays.asList(new SalesEntry(), new SalesEntry(), new SalesEntry())); // minimum 3 blank
        model.addAttribute("predictionDto", dto);


        return "dashboard";
    }

    @PostMapping("/dashboard/add-product")
    public String addProduct(@ModelAttribute Product product, Principal principal) {


        String email = principal.getName();
        User user = userRepository.findFirstByEmail(email).orElseThrow();


        productService.autoFillCoordinates(product); //map er jonno jeno location er shthe lattitute, logetute auto ashe


        user.getProducts().add(product);

        userRepository.save(user);

        productService.save(product); // 🔁 Save in ProductRepository too

        return "redirect:/dashboard";
    }





//    @PostMapping("/predict-sales")
//    public String predictSales(@ModelAttribute SalesPredictionDto dto, Model model, Principal principal) {
//        long validEntries = dto.getEntries().stream()
//                .filter(e -> e.getDate() != null && e.getUnitsSold() > 0)
//                .count();
//
//        if (validEntries < 3) {
//            model.addAttribute("error", "Please provide at least 3 valid entries.");
//        } else {
//            String predictionResult = predictionService.predictSales(dto);
//            model.addAttribute("prediction", predictionResult);
//        }
//
//        // 🔁 Rebind user, product and predictionDto — required by dashboard.html
//        String email = principal.getName();
//        User user = userRepository.findFirstByEmail(email).orElseThrow();
//
//
//
//        model.addAttribute("user", user);
//        model.addAttribute("product", new Product());
//        model.addAttribute("predictionDto", dto); // keep user's previous entries
//
//        return "dashboard";
//    }
@PostMapping("/predict-sales")
public String predictSales(@ModelAttribute SalesPredictionDto dto, Model model, Principal principal) {
    long validEntries = dto.getEntries().stream()
            .filter(e -> e.getDate() != null && e.getUnitsSold() > 0)
            .count();

    if (validEntries < 3) {
        model.addAttribute("error", "Please provide at least 3 valid entries.");
    } else {
        String predictionResult = predictionService.predictSales(dto);
        model.addAttribute("prediction", predictionResult);

        String email = principal.getName();
        User user = userRepository.findFirstByEmail(email).orElseThrow();

        // Save the prediction in MongoDB
        predictionService.savePredictionForUser(user.getUserId(), dto);
    }

    String email = principal.getName();
    User user = userRepository.findFirstByEmail(email).orElseThrow();
    model.addAttribute("user", user);
   // model.addAttribute("productList", user.getProducts());  // <=== Add this line
    model.addAttribute("productList", productService.getAll());  // ✅

    model.addAttribute("product", new Product());
    model.addAttribute("predictionDto", dto);

    return "dashboard";
}




    // Post to get product for editing (via the form)
    @PostMapping("/dashboard/get-product")
    public String getProductForEdit(@RequestParam("productId") int productId, Model model, Principal principal) {
        System.out.println("Received productId: " + productId); // 👈 Check if it comes null
        Product editingProduct = productService.getById(productId);  // You must implement this in ProductService
        if (editingProduct == null) {
            return "redirect:/dashboard"; // Or show an error message
        }
        String email = principal.getName();
        User user = userRepository.findFirstByEmail(email).orElseThrow();

        model.addAttribute("user", user);
       // model.addAttribute("productList", user.getProducts());  // <=== Add this line
        model.addAttribute("productList", productService.getAll());  // ✅ Correct

        model.addAttribute("product", new Product());
        model.addAttribute("editingProduct", editingProduct);

        SalesPredictionDto dto = new SalesPredictionDto();
        dto.setEntries(Arrays.asList(new SalesEntry(), new SalesEntry(), new SalesEntry()));
        model.addAttribute("predictionDto", dto);

        return "dashboard";  // forward directly to view, not redirect
    }


    // Update product POST
//    @PostMapping("/dashboard/update-product")
//    public String updateProduct(@ModelAttribute Product product) {
//        productService.update(product); // implement update method in service
//        return "redirect:/dashboard";
//    }

    /////////
    @PostMapping("/dashboard/update-product")
    public String updateProduct(@ModelAttribute Product product, Principal principal, Model model) {


        productService.autoFillCoordinates(product);  //map purpose

        productService.update(product); // ✅ Saves product to DB

        // ✅ Now refresh the user object from DB so that updated product appears in dashboard
        String email = principal.getName();
        User user = userRepository.findFirstByEmail(email).orElseThrow();

        // ✅ Get all products from DB
        List<Product> allProducts = productRepository.findAll();

        model.addAttribute("user", user);
        //model.addAttribute("productList", user.getProducts());  // <=== Add this line
        model.addAttribute("productList", productService.getAll());  // ✅ Correct - freshly fetched from DB

        model.addAttribute("product", new Product()); // blank product form
        model.addAttribute("predictionDto", new SalesPredictionDto()); // reset prediction form

        model.asMap().remove("editingProduct"); // ✅ Cleanly removes it from the model

        return "dashboard"; // ❗ Return view instead of redirect
    }

    /////////

    // Delete product POST
//    @PostMapping("/dashboard/delete-product")
//    public String deleteProduct(@RequestParam("productId") int productId) {
//        productService.delete(productId); // implement delete method in service
//        return "redirect:/dashboard";
//    }

    @PostMapping("/dashboard/delete-product")
    public String deleteProduct(@RequestParam("productId") int productId, Principal principal) {
        // ✅ First, delete from ProductRepository
        productService.delete(productId);

        // ✅ Then, remove it from the user's product list
        String email = principal.getName();
        User user = userRepository.findFirstByEmail(email).orElseThrow();

        user.getProducts().removeIf(p -> p.getPId() == productId);
        userRepository.save(user); // ✅ Save updated user

        return "redirect:/dashboard";
    }


    //graph part
    @GetMapping("/dashboard/trends")
    public String predictionTrends(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findFirstByEmail(email).orElseThrow();

        List<Prediction> predictions = predictionService.getAllPredictionsForUser(user.getUserId());

        model.addAttribute("user", user);

        model.addAttribute("predictions", predictions);

        return "trends";  // New Thymeleaf template file
    }


//    @GetMapping("/map")
//    public String showMapPage() {
//        return "map"; // This will render map.html from templates
//    }

}
