package com.E3N.admin.catalogo.application;

import com.github.javafaker.Faker;
import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberType;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.utils.IdUtils;
import e3n.com.admin.catalogo.domain.video.*;

import java.time.Year;
import java.util.Set;
import java.util.UUID;

import static io.vavr.API.*;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 20.0);
    }

    public static boolean aBoolean() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
        );
    }

    public static String checksum() {
        return UUID.randomUUID().toString();
    }

    public static Video video() {
        return Video.newVideo(
                Fixture.title(),
                Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.aBoolean(),
                Fixture.aBoolean(),
                Videos.rating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.eva().getId(), CastMembers.mariana().getId())

        );
    }

    public static final class Categories {
        private static final Category AULAS = Category.newCategory("Aulas", "Description", true);

        private static final Category LIVES = Category.newCategory("Lives", "Description", true);

        public static Category aulas() {
            return AULAS.clone();
        }

        public static Category lives() {
            return LIVES.clone();
        }
    }

    public static final class CastMembers {

        private static final CastMember EVA = CastMember.newMember("Eva Mendes", CastMemberType.ACTRESS);

        private static final CastMember MARIANA = CastMember.newMember("Mariana Rios", CastMemberType.ACTRESS);

        public static CastMember eva() {
            return CastMember.with(EVA);
        }

        public static CastMember mariana() {
            return CastMember.with(MARIANA);
        }

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.ACTRESS, CastMemberType.DIRECTOR);
        }
    }

    public static final class Genres {
        private static final Genre TECH = Genre.newGenre("Technology", true);

        private static final Genre BUSINESS = Genre.newGenre("Business", true);

        public static Genre tech() {
            return Genre.with(TECH);
        }

        public static Genre business() {
            return Genre.with(BUSINESS);
        }
    }

    public static final class Videos {

        private static final Video SYSTEM_DESIGN = Video.newVideo(
                "System Design no Mercado Livre na prática",
                description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.aBoolean(),
                Fixture.aBoolean(),
                rating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.eva().getId(), CastMembers.mariana().getId())
        );

        public static Video systemDesign() {
            return Video.with(SYSTEM_DESIGN);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static VideoMediaType videoMediaType() {
            return FAKER.options().option(VideoMediaType.values());
        }

        public static Resource resource(final VideoMediaType type) {
            final String contentType = Match(type).of(
                    Case($(List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final String checksum = IdUtils.uuid();
            final byte[] content = "Conteudo".getBytes();

            return Resource.with(content, checksum, contentType, type.name().toLowerCase());
        }

        public static String description() {
            return FAKER.options().option(
                    """
                                ajakjajlajlfkajjaljflaljlaajçlkjasdfgzxcv;.,mn
                            """,
                    """
                            aasdfçkljçlkjasdfgpoiuqwer;.,mzxcv
                            """
            );
        }

        public static AudioVideoMedia audioVideoMedia(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return AudioVideoMedia.with(
                    checksum, type.name().toLowerCase(),
                    "/videos/" + checksum
            );
        }

        public static ImageMedia image(final VideoMediaType type){
            final var checksum = Fixture.checksum();
            return ImageMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/images/" + checksum
            );
        }

    }
}
