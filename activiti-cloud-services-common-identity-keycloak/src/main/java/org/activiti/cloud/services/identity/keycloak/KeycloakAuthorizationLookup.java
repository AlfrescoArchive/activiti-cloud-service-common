package org.activiti.cloud.services.identity.keycloak;

import java.util.ArrayList;
import java.util.List;

import org.activiti.runtime.api.auth.AuthorizationLookup;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakAuthorizationLookup implements AuthorizationLookup {

    @Value("${admin-role-name:admin}")
    private String adminRoleName;

    private KeycloakLookupService keycloakLookupService;

    @Autowired
    public KeycloakAuthorizationLookup(KeycloakLookupService keycloakLookupService) {
        this.keycloakLookupService = keycloakLookupService;
    }

    public List<String> getRolesForUser(String userName) {

        UserRepresentation user = keycloakLookupService.getUser(userName);

        List<RoleRepresentation> roleRepresentations = keycloakLookupService.getRolesForUser(user.getId());

        List<String> roles = null;
        if (roleRepresentations != null && roleRepresentations.size() > 0) {
            roles = new ArrayList<String>();
            for (RoleRepresentation roleRepresentation : roleRepresentations) {
                roles.add(roleRepresentation.getName());
            }
        }

        return roles;
    }

    public boolean isAdmin(String userId) {
        List<String> roles = getRolesForUser(userId);
        return (roles != null && roles.contains(adminRoleName));
    }

    public void setAdminRoleName(String adminRoleName) {
        this.adminRoleName = adminRoleName;
    }
}