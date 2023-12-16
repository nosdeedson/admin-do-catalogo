package e3n.com.admin.catalogo;

import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberType;
import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.genre.Genre;
import com.E3N.admin.catalogo.domain.utils.IdUtils;
import com.E3N.admin.catalogo.domain.video.*;
import e3n.com.admin.catalogo.domain.video.*;

import java.time.Year;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static io.vavr.API.*;

public class Fixture {

    private static final Random random = new Random();

    private static String randomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static String title() {
        return randomString();
    }

    public static String description() {
        return randomString();
    }

    public static String checksum() {
        return UUID.randomUUID().toString();
    }

    public static Year year() {
        final var inteiro = random.nextInt(2020, 2030);
        return Year.of(inteiro);
    }

    public static double duration() {
        return random.nextDouble(30.0, 120.0);
    }

    public static boolean bool() {
        return random.nextBoolean();
    }

    public static Rating rating() {
        int value = random.nextInt(0, 6);
        for (Rating r : Rating.values()) {
            if (r.ordinal() == value) {
                return r;
            }
        }
        return Rating.ER;
    }

    public static VideoMediaType videoMediaType() {
        int value = random.nextInt(0, 6);
        for (VideoMediaType v : VideoMediaType.values()) {
            if (v.ordinal() == value) {
                return v;
            }
        }
        return VideoMediaType.VIDEO;
    }

    public static Video video() {
        return Video.newVideo(
                "title",
                "descripion",
                year(),
                duration(),
                bool(),
                bool(),
                rating(),
                Set.of(Categories.aulas().getId(), Categories.lives().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.eva().getId(),
                        CastMembers.mariana().getId())

        );
    }

    public static final class Categories {
        private static final Category AULAS = Category.newCategory("Aulas",
                "Description", true);

        private static final Category LIVES = Category.newCategory("Lives",
                "Description", true);

        public static Category aulas() {
            return AULAS;
        }

        public static Category lives() {
            return LIVES;
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
            int value = random.nextInt(0, 6);
            for (CastMemberType cm : CastMemberType.values()) {
                if (cm.ordinal() == value) {
                    return cm;
                }
            }
            return CastMemberType.ACTRESS;
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
                Fixture.year(),
                Fixture.duration(),
                bool(),
                bool(),
                rating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.eva().getId(), CastMembers.mariana().getId())
        );

        public static Video systemDesign() {
            return Video.with(SYSTEM_DESIGN);
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

        public static AudioVideoMedia audioVideoMedia(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return AudioVideoMedia.with(
                    checksum, type.name().toLowerCase(),
                    "/videos/" + checksum
            );
        }

        public static ImageMedia image(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return ImageMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/images/" + checksum
            );
        }

    }

}
