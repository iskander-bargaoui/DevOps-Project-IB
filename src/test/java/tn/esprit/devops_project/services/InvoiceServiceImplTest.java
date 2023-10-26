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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class InvoiceServiceImplTest {

    @Autowired
    InvoiceServiceImpl invoiceService;
    @Autowired
    StockServiceImpl stockService;
    @Autowired
    SupplierServiceImpl supplierService;
    @Autowired
    OperatorServiceImpl operatorService;



    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveAllInvoices() {
        final List<Invoice> allInvoices = this.invoiceService.retrieveAllInvoices();
        assertEquals(allInvoices.size(), 1);
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void cancelInvoice() {
        Long invoiceId = 1L; // Adjust with the invoice ID from your test data

        // Act - Call the cancelInvoice method
        invoiceService.cancelInvoice(invoiceId);

        // Assert - Verify that the invoice is archived
        final Invoice invoice = this.invoiceService.retrieveInvoice(invoiceId);
        assertNotNull(invoice);
        assertTrue(invoice.getArchived());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveInvoice() {
        final Invoice inv = this.invoiceService.retrieveInvoice(1L);
        assertNotNull(inv);
        assertEquals(1L, inv.getIdInvoice());
    //    assertEquals("label 1", inv.getLabel()); // Adjust the expected size based on your test data
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    @DatabaseSetup("/data-set/supplier-data.xml")
    void getInvoicesBySupplier() {
       /* // To Re-Check
        // Retrieve the supplier with ID 1
        Supplier supplier = supplierService.retrieveSupplier(1L);

        assertNotNull(supplier, "Supplier should not be null");

        // Retrieve all invoices
        List<Invoice> allInvoices = invoiceService.retrieveAllInvoices();

        // Check that the invoices belong to the correct supplier
        for (Invoice invoice : allInvoices) {
            assertEquals(supplier, invoice.getSupplier(),
                    "Invoice should belong to the correct supplier");
        }*/
    }


    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    @DatabaseSetup("/data-set/operator-data.xml")
    void assignOperatorToInvoice() {
        /*final Operator operator = this.operatorService.retrieveOperator(1L);
        final Invoice invoice = this.invoiceService.retrieveInvoice(1L);
        this.invoiceService.assignOperatorToInvoice(1L, 1L);
        assertEquals(1, operator.getInvoices().size());*/
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getTotalAmountInvoiceBetweenDates() {

    }

    // Exception 1
    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Invoice invoice = this.invoiceService.retrieveInvoice(100L);
        });
    }
}