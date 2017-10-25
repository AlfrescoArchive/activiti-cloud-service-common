package org.activiti.conf;

import java.util.Map;

public class ActivitiCloudProperty {

    private Map<String, String> user;

    public Map<String, String> getUser() {
        return user;
    }

    public void setUser(Map<String, String> user) {
        this.user = user;
    }

    private Map<String, String> group;

    public Map<String, String> getGroup() {
        return group;
    }

    public void setGroup(Map<String, String> group) {
        this.group = group;
    }
}
