package org.activiti.cloud.services.identity.keycloak;

import org.activiti.engine.UserRoleLookupProxy;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeycloakUserRoleLookupProxy implements UserRoleLookupProxy {

    @Value("${keycloak.auth-server-url}")
    private String authServer;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloakadminclientapp}")
    private String keycloakadminclientapp;

    @Value("${keycloakclientuser}")
    private String clientUser;

    @Value("${keycloakclientpassword}")
    private String clientPassword;

    @Value("${adminrolename:admin}")
    private String adminRoleName;

    public List<String> getRolesForUser(String userName) {

        Keycloak keycloak = Keycloak.getInstance(authServer,
                realm,
                clientUser,
                clientPassword,
                keycloakadminclientapp);


        List<UserRepresentation> users = keycloak.realms().realm(realm).users().search(userName,
                0,
                10);
        if (users.size() > 1) {
            throw new UnsupportedOperationException("User id " + userName + " is not unique");
        }
        UserRepresentation user = users.get(0);

        List<RoleRepresentation> roleRepresentations = keycloak.realms().realm(realm).users().get(user.getId()).roles().realmLevel().listEffective();

        List<String> roles = null;
        if (roleRepresentations != null && roleRepresentations.size() > 0) {
            roles = new ArrayList<String>();
            for (RoleRepresentation roleRepresentation : roleRepresentations) {
                roles.add(roleRepresentation.getName());
            }
        }

        return roles;
    }

    public boolean isAdmin(String userId){
        List<String> roles = getRolesForUser(userId);
        if(roles != null && roles.contains(adminRoleName)){
            return true;
        }
        return false;
    }
}
