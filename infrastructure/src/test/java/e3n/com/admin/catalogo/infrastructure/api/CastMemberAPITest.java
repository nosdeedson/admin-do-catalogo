package e3n.com.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import e3n.com.admin.catalogo.ApiTest;
import e3n.com.admin.catalogo.ControllerTest;
import com.E3N.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.E3N.admin.catalogo.application.castmember.create.CreateCastMemberOutput;
import com.E3N.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.E3N.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.E3N.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import com.E3N.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.E3N.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput;
import com.E3N.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.E3N.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.E3N.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import com.E3N.admin.catalogo.application.castmember.update.UpdateCastMemberOutput;
import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.castmember.CastMemberType;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.validation.Error;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("all")
@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase;

    @MockBean
    private DefaultListCastMembersUseCase listCastMembersUseCase;

    @MockBean
    private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnItsIdentifier() throws Exception {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        Mockito.when(createCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(CreateCastMemberOutput.from(expectedId));

        final var request = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(command));

        final var response = mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/cast_members/" + expectedId.getValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        Mockito.verify(createCastMemberUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCastMember_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember("Eva Mendes", expectedType);

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);
        final var expectedErrorMessage = "'name' should not be null";
        Mockito.when(createCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .content(mapper.writeValueAsString(command))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(createCastMemberUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnIt() throws Exception {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        Mockito.when(getCastMemberByIdUseCase.execute(Mockito.any()))
                .thenReturn(CastMemberOutput.from(member));

        final var request = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo(expectedType.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(member.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(member.getUpdatedAt().toString())));

        Mockito.verify(getCastMemberByIdUseCase, Mockito.times(1))
                .execute(Mockito.eq(expectedId.getValue()));

    }

    @Test
    public void givenAInvalidId_whenCallsGetByIdAndCastMemberDoesntExists_shouldReturnNotFound() throws Exception {
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "Cast member whith ID 123 was not found";
        Mockito.when(getCastMemberByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var respponse = mvc.perform(request).andDo(MockMvcResultHandlers.print());

        respponse.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(getCastMemberByIdUseCase, Mockito.times(1)).execute(Mockito.eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() throws Exception {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(UpdateCastMemberOutput.from(expectedId));

        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(command));

        final var response = mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));
        Mockito.verify(updateCastMemberUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember("Eva memdes", expectedType);
        final var expectedId = member.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .content(mapper.writeValueAsString(command))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros[0].message", Matchers.equalTo(expectedErrorMessage)));
        Mockito.verify(updateCastMemberUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));

    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldReturnNotFound() throws Exception {
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "Cast member whith ID 123 was not found";
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(new Error(expectedErrorMessage)));

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())

        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        final var expectedId = CastMemberID.from("123");

        Mockito.doNothing().when(deleteCastMemberUseCase).execute(Mockito.any());

        final var request = MockMvcRequestBuilders.delete("/cast_members/{id}", expectedId.getValue())
        .with(ApiTest.CAST_MEMBERS_JWT);

        final var response = mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(deleteCastMemberUseCase, Mockito.times(1)).execute(Mockito.eq(expectedId.getValue()));
    }

    @Test
    public void givenValidParams_whenCallListCastMembers_shouldReturnIt() throws Exception {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 10;
        final var expectedItems = List.of(CastMemberListOutput.from(member));
        final var expectedTerms = "Eva";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        Mockito.when(listCastMembersUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .queryParam("search", expectedTerms)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(member.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(expectedType.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(member.getCreatedAt().toString())));

        Mockito.verify(listCastMembersUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedPage, cmd.page())
                        && Objects.equals(expectedPerPage, cmd.perPage())
                        && Objects.equals(expectedSort, cmd.sort())
                        && Objects.equals(expectedTerms, cmd.terms())
                        && Objects.equals(expectedDirection, cmd.direction())
        ));
    }

    @Test
    public void givenEmptyParams_whenCallListCastMembers_shouldUseDefaultsAndReturnIt() throws Exception {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;
        final var member = CastMember.newMember(expectedName, expectedType);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 10;
        final var expectedItems = List.of(CastMemberListOutput.from(member));
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        Mockito.when(listCastMembersUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(member.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(expectedType.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(member.getCreatedAt().toString())));

        Mockito.verify(listCastMembersUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedPage, cmd.page())
                        && Objects.equals(expectedPerPage, cmd.perPage())
                        && Objects.equals(expectedSort, cmd.sort())
                        && Objects.equals(expectedTerms, cmd.terms())
                        && Objects.equals(expectedDirection, cmd.direction())
        ));
    }


}
