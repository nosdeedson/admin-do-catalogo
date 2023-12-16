package e3n.com.admin.catalogo.application.castmember.retrieve.get;

import com.E3N.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.castmember.CastMemberType;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberJpaEntity;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class GetCastMemberByIdUseCaseIT {

    @Autowired
    private GetCastMemberByIdUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        Assertions.assertEquals(0, repository.count());
        repository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, repository.count());

        final var output = useCase.execute(expectedId.getValue());

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedType, output.type());
        Assertions.assertEquals(member.getUpdatedAt(), output.updatedAt());
        Assertions.assertEquals(member.getCreatedAt(), output.createdAt());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        Assertions.assertEquals(0, repository.count());
        repository.saveAndFlush(CastMemberJpaEntity.from(member));

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        Assertions.assertEquals(1, repository.count());
        Mockito.verify(gateway).findById(Mockito.any());
    }

}
