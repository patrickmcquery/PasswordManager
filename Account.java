public class Account {
    private String appName;
    private String username;
    private String password;
    private String notes = "";
    private boolean hidden = true;

    // constructor with notes
    public Account(String appName, String username, String password, String notes) {
        this.appName = appName;
        this.username = username;
        this.password = password;
        this.notes = notes;
    }

    // constructor no notes
    public Account(String appName, String username, String password) {
        this.appName = appName;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void unHide() {
        hidden = false;
    }

    public void hide() {
        hidden = true;
    }

    public String getString() {
        if (!hidden) {
            return "Website/Application Name: " + appName + "\nUsername: " + username
                    + "\nPassword: " + password + "\nNotes: " + notes;
        } else {
            return "Website/Application Name: " + appName + "\nUsername: " + username
                    + "\nPassword: " + "********" + "\nNotes: " + notes;
        }
    }

    public String getCSVString() {
        return appName + "," + username + "," + password + "," + notes;
    }
}
