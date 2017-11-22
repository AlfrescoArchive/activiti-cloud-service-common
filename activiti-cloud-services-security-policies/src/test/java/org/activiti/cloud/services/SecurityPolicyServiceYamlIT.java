package org.activiti.cloud.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityPolicyServiceYamlIT {

    @Autowired
    private SecurityPolicyService securityPolicyService;

    @Test
    public void shouldBePoliciesDefined() throws Exception {
        assertThat(securityPolicyService.policiesDefined()).isTrue();
    }


    @Test
    public void shouldGetProcessDefsByUserAndPolicies() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys("bOb",null, Arrays.asList(SecurityPolicy.WRITE,SecurityPolicy.READ));

        assertThat(keys).hasSize(1);
        assertThat(keys).contains("TestProcess");
    }


    @Test
    public void shouldGetProcessDefsByGroupAndPolicies() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("hr"), Arrays.asList(SecurityPolicy.READ));

        assertThat(keys).hasSize(2);
        assertThat(keys).contains("SimpleProcessYML1");
        assertThat(keys).contains("SimpleProcessYML2");
    }

}
