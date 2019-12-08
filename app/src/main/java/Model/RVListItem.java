package Model;

import android.app.LauncherActivity;

public class RVListItem extends LauncherActivity.ListItem {
    private String type;
    private String companyName;
    private String email;

    public RVListItem(String type, String companyName, String email) {
        this.type = type;
        this.companyName = companyName;
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RVListItem() {

    }

}