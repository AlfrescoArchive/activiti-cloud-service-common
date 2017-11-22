package org.activiti.cloud.services.identity.basic;

import org.activiti.engine.UserRoleLookupProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicUserRoleLookupProxyIT {

    @Autowired
    private UserRoleLookupProxy userRoleLookupProxy;

    @Test
    public void testAdminRole() throws Exception {
        assertThat(userRoleLookupProxy.isAdmin("client")).isTrue();
        assertThat(userRoleLookupProxy.isAdmin("testuser")).isFalse();
    }
}
