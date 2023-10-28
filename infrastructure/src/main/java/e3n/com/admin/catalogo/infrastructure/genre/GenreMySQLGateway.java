package e3n.com.admin.catalogo.infrastructure.genre;

import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.genre.GenreId;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
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
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySQLGateway(GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(Genre genre) {
        return this.genreRepository.save(GenreJpaEntity.from(genre))
                .toAggregate();
    }

    @Override
    public void deleteById(GenreId id) {
        if(findById(id).isPresent()){
            this.genreRepository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Genre> findById(GenreId id) {
        return this.genreRepository.findById(id.getValue())
                .map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(Genre genre) {
        return this.genreRepository.save(GenreJpaEntity.from(genre))
                .toAggregate();
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery query) {
        final var page = PageRequest.of(
                query.page(), query.perPage(), Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );
        final var where = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        var pageResult = this.genreRepository.findAll(Specification.where(where), page);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<GenreId> existsByIds(final Iterable<GenreId> genreIDS) {
        final var ids = StreamSupport.stream(genreIDS.spliterator(), false)
                .map(GenreId::getValue)
                .toList();
        return this.genreRepository.existsByIds(ids).stream()
                .map(GenreId::from)
                .toList();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms){
        return SpecificationUtils.like("name", terms);
    }

}
