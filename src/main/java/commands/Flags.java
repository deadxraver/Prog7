package commands;

public enum Flags {
    FOR_ALL_USERS,
    FOR_CURRENT_USER,
    FOR_PARTICULAR_USER;
    public static Flags getByString(String s) {
        return s.equals("-a") ? FOR_ALL_USERS
                : s.equals("-m") ? FOR_CURRENT_USER
                : s.equals("-u") ? FOR_PARTICULAR_USER
                : null;
    }
}
