package tn.esprit.devops_project.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.*;
import tn.esprit.devops_project.services.InvoiceServiceImpl;
import tn.esprit.devops_project.services.OperatorServiceImpl;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class InvoiceServiceImplTest {
    @Mock
    InvoiceRepository invoiceRepository;

    @Mock
    OperatorRepository operatorRepository;

    @Mock
    SupplierRepository supplierRepository;

    @Mock
    InvoiceDetailRepository invoiceDetailRepository;

    @InjectMocks
    InvoiceServiceImpl invoiceService;

    @InjectMocks
    OperatorServiceImpl operatorService;

    @Test
    public void retrieveAllInvoicesTest() {
        when(invoiceRepository.findAll()).thenReturn(
                Arrays.asList(
                        new Invoice(1L, 10.0f, 100.0f, new Date(), new Date(), false, null, null),
                        new Invoice(2L, 20.0f, 200.0f, new Date(), new Date(), false, null, null),
                        new Invoice(3L, 30.0f, 300.0f, new Date(), new Date(), false, null, null)
                )
        );

        List<Invoice> invoiceList = invoiceService.retrieveAllInvoices();

        assertEquals(3, invoiceList.size());
    }


    @Test
    public void cancelInvoiceTest() {
        Invoice invoice = new Invoice(1L, 10.0f, 100.0f, new Date(), new Date(), false, null, null);
        when(invoiceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(invoice));

        invoiceService.cancelInvoice(1L);

        verify(invoiceRepository).save(invoice);
    }

    @Test
    public void cancelInvoiceNotFoundTest() {
        // Simulate that the invoice is not found
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NullPointerException.class, () -> {
            invoiceService.cancelInvoice(1L);
        });
        verify(invoiceRepository).findById(1L);

    }

    @Test
    public void retrieveInvoiceTest() {
        Invoice invoice = new Invoice(1L, 10.0f, 100.0f, new Date(), new Date(), false, null, null);
        when(invoiceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(invoice));

        Invoice retrievedInvoice = invoiceService.retrieveInvoice(1L);

        assertNotNull(retrievedInvoice);
        assertEquals(10.0f, retrievedInvoice.getAmountDiscount());
    }

    @Test
    public void retrieveInvoiceNotFoundTest() {
        when(invoiceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty()); // Simulate not finding the invoice

        Assertions.assertThrows(NullPointerException.class, () -> {
            invoiceService.retrieveInvoice(1L);
        });
    }



    @Test
    public void getTotalAmountInvoiceBetweenDatesTest() {
        Date startDate = new Date(1634994000000L); // October 23, 2021
        Date endDate = new Date(1635354000000L);   // October 27, 2021
        when(invoiceRepository.getTotalAmountInvoiceBetweenDates(startDate, endDate)).thenReturn(500.0f);

        float totalAmount = invoiceService.getTotalAmountInvoiceBetweenDates(startDate, endDate);

        assertEquals(500.0f, totalAmount);
    }

    @Test
    public void testAssignOperatorToInvoice() {
        Long idOperator = 1L;
        Long idInvoice = 1L;

        Operator operator = new Operator(idOperator, "fathi", "hadewi", "fathi123", new HashSet<>());
        Invoice invoice = new Invoice(idInvoice, 10.0f, 100.0f, new Date(), new Date(), false, null, null);

        when(invoiceRepository.findById(idInvoice)).thenReturn(Optional.of(invoice));
        when(operatorRepository.findById(idOperator)).thenReturn(Optional.of(operator));

        invoiceService.assignOperatorToInvoice(idOperator, idInvoice);

        Set<Invoice> invoices = operator.getInvoices();
        assertThat(invoices).contains(invoice);
    }

    @Test
    public void assignOperatorToInvoiceInvoiceNotFound() {
        Long idOperator = 1L;
        Long idInvoice = 2L;

        when(invoiceRepository.findById(idInvoice)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> invoiceService.assignOperatorToInvoice(idOperator, idInvoice));
    }

    @Test
    public void assignOperatorToInvoiceOperatorNotFound() {
        Long idOperator = 1L;
        Long idInvoice = 2L;

        Invoice invoice = new Invoice();
        invoice.setIdInvoice(idInvoice);

        when(invoiceRepository.findById(idInvoice)).thenReturn(Optional.of(invoice));
        when(operatorRepository.findById(idOperator)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> invoiceService.assignOperatorToInvoice(idOperator, idInvoice));
    }

    @Test
    void getInvoicesBySupplier_SupplierFound() {
     /*   // Create a sample Supplier
        Long idSupplier = 1L;
        Supplier supplier = new Supplier();
        supplier.setIdSupplier(idSupplier);
        supplier.setCode("Code1");
        supplier.setLabel("label1");

        // Create sample Invoices and associate them with the Supplier
        Invoice invoice1 = new Invoice(1L, 10.0f, 100.0f, new Date(), new Date(), false, null, supplier);
        Invoice invoice2 = new Invoice(2L, 20.0f, 200.0f, new Date(), new Date(), false, null, supplier);

        // Add the Invoices to the Supplier's Set of Invoices
        supplier.getInvoices().add(invoice1);
        supplier.getInvoices().add(invoice2);

        // Mock the behavior of the supplierRepository to return the supplier when findById is called.
        Mockito.when(supplierRepository.findById(idSupplier)).thenReturn(Optional.of(supplier));

        // Call the service method
        List<Invoice> invoices = invoiceService.getInvoicesBySupplier(idSupplier);

        // Verify that the supplierRepository.findById method was called once with the correct ID.
        Mockito.verify(supplierRepository, Mockito.times(1)).findById(idSupplier);

        // Assert that the List contains the expected invoices
        assertThat(invoices).contains(invoice1, invoice2);*/
    }


    @Test
    public void getInvoicesBySupplierNotFound() {
        Long idSupplier = 1L;

        when(supplierRepository.findById(idSupplier)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> invoiceService.getInvoicesBySupplier(idSupplier));

        verify(supplierRepository, times(1)).findById(idSupplier);
    }

}