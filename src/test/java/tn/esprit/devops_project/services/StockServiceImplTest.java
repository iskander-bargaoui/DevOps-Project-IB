package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
class StockServiceImplTest {

    // DONE
    @Autowired
    private StockServiceImpl stockService;

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void addStock() {
        final Stock stock = new Stock();
        stock.setTitle("Title");
        this.stockService.addStock(stock);
        assertEquals(this.stockService.retrieveAllStock().size(),2);
        assertEquals(this.stockService.retrieveStock(2L).getTitle(),"Title");
    }

    // Exception
    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Stock stock = this.stockService.retrieveStock(100L);
        });
    }
    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock() {
        final Stock stock = this.stockService.retrieveStock(1L);
        assertEquals("stock 1", stock.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveAllStock() {
        final List<Stock> allStocks = this.stockService.retrieveAllStock();
        assertEquals(allStocks.size(), 1);
    }
}