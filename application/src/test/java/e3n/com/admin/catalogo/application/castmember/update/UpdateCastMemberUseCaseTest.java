package e3n.com.admin.catalogo.application.castmember.update;

import e3n.com.admin.catalogo.application.Fixture;
import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.castmember.CastMemberType;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {

        final var member = CastMember.newMember("eva mendes", CastMemberType.ACTRESS);
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedId = member.getId();
        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.of(CastMember.with(member)));

        Mockito.when(gateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId.getValue(), output.id());

        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.getName())
                        && Objects.equals(expectedType, cmd.getType())
                        && Objects.equals(expectedId, cmd.getId())
                        && Objects.nonNull(cmd.getCreatedAt())
                        && member.getUpdatedAt().isBefore(cmd.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final var member = CastMember.newMember("eva mendes", CastMemberType.ACTRESS);
        final var expectedType = Fixture.CastMembers.type();
        final var expectedId = member.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), null, expectedType);

        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.of(member));

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final var member = CastMember.newMember("eva mendes", CastMemberType.ACTRESS);
        final var expectedName = Fixture.name();
        final var expectedId = member.getId();
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, null);

        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.of(member));

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        final var member = CastMember.newMember("eva mendes", CastMemberType.ACTOR);
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), "Eva Mendes", CastMemberType.ACTRESS);
        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

}
