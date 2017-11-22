package org.activiti.cloud.services.identity.basic;

import org.activiti.engine.UserGroupLookupProxy;
import org.activiti.engine.UserRoleLookupProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasicUserRoleLookupProxy implements UserRoleLookupProxy {

    @Value("${admin-role-name:admin}")
    private String adminRoleName;

    private UserGroupLookupProxy userGroupLookupProxy;

    @Autowired
    public BasicUserRoleLookupProxy(UserGroupLookupProxy userGroupLookupProxy){
        this.userGroupLookupProxy = userGroupLookupProxy;
    }


    @Override
    public List<String> getRolesForUser(String s) {
        // NOT RECOMMENDED TO MIX GROUPS AND ROLES IN GENERAL - THIS IS JUST A LIMITATION OF THIS EXAMPLE
        return userGroupLookupProxy.getGroupsForCandidateUser(s);
    }

    @Override
    public boolean isAdmin(String userId){
        List<String> roles = getRolesForUser(userId);
        if(roles != null && roles.contains(adminRoleName)){
            return true;
        }
        return false;
    }

    public void setAdminRoleName(String adminRoleName) {
        this.adminRoleName = adminRoleName;
    }
}
