package energy.usef.core.model;

import com.google.common.base.Objects;

public class ParticipantKey {
    String domain;
    String role;

    public ParticipantKey() {
    }

    public ParticipantKey(String domain, String role) {
        this.domain = domain;
        this.role = role;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ParticipantKey that = (ParticipantKey) o;
        return Objects.equal(domain, that.domain) &&
                Objects.equal(role, that.role);
    }

    @Override public int hashCode() {
        return Objects.hashCode(domain, role);
    }

    @Override public String toString() {
        return "ParticipantKey{" +
                "domain='" + domain + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
