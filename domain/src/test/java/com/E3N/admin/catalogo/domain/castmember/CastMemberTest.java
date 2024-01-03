package com.E3N.admin.catalogo.domain.castmember;

import com.E3N.admin.catalogo.domain.UnitTest;
import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberType;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var member = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(member);
        Assertions.assertNotNull(member.getId());
        Assertions.assertEquals(expectedName, member.getName());
        Assertions.assertEquals(expectedType, member.getType());
        Assertions.assertNotNull(member.getCreatedAt());
        Assertions.assertNotNull(member.getUpdatedAt());
    }

    @Test
    public void givenAInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var exception = Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(null, expectedType));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var exception = Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
    }

    @Test
    public void givenAInvalidNameWithLengthMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;

        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var exception = Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
    }

    @Test
    public void givenAInvalidNullType_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = "Vin Diesel";
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;
        final var exception = Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, null));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var member = CastMember.newMember("vind", CastMemberType.ACTRESS);

        Assertions.assertNotNull(member);
        Assertions.assertNotNull(member.getId());

        final var createdAt = member.getCreatedAt();
        final var updatedAt = member.getUpdatedAt();
        final var updatedMember = member.update(expectedName, expectedType);
        Assertions.assertEquals(expectedName, updatedMember.getName());
        Assertions.assertEquals(expectedType, updatedMember.getType());
        Assertions.assertEquals(createdAt, updatedMember.getCreatedAt());
        Assertions.assertTrue(updatedAt.isBefore(updatedMember.getUpdatedAt()));

    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNullName_shouldReceiveNotification() {
        final var member = CastMember.newMember("Eva Mendes", CastMemberType.ACTRESS);
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertNotNull(member);
        Assertions.assertNotNull(member.getId());

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> member.update(null, CastMemberType.ACTRESS));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification() {
        final var member = CastMember.newMember("Eva Mendes", CastMemberType.ACTRESS);
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        Assertions.assertNotNull(member);
        Assertions.assertNotNull(member.getId());

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> member.update(" ", CastMemberType.ACTRESS));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithLengthMoreThan255_shouldReceiveNotification() {
        final var expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;

        final var member = CastMember.newMember("Eva Mendes", CastMemberType.ACTRESS);
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        Assertions.assertNotNull(member);
        Assertions.assertNotNull(member.getId());

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> member.update(expectedName, CastMemberType.ACTRESS));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNullType_shouldReceiveNotification() {
        final var member = CastMember.newMember("Eva Mendes", CastMemberType.ACTRESS);
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertNotNull(member);
        Assertions.assertNotNull(member.getId());

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> member.update("Eva Mendes", null));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }
}
