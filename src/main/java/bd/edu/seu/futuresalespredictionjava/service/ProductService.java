package bd.edu.seu.futuresalespredictionjava.service;

import bd.edu.seu.futuresalespredictionjava.model.Product;
import bd.edu.seu.futuresalespredictionjava.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void save(Product product) {
        productRepository.save(product);
    }


    public Product getById(int pId) {
        return productRepository.findBypId(pId).orElse(null);
    }

    public void update(Product product) {
        Optional<Product> existing = productRepository.findBypId(product.getPId());
        if (existing.isPresent()) {
            Product p = existing.get();
            p.setName(product.getName());
            p.setCategory(product.getCategory());
            p.setPrice(product.getPrice());
            p.setLocation(product.getLocation());
            p.setLatitude(product.getLatitude());        // Add this line
            p.setLongitude(product.getLongitude());      // Add this line
            productRepository.save(p);
        }
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }


    public void delete(int productId) {
        productRepository.deleteBypId(productId);  //deleteById emni ashe default
        //but ekhane deleteBypId dawar jonno extra query repository te likhe ekhne pass korte hoise
    }



    private static final Map<String, double[]> CITY_COORDINATES = Map.of(
            "Dhaka", new double[]{23.8103, 90.4125},
            "Chittagong", new double[]{22.3569, 91.7832},
            "Rajshahi", new double[]{24.3636, 88.6241},
            "Barisal", new double[]{22.7010, 90.3535},
            "Sylhet", new double[]{24.8949, 91.8687},
            "Khulna", new double[]{22.8456, 89.5403},
            "Rangpur", new double[]{25.7439, 89.2752}
    );

    public void autoFillCoordinates(Product product) {
        if (product.getLocation() != null) {
            String city = product.getLocation().trim().toLowerCase();
            for (String key : CITY_COORDINATES.keySet()) {
                if (key.toLowerCase().equals(city)) {
                    double[] coords = CITY_COORDINATES.get(key);
                    product.setLatitude(coords[0]);
                    product.setLongitude(coords[1]);
                    break;
                }
            }
        }
    }



}
