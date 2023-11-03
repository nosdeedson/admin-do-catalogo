package e3n.com.admin.catalogo.domain.castmember;

import e3n.com.admin.catalogo.domain.AggregateRoot;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.domain.utils.InstantUtils;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    public CastMember(CastMemberID castMemberID, String name, CastMemberType type, Instant createdAt, Instant updatedAt) {
        super(castMemberID);
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        selfValidate();
    }

    public static CastMember newMember(final String name, final CastMemberType type){
        final var id = CastMemberID.unique();
        final var now = InstantUtils.now();
        return new CastMember(id, name, type, now,now);
    }

    public static CastMember whith(final CastMemberID id, final String name, final CastMemberType type, final Instant createdAt, final Instant updatedAt){
        return new CastMember(id, name, type, createdAt, updatedAt);
    }

    public static CastMember with(final CastMember member){
        return new CastMember(member.id, member.name, member.type, member.createdAt, member.updatedAt);
    }

    public CastMember update(final String name, final CastMemberType type){
        this.name = name;
        this.type = type;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }
    @Override
    public void validate(ValidationHandler handler) {
        new CastMemberValidator(handler, this).validate();
    }

    private void selfValidate(){
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasError()){
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CastMemberType getType() {
        return type;
    }

    public void setType(CastMemberType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
