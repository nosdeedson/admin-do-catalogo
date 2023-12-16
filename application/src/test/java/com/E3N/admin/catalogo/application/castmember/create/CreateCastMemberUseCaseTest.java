package com.E3N.admin.catalogo.application.castmember.create;

import com.E3N.admin.catalogo.application.Fixture;
import com.E3N.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;


public class CreateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        Mockito.when(gateway.create(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway).create(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.getName())
                        && Objects.equals(expectedType, cmd.getType())
                        && Objects.nonNull(cmd.getCreatedAt())
                        && Objects.nonNull(cmd.getUpdatedAt())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        final var expectedType = Fixture.CastMembers.type();

        final var command = CreateCastMemberCommand.with(null, expectedType);

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

    }

    @Test
    public void givenAInvalidType_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        final String expectedName = Fixture.name();

        final var command = CreateCastMemberCommand.with(expectedName, null);

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var exception = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

}






