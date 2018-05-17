package org.activiti.cloud.services.security;

import org.activiti.cloud.services.security.conf.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "activiti.cloud.security.policies.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityPoliciesAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SecurityPoliciesService securityPoliciesService(SecurityProperties securityProperties){
        return new SecurityPoliciesService(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public BaseAuthenticationWrapper baseAuthenticationWrapper(){
        return new BaseAuthenticationWrapper();
    }
}
