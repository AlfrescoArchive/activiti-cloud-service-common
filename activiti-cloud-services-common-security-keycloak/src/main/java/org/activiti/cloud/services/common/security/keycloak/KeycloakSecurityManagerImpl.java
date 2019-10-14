package org.activiti.cloud.services.common.security.keycloak;

import org.activiti.api.runtime.shared.security.AbstractSecurityManager;
import org.activiti.api.runtime.shared.security.PrincipalDetailsProvider;
import org.activiti.api.runtime.shared.security.PrincipalIdentityProvider;
import org.activiti.api.runtime.shared.security.SecurityContextPrincipalProvider;
import org.springframework.lang.NonNull;

public class KeycloakSecurityManagerImpl extends AbstractSecurityManager {
    
    public KeycloakSecurityManagerImpl(@NonNull SecurityContextPrincipalProvider securityContextPrincipalProvider,
                                       @NonNull PrincipalIdentityProvider principalIdentityProvider,
                                       @NonNull PrincipalDetailsProvider principalDetailsProvider) {
        super(securityContextPrincipalProvider, 
              principalIdentityProvider, 
              principalDetailsProvider);
    }


}
