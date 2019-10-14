package org.activiti.cloud.services.common.security.keycloak.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.cloud.services.common.security.keycloak.config.CommonSecurityAutoConfiguration;
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
public class CommonSecurityAutoConfigurationTest {
    
    @Autowired(required = false)
    private CommonSecurityAutoConfiguration commonSecurityAutoConfiguration;
    
    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class Application {
        
    }

    @Test
    public void contextLoads() {
        assertThat(commonSecurityAutoConfiguration).isNotNull();
    }

}
