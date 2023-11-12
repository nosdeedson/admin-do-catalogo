package e3n.com.admin.catalogo.infrastructure.configuration.usecases;


import e3n.com.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import e3n.com.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import e3n.com.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import e3n.com.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import e3n.com.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import e3n.com.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import e3n.com.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import e3n.com.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import e3n.com.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import e3n.com.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CastMemberUseCaseConfig {

    private CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase(){
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase(){
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase(){
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase(){
        return new  DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberuseCase(){
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }
}
