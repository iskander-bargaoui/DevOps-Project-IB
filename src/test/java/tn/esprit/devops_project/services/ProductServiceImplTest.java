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
    private StockServiceImpl stockService;

    @DatabaseSetup("/data-set/product-data.xml")
    @Test
    void addProduct() {
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @Test
    void retrieveProduct() {
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @Test
    void retreiveAllProduct() {
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @Test
    void retrieveProductByCategory() {
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @Test
    void deleteProduct() {
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @Test
    void retreiveProductStock() {
    }
}