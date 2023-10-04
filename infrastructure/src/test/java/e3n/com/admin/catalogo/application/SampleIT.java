package e3n.com.admin.catalogo.application;


import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.MySQLGatewayTest;
import e3n.com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class SampleIT {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CreateCategoryUseCase useCase;

    @Test
    public void test(){
        Assertions.assertNotNull(categoryRepository);
       // Assertions.assertNotNull(useCase);
    }
}
