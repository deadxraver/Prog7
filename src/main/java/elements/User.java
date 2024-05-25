package elements;

import java.util.Objects;

public class User {
    public User(
            String username,
            String password
    ) {
        this.username = username;
        this.password = password;
    }
    private long id;
    private final String username;
    private final String password;
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

    public String getPassword() {
        return password;
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
        return id == user.id && superuser == user.superuser && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, superuser);
    }

    @Override
    public String toString() {
        return "user_id=" + id +
                ", username=" + username +
                ", is_a_superuser=" + superuser;
    }
}
