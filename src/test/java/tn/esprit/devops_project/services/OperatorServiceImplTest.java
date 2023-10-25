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
import tn.esprit.devops_project.entities.*;

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
class OperatorServiceImplTest {

    @Autowired
    private OperatorServiceImpl operatorService;
    @Autowired
    private StockServiceImpl stockService;

    @DatabaseSetup("/data-set/operator-data.xml")
    @Test
    void retrieveAllOperators() {
        final List<Operator> allOperators = this.operatorService.retrieveAllOperators();
        assertEquals(1, allOperators.size());
    }

    @DatabaseSetup("/data-set/operator-data.xml")
    @Test
    void addOperator() {
        final Operator operator = new Operator();
      //  operator.setIdOperateur(2L);
        operator.setFname("fname");
        operator.setLname("lname 2");
        operator.setPassword("pass 2");
        this.operatorService.addOperator(operator);
        assertEquals(this.operatorService.retrieveAllOperators().size(),2);
        assertEquals(this.operatorService.retrieveOperator(2L).getFname(),"fname");
    }

    @DatabaseSetup("/data-set/operator-data.xml")
    @Test
    void deleteOperator() {
        Operator operator = this.operatorService.retrieveOperator(1L);
        this.operatorService.deleteOperator(operator.getIdOperateur());
    }

    @DatabaseSetup("/data-set/operator-data.xml")
    @Test
    void updateOperator() {
        Operator operator = this.operatorService.retrieveOperator(1L);
        operator.setPassword("Updated Password");
        this.operatorService.updateOperator(operator);
        Operator operator1 = this.operatorService.retrieveOperator(1L);
        assertEquals("Updated Password", operator1.getPassword());
    }

    @DatabaseSetup("/data-set/operator-data.xml")
    @Test
    void retrieveOperator() {
        final Operator operator = this.operatorService.retrieveOperator(1L);
        assertNotNull(operator);
        assertEquals(1L, operator.getIdOperateur());
        assertEquals("pass", operator.getPassword());
    }

    // Exception Retrieve Operator
    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveSupplier_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Operator operator = this.operatorService.retrieveOperator(50L);
        });
    }
}