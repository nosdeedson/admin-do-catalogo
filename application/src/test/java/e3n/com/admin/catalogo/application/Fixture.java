package e3n.com.admin.catalogo.application;

import com.github.javafaker.Faker;
import e3n.com.admin.catalogo.domain.castmember.CastMemberType;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name(){
        return FAKER.name().fullName();
    }

    public static  final class CastMembers {
        public static CastMemberType type(){
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.ACTRESS, CastMemberType.DIRECTOR);
        }
    }
}
