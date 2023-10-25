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
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;

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
class SupplierServiceImplTest {

    @Autowired
    SupplierServiceImpl supplierService;

    // DONE
    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveAllSuppliers() {
        final List<Supplier> allSuppliers = this.supplierService.retrieveAllSuppliers();
        assertEquals(allSuppliers.size(), 1);
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void addSupplier() {
        final Supplier supplier = new Supplier();
        supplier.setIdSupplier(2L);
        supplier.setSupplierCategory(SupplierCategory.ORDINAIRE);
        supplier.setCode("c2");
        supplier.setLabel("label 2");
        this.supplierService.addSupplier(supplier);
        assertEquals(this.supplierService.retrieveAllSuppliers().size(),2);
        assertEquals(this.supplierService.retrieveSupplier(2L).getCode(),"c2");
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void updateSupplier() {
        // Load an existing supplier from the dataset
        Supplier existingSupplier = this.supplierService.retrieveSupplier(1L);

        // Modify the supplier's data
        existingSupplier.setLabel("Updated Label");

        // Call the update method
        this.supplierService.updateSupplier(existingSupplier);

        // Retrieve the supplier again and assert that the changes were saved
        Supplier updatedSupplier = this.supplierService.retrieveSupplier(1L);
        assertEquals("Updated Label", updatedSupplier.getLabel());
    }




    @DatabaseSetup("/data-set/supplier-data.xml")
    @Test
    void retrieveSupplier() {
        final Supplier supplier = this.supplierService.retrieveSupplier(1L);
        assertNotNull(supplier);
        assertEquals(1L, supplier.getIdSupplier());
        assertEquals("label 1", supplier.getLabel());

    }

    @DatabaseSetup("/data-set/supplier-data.xml")
    @Test
    void deleteSupplier() {
        // Load an existing supplier from the dataset
        Supplier supplierToDelete = this.supplierService.retrieveSupplier(1L);

        // Call the delete method
        this.supplierService.deleteSupplier(supplierToDelete.getIdSupplier());
//
//        // Attempt to retrieve the deleted supplier, it should be null
//        Supplier deletedSupplier = this.supplierService.retrieveSupplier(2L);
//        assertNull(deletedSupplier);
    }

    // Exception Retrieve Supplier
    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveSupplier_Null() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            final Supplier supplier = this.supplierService.retrieveSupplier(50L);
        });
    }
}