package org.activiti.cloud.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityPolicyServiceTest {

    @Autowired
    private SecurityPolicyService securityPolicyService;

    @Test
    public void shouldBePoliciesDefined() throws Exception {
        assertThat(securityPolicyService.policiesDefined()).isTrue();
    }


    @Test
    public void shouldGetProcessDefsByUserAndPoliciesCaseInsensitive() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys("bOb",null, Arrays.asList("wRiTe"));

        assertThat(keys).isNotNull();
        assertThat(keys).hasSize(1);
        assertThat(keys).contains("SimpleProcess");
    }

    @Test
    public void shouldGetProcessDefsByUserAndMinPolicyCaseInsensitive() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys("bOb",null, "rEaD");

        assertThat(keys).isNotNull();
        assertThat(keys).hasSize(1);
        assertThat(keys).contains("SimpleProcess");
    }

    @Test
    public void shouldGetProcessDefsByGroupAndPoliciesCaseInsensitive() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("hr"), Arrays.asList("rEaD"));

        assertThat(keys).isNotNull();
        assertThat(keys).hasSize(2);
        assertThat(keys).contains("SimpleProcess1");
        assertThat(keys).contains("SimpleProcess2");
    }

    @Test
    public void shouldGetProcessDefsByGroupsAndMinPolicyCaseInsensitive() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("hr","nonexistent"), "rEaD");

        assertThat(keys).isNotNull();
        assertThat(keys).hasSize(2);
        assertThat(keys).contains("SimpleProcess1");
        assertThat(keys).contains("SimpleProcess2");
    }

    @Test
    public void shouldNotGetProcessDefsForNonExistentPolicy() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("hr"), "nonsensepolicy");

        assertThat(keys).isEmpty();
    }

    @Test
    public void shouldNotGetProcessDefsForGroupWithoutDefs() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("hrbitlikerealgroupbutnot","nonexistent"), "rEaD");

        assertThat(keys).isEmpty();
    }

    @Test
    public void shouldNotGetPoliciesWithoutUserOrGroup() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,null, Arrays.asList("write"));

        assertThat(keys).isEmpty();
    }

    @Test
    public void shouldNotGetPoliciesWithoutPolicyLevels() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys(null,Arrays.asList("hr"), new HashSet<>());

        assertThat(keys).isEmpty();

        keys = securityPolicyService.getProcessDefinitionKeys("bob", null, "");
        assertThat(keys).isEmpty();
    }

    @Test
    public void shouldNotGetPoliciesWhenEntryMissingPolicyLevels() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys("fredslinehasanerror", null, "");
        assertThat(keys).isEmpty();
    }

    @Test
    public void shouldNotGetPoliciesWhenEntryMissingProcDefKeys() throws Exception {

        Collection<String> keys = securityPolicyService.getProcessDefinitionKeys("jimhasnothing", null, "");
        assertThat(keys).isEmpty();
    }
}
