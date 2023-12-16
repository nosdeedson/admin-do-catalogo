package com.E3N.admin.catalogo.application.castmember.delete;

import com.E3N.admin.catalogo.application.Fixture;
import com.E3N.admin.catalogo.application.UseCaseTest;
import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = member.getId();

        Mockito.doNothing().when(gateway).deleteById(Mockito.any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        final var expectedId = CastMemberID.from("123");
        Mockito.doNothing().when(gateway).deleteById(Mockito.any());
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(gateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = member.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(gateway).deleteById(Mockito.any());
        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertNotNull(exception);
        Mockito.verify(gateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }
}
