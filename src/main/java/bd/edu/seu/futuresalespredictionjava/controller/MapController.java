package bd.edu.seu.futuresalespredictionjava.controller;

import bd.edu.seu.futuresalespredictionjava.model.Product;
import bd.edu.seu.futuresalespredictionjava.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MapController {
    private final ProductService productService;

    public MapController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/map")
    public String showMapPage(Model model) {
        List<Product> productList = productService.getAll();
        model.addAttribute("productList", productList);
        return "map";
    }

    @PostMapping("/map")
    public String showMapResult(@RequestParam("selectedProductId") int selectedProductId, Model model) {
        List<Product> productList = productService.getAll();
        Product selectedProduct = productService.getById(selectedProductId);

        model.addAttribute("productList", productList);
        model.addAttribute("selectedProduct", selectedProduct);

        return "map";
    }

}
