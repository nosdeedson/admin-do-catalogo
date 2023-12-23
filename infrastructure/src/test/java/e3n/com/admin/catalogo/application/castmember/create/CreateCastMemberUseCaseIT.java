package e3n.com.admin.catalogo.application.castmember.create;


import com.E3N.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.E3N.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import com.E3N.admin.catalogo.domain.castmember.CastMemberType;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.infrastructure.castmember.CastMemberMySQLGateway;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateCastMemberUseCaseIT {

    @Autowired
    private CreateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberMySQLGateway gateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;

        Assertions.assertEquals(0, repository.count());

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        final var output = useCase.execute(command);
        Assertions.assertEquals(1, repository.count());

        Assertions.assertNotNull(output.id());

        final var persistedMember = repository.findById(output.id()).get();
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertNotNull(persistedMember.getCreatedAt());
        Assertions.assertNotNull(persistedMember.getUpdatedAt());

        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.any());

    }

    @Test
    public void givenAInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTRESS;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        Assertions.assertEquals(0, repository.count());

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAInvalidType_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        final var expectedName = "Eva Mendes";
        final CastMemberType expectedType = null;

        Assertions.assertEquals(0, repository.count());

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;
        Assertions.assertEquals(0, repository.count());

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }
}
