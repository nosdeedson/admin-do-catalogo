package e3n.com.admin.catalogo.application.castmember.retrieve.get;

import e3n.com.admin.catalogo.application.Fixture;
import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetCastMemberByIdUseCaseTest extends UseCaseTest {

    @Mock
    private CastMemberGateway gateway;

    @InjectMocks
    private DefaultGetCastMemberByIdUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var member = CastMember.newMember(expectedName, expectedType);

        final var expectedId = member.getId();
        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.of(member));

        final var output = useCase.execute(expectedId.getValue());

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedType, output.type());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }

}
