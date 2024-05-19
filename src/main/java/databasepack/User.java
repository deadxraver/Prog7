package databasepack;

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
}
