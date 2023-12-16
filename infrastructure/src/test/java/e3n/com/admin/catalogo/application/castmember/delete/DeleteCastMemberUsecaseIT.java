package e3n.com.admin.catalogo.application.castmember.delete;

import com.E3N.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.castmember.CastMemberType;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberJpaEntity;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class DeleteCastMemberUsecaseIT {

    @Autowired
    private DeleteCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        repository.saveAndFlush(CastMemberJpaEntity.from(member));
        repository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Mariana Rios", CastMemberType.ACTRESS)));

        Assertions.assertEquals(2, repository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(1, repository.count());
        Mockito.verify(gateway, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {

        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = CastMemberID.from("123");

        repository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, repository.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(1, repository.count());
        Mockito.verify(gateway, Mockito.times(1)).deleteById(Mockito.any());

    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedErrorMessage = "Gateway error";
        repository.saveAndFlush(CastMemberJpaEntity.from(member));

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(gateway).deleteById(Mockito.any());

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(member.getId().getValue()));

        Mockito.verify(gateway, Mockito.times(1)).deleteById(Mockito.any());
        Assertions.assertEquals(1, repository.count());
    }

}
