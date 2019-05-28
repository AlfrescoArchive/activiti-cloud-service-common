package org.activiti.cloud.services.common.security.keycloak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.activiti.api.runtime.shared.security.SecurityManager;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class KeycloakSecurityManagerImpl implements SecurityManager {

    @Override
    public String getAuthenticatedUserId() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof KeycloakPrincipal) {
                KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) principal;
                return keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
            }
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
        return "";
    }
    
    @Override
    public List<String> getAuthenticatedUserGroups() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                       .map(SecurityContext::getAuthentication)
                       .map(Authentication::getCredentials)
                       .filter(KeycloakSecurityContext.class::isInstance)
                       .map(KeycloakSecurityContext.class::cast)
                       .map(KeycloakSecurityContext::getToken)
                       .map(AccessToken::getOtherClaims)
                       .map(it -> it.get("groups"))
                       .filter(Collection.class::isInstance)
                       .map(Collection.class::cast)
                       .map(it -> Collections.unmodifiableList(new ArrayList<String>(it)))
                       .orElse(Collections.emptyList());
    }    
}
