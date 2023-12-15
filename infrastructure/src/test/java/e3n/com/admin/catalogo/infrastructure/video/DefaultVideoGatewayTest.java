package e3n.com.admin.catalogo.infrastructure.video;


import e3n.com.admin.catalogo.Fixture;
import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway memberGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private VideoRepository videoRepository;

    private CastMember eva;
    private CastMember mariana;

    private Category aulas;
    private Category lives;

    private Genre tech;
    private Genre business;

    @BeforeEach
    public void setUp(){
        eva = memberGateway.create(Fixture.CastMembers.eva());
        mariana = memberGateway.create(Fixture.CastMembers.mariana());
        aulas = categoryGateway.create(Fixture.Categories.aulas());
        lives = categoryGateway.create(Fixture.Categories.lives());
        tech = genreGateway.create(Fixture.Genres.tech());
        business = genreGateway.create(Fixture.Genres.business());
    }

    @Test
    public void testInjection(){
        Assertions.assertNotNull(videoRepository);
        Assertions.assertNotNull(videoGateway);
        Assertions.assertNotNull(memberGateway);
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(business);
        Assertions.assertNotNull(lives);
        Assertions.assertNotNull(tech);
        Assertions.assertNotNull(aulas);
        Assertions.assertNotNull(eva);
        Assertions.assertNotNull(mariana);

    }

}
