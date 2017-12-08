package org.activiti.cloud.services.identity.keycloak;

import org.junit.Before;
import org.junit.Test;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class KeycloakLookupServiceTest {

    @InjectMocks
    private KeycloakLookupService keycloakLookupService;

    @Mock
    private KeycloakInstanceWrapper keycloakInstanceWrapper;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @Mock
    private UserResource userResource;

    @Mock
    private GroupRepresentation groupRepresentation;

    @Mock
    private RoleMappingResource roleMappingResource;

    @Mock
    private RoleScopeResource roleScopeResource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetGroupsForUser(){
        when(keycloakInstanceWrapper.getRealm()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(any())).thenReturn(userResource);
        when(userResource.groups()).thenReturn(Arrays.asList(groupRepresentation));
        assertThat(keycloakLookupService.getGroupsForUser("bob")).contains(groupRepresentation);
    }

    @Test
    public void testGetRolesForUser(){
        when(keycloakInstanceWrapper.getRealm()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(any())).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        when(roleScopeResource.listEffective()).thenReturn(new ArrayList<>());
        assertThat(keycloakLookupService.getRolesForUser("bob")).isNotNull();
    }

    @Test
    public void testGetUser(){
        when(keycloakInstanceWrapper.getRealm()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(any())).thenReturn(userResource);
        when(usersResource.search("bob",0,10)).thenReturn(new ArrayList<>());
        assertThat(keycloakLookupService.getUser("bob")).isNotNull();
    }
}
