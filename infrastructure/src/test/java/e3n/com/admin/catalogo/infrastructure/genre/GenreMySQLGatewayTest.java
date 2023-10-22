package e3n.com.admin.catalogo.infrastructure.genre;


import e3n.com.admin.catalogo.MYSQLGatewayTest;
import e3n.com.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MYSQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private GenreMySQLGateway genreMySQLGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjections(){
        Assertions.assertNotNull(categoryMySQLGateway);
        Assertions.assertNotNull(genreMySQLGateway);
        Assertions.assertNotNull(genreRepository);
    }

    @Test
    public void todoAlltest(){
        // TODO
    }

}
