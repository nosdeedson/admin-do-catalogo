package e3n.com.admin.catalogo.infrastructure.castmember;

import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberJpaEntity;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberRepository;
import e3n.com.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private CastMemberRepository castMemberRepository;

    public CastMemberMySQLGateway(CastMemberRepository castMemberRepository) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
    }


    @Override
    public CastMember create(CastMember member) {
        return castMemberRepository.save(CastMemberJpaEntity.from(member)).toAggregate();
    }

    @Override
    public void deleteById(CastMemberID id) {
        if (castMemberRepository.existsById(id.getValue())) {
            castMemberRepository.deleteById(id.getValue());
        }
    }

    @Override
    public List<CastMemberID> existsByIds(Iterable<CastMemberID> ids) {
        final var memberIds = StreamSupport.stream(ids.spliterator(), false).map(CastMemberID::getValue).toList();
        final var results = castMemberRepository.existSByIds(memberIds);
        return results.stream().map(CastMemberID::from).toList();
    }

    @Override
    public Optional<CastMember> findById(CastMemberID id) {
        return castMemberRepository.findById(id.getValue()).map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public Pagination<CastMember> findAll(SearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );
        final var where = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);
        final var pageResult = this.castMemberRepository.findAll(where, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public CastMember update(CastMember member) {
        return castMemberRepository.save(CastMemberJpaEntity.from(member)).toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
