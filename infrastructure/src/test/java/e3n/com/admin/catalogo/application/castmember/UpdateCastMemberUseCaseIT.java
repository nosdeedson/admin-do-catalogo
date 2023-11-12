package e3n.com.admin.catalogo.application.castmember;

import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import e3n.com.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import e3n.com.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.castmember.CastMemberType;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberJpaEntity;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {

        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();


        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        repository.saveAndFlush(CastMemberJpaEntity.from(member.update("ev men", CastMemberType.ACTOR)));
        Assertions.assertEquals(1, repository.count());

        final var output = useCase.execute(command);

        final var updatedMember = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, updatedMember.getName());
        Assertions.assertEquals(expectedType, updatedMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), updatedMember.getCreatedAt());
        Assertions.assertTrue(member.getUpdatedAt().isBefore(updatedMember.getUpdatedAt()));
        Assertions.assertEquals(1, repository.count());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTRESS;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var member = CastMember.newMember("Mariana Rios", CastMemberType.ACTRESS);
        final var expectedId = member.getId();
        repository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, repository.count());

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final var expectedName = "Eva Mendes";
        final CastMemberType expectedType = null;

        final var member = CastMember.newMember("Mariana Rios", CastMemberType.ACTRESS);
        final var expectedId = member.getId();
        repository.saveAndFlush(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;
        Assertions.assertEquals(1, repository.count());

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(1, repository.count());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        repository.saveAndFlush(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        Assertions.assertEquals(1, repository.count());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }
}
