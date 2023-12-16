package com.E3N.admin.catalogo.domain.castmember;

import com.E3N.admin.catalogo.domain.Identifier;
import com.E3N.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;

public class CastMemberID extends Identifier {

    private final String value;

    private CastMemberID(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }
    public static CastMemberID unique(){
        return CastMemberID.from(IdUtils.uuid());
    }

    public static CastMemberID from(final String id){
        return new CastMemberID(id.trim());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastMemberID that = (CastMemberID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
