package e3n.com.admin.catalogo.infrastructure.genre;


import e3n.com.admin.catalogo.MYSQLGatewayTest;
import e3n.com.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import org.springframework.beans.factory.annotation.Autowired;

@MYSQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

}
