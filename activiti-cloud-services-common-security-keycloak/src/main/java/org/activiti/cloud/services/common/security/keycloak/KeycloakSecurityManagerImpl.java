package org.activiti.cloud.services.common.security.keycloak;

import org.activiti.api.runtime.shared.security.SecurityManager;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class KeycloakSecurityManagerImpl implements SecurityManager {

    @Override
    public String getAuthenticatedUserId() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof KeycloakPrincipal) {
                KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) principal;
                return keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
            }
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
        return "";
    }
    
    @Override
    public Collection<String> getAuthenticatedUserGroups() {
        return getAuthenticatedUserAccessToken().map(AccessToken::getOtherClaims)
                                                .map(it -> it.get("groups"))
                                                .filter(Collection.class::isInstance)
                                                .map(Collection.class::cast)
                                                .map(it -> Collections.unmodifiableCollection(new ArrayList<String>(it)))
                                                .orElseThrow(() -> new SecurityException("Invalid groups claims in access token"));
    }

    @Override
    public Collection<String> getAuthenticatedUserRoles() {
        return getAuthenticatedUserAccessToken().map(AccessToken::getRealmAccess)
                                                .map(Access::getRoles)
                                                .map(it -> Collections.unmodifiableCollection(new ArrayList<String>(it)))
                                                .orElseThrow(() -> new SecurityException("Invalid realm access roles in access token"));
    }

    private Optional<AccessToken> getAuthenticatedUserAccessToken() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                       .map(SecurityContext::getAuthentication)
                       .map(Authentication::getCredentials)
                       .filter(KeycloakSecurityContext.class::isInstance)
                       .map(KeycloakSecurityContext.class::cast)
                       .map(KeycloakSecurityContext::getToken)
                       .filter(AccessToken::isActive);
    }
    
}
