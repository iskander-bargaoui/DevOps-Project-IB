package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private StockServiceImpl stockService;

    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    @Test
    void addProduct() {
        final Product product = new Product();
        Stock stock = this.stockService.retrieveStock(1L);
        product.setStock(stock);
       // product.setIdProduct(2L);
        product.setTitle("t2");
        product.setPrice(2);
        product.setQuantity(2);
        product.setCategory(ProductCategory.ELECTRONICS);
        this.productService.addProduct(product, stock.getIdStock());
        assertEquals(2, this.productService.retreiveAllProduct().size());
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    @Test
    void retrieveProduct() {
        final Product product = this.productService.retrieveProduct(1L);
        assertNotNull(product);
        assertEquals(1L, product.getIdProduct());
        assertEquals("t1", product.getTitle());
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    @Test
    void retrieveAllProduct() {
        final List<Product> allProducts = this.productService.retreiveAllProduct();
        assertEquals(1, allProducts.size());
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    @Test
    void retrieveProductByCategory() {
        // Specify the category you want to test
        ProductCategory categoryToTest = ProductCategory.ELECTRONICS;

        // Retrieve products by the specified category
        List<Product> productsByCategory = this.productService.retrieveProductByCategory(categoryToTest);

        // Ensure that the list is not null
        assertNotNull(productsByCategory);

        // Perform assertions on the retrieved products, such as checking their category.
        // Example:
        for (Product product : productsByCategory) {
            assertEquals(categoryToTest, product.getCategory());
        }
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    @Test
    void deleteProduct() {
        // Load an existing product from the dataset
        Product productToDelete = this.productService.retrieveProduct(1L);

        // Call the delete method
        this.productService.deleteProduct(productToDelete.getIdProduct());
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    @Test
    void retrieveProductStock() {
        Stock stock = this.stockService.retrieveStock(1L);
        final List<Product> allProducts = this.productService.retreiveProductStock(2L);
        for (Product product : allProducts){
            assertEquals(product.getStock(),stock);
        }
    }

    // Exception Stock not Found [lambda Expression]
    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveStock_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Stock stock = this.stockService.retrieveStock(100L);
        });
    }


    // Exception Product not Found [lambda Expression]
    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProduct_nullId() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Product product = new Product();
            Long idStock = 50L;
            productService.addProduct(product, idStock);
        });
        assertEquals("stock not found", exception.getMessage());

    }

}