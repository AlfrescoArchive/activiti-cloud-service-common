
package org.activiti.cloud.services.common.security.keycloak.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.activiti.api.runtime.shared.security.SecurityManager;
import org.activiti.cloud.services.common.security.keycloak.KeycloakSecurityManagerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class KeycloakSecurityManagerImplTest {
    
    @Autowired
    private SecurityManager securityManager;
    
    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class Application {
        
    }

    @Test
    public void contextLoads() {
        assertThat(securityManager).isInstanceOf(KeycloakSecurityManagerImpl.class);
    }
    
    @Test
    public void testGetAuthenticatedUserIdAnonymous() {
        // given
        
        // when
        Throwable thrown = catchThrowable(() -> { securityManager.getAuthenticatedUserId(); });
        
        // then
        assertThat(thrown).isInstanceOf(SecurityException.class);
    }

    @Test
    public void testGetAuthenticatedUserGroupsAnonymous() {
        // given
        
        // when
        Throwable thrown = catchThrowable(() -> { securityManager.getAuthenticatedUserGroups(); });
        
        // then
        assertThat(thrown).isInstanceOf(SecurityException.class);
    }

    @Test
    public void testGetAuthenticatedUserRolesAnonymous() {
        // given
        
        // when
        Throwable thrown = catchThrowable(() -> { securityManager.getAuthenticatedUserRoles(); });
        
        // then
        assertThat(thrown).isInstanceOf(SecurityException.class);
        
    }    
    

}
