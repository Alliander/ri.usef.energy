package energy.usef.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.Objects;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "domain",
        "role",
        "version",
        "publicKeys",
        "url"
})
public class Participant{

    private static final String CS1_PREFIX = "cs1.";
    private static final int FIRST_KEY_INDEX = 0;
    private static final int SECOND_KEY_INDEX = 32;
    private static final int SECOND_KEY_SIZE = 32;

    @JsonProperty("domain")
    private String domain;
    @JsonProperty("role")
    private String role;
    @JsonProperty("version")
    private String version;
    @JsonProperty("publicKeys")
    private String publicKeys;
    @JsonProperty("url")
    private String url;

    private String unsealingKey;
    private String encryptionKey;

    @JsonProperty("domain")
    public String getDomain() {
        return domain;
    }

    @JsonProperty("domain")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("publicKeys")
    public String getPublicKeys() {
        return publicKeys;
    }

    @JsonProperty("publicKeys")
    public void setPublicKeys(String publicKeys) {
        this.publicKeys = publicKeys;

        String actualConcatenatedKey = publicKeys.substring(CS1_PREFIX.length());
        byte[] decodedKey = Base64.decodeBase64(actualConcatenatedKey);
        byte[] firstKey = Arrays.copyOfRange(decodedKey, FIRST_KEY_INDEX, SECOND_KEY_INDEX);
        this.unsealingKey = Base64.encodeBase64String(firstKey);
        if (decodedKey.length > firstKey.length) {
            this.encryptionKey = Base64.encodeBase64String(new byte[SECOND_KEY_SIZE]);
        }
    }

    public String getUnsealingKey() {
        return unsealingKey;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Participant that = (Participant) o;
        return Objects.equal(domain, that.domain) &&
                Objects.equal(role, that.role);
    }

    @Override public int hashCode() {
        return Objects.hashCode(domain, role);
    }

    @Override public String toString() {
        return "Participant{" +
                "domain='" + domain + '\'' +
                ", role='" + role + '\'' +
                ", version='" + version + '\'' +
                ", publicKeys='" + publicKeys + '\'' +
                ", unsealingKey='" + unsealingKey + '\'' +
                ", encryptionKey='" + encryptionKey + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
