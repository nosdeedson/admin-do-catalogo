package e3n.com.admin.catalogo.application.genre.retrieve.list;

import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;

public class DefaultListGenreUseCase extends ListGenreUseCase {

    private final GenreGateway genreGateway;

    public DefaultListGenreUseCase(GenreGateway genreGateway) {
        this.genreGateway = genreGateway;
    }

    @Override
    public Pagination<GenreLIstOutput> execute(SearchQuery searchQuery) {
        return this.genreGateway.findAll(searchQuery)
                .map(GenreLIstOutput::from);
    }
}
