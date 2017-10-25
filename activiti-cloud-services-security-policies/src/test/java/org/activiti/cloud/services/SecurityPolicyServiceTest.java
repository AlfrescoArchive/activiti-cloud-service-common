package org.activiti.cloud.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:propstest.properties")
public class SecurityPolicyServiceTest {

    @Autowired
    private SecurityPolicyService securityPolicyService;

    @Test
    public void shouldBePoliciesDefined() throws Exception {
        assertThat(securityPolicyService.policiesDefined()).isTrue();
    }


    @Test
    public void shouldGetProcessDefsByUserAndPolicies() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys("jEff",null, Arrays.asList(SecurityPolicy.write,SecurityPolicy.read));

        assertThat(keys).isNotNull();
        assertThat(keys).hasSize(1);
        assertThat(keys).contains("SimpleProcess");
    }

    @Test
    public void shouldGetProcessDefsByUserAndMinPolicy() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys("jEff",null, SecurityPolicy.read);

        assertThat(keys).isNotNull();
        assertThat(keys).hasSize(1);
        assertThat(keys).contains("SimpleProcess");
    }

    @Test
    public void shouldGetProcessDefsByGroupAndPolicies() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("finance"), Arrays.asList(SecurityPolicy.read));

        assertThat(keys).isNotNull();
        assertThat(keys).hasSize(2);
        assertThat(keys).contains("SimpleProcess1");
        assertThat(keys).contains("SimpleProcess2");
    }

    @Test
    public void shouldGetProcessDefsByGroupsAndMinPolicy() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("finance","nonexistent"), SecurityPolicy.read);

        assertThat(keys).isNotNull();
        assertThat(keys).hasSize(2);
        assertThat(keys).contains("SimpleProcess1");
        assertThat(keys).contains("SimpleProcess2");
    }

    @Test
    public void shouldNotGetProcessDefsForGroupWithoutDefs() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("hrbitlikerealgroupbutnot","nonexistent"), SecurityPolicy.read);

        assertThat(keys).isEmpty();
    }

    @Test
    public void shouldNotGetProcessDefsWithoutUserOrGroup() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,null, Arrays.asList(SecurityPolicy.write));

        assertThat(keys).isEmpty();
    }

    @Test
    public void shouldNotGetProcessDefsWithoutPolicyLevels() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("finance"), new HashSet<>());

        assertThat(keys).isEmpty();
    }

    @Test
    public void shouldNotGetProcessDefsWhenEntryMissingPolicyLevels() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys("fredslinehasanerror", null, SecurityPolicy.read);
        assertThat(keys).isEmpty();
    }

    @Test
    public void shouldNotGetProcessDefsWhenEntryMissingProcDefKeys() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys("jimhasnothing", null, SecurityPolicy.read);
        assertThat(keys).isEmpty();
    }
}
