package org.activiti.cloud.services.identity.basic;

import org.activiti.engine.UserGroupLookupProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class BasicUserRoleLookupProxyTest {

    private BasicUserRoleLookupProxy userRoleLookupProxy;

    @Mock
    private UserGroupLookupProxy userGroupLookupProxy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userRoleLookupProxy = new BasicUserRoleLookupProxy(userGroupLookupProxy);
        userRoleLookupProxy.setAdminRoleName("admin");
    }

    @Test
    public void testGetRoles() {

        List<String> roles = new ArrayList<>();
        roles.add("role");

        when(userGroupLookupProxy.getGroupsForCandidateUser("test"))
                .thenReturn(roles);

        assertThat(userRoleLookupProxy.getRolesForUser("test")).contains("role");
        assertThat(userRoleLookupProxy.isAdmin("test")).isFalse();
    }

    @Test
    public void testAdminRole()  {

        List<String> roles = new ArrayList<>();
        roles.add("admin");

        when(userGroupLookupProxy.getGroupsForCandidateUser("admin"))
                .thenReturn(roles);

        assertThat(userRoleLookupProxy.isAdmin("admin")).isTrue();
    }

}
