package org.activiti.cloud.services.identity.keycloak;

import org.activiti.engine.UserGroupLookupProxy;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.assertj.core.api.Assertions.assertThat;

public class KeycloakUserGroupLookupProxyTest {

    private UserGroupLookupProxy userGroupLookupProxy;

    @Mock
    private KeycloakLookupService keycloakLookupService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userGroupLookupProxy = new KeycloakUserGroupLookupProxy(keycloakLookupService);
        List<UserRepresentation> users = new ArrayList<>();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("id");
        users.add(userRepresentation);
        List<GroupRepresentation> groupRepresentations = new ArrayList<>();
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName("testgroup");
        groupRepresentations.add(groupRepresentation);


        when(keycloakLookupService.getUser(anyString())).thenReturn(users);
        when(keycloakLookupService.getGroupsForUser(anyString())).thenReturn(groupRepresentations);

    }

    @Test
    public void testGetGroups() {
        assertThat(userGroupLookupProxy.getGroupsForCandidateUser("bob")).contains("testgroup");
    }

    @Test
    public void testMustBeUniqueUser() {

        List<UserRepresentation> users = new ArrayList<>();
        UserRepresentation userRepresentation1 = new UserRepresentation();
        userRepresentation1.setId("id1");
        users.add(userRepresentation1);

        UserRepresentation userRepresentation2 = new UserRepresentation();
        userRepresentation2.setId("id2");
        users.add(userRepresentation2);

        when(keycloakLookupService.getUser(anyString())).thenReturn(users);

        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> userGroupLookupProxy.getGroupsForCandidateUser("fred"));

    }
}