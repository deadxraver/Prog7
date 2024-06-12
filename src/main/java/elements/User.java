package elements;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    public User(
            long id,
            String username,
            String hashedPassword,
            boolean superuser
    ) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.id = id;
        this.superuser = superuser;
    }
    private long id;
    private final String username;
    private final String hashedPassword;
    private boolean superuser;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && superuser == user.superuser && Objects.equals(username, user.username) && Objects.equals(hashedPassword, user.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, hashedPassword, superuser);
    }

    @Override
    public String toString() {
        return "user_id=" + id +
                ", username=" + username +
                ", is_a_superuser=" + superuser;
    }
}
