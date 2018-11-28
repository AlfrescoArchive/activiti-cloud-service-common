//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.keycloak.adapters.springsecurity.config;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springsecurity.AdapterDeploymentContextFactoryBean;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakCsrfRequestMatcher;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

public abstract class KeycloakWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity> {
    @Value("${keycloak.configurationFile:WEB-INF/keycloak.json}")
    private Resource keycloakConfigFileResource;
    @Autowired(
        required = false
    )
    private KeycloakConfigResolver keycloakConfigResolver;

    public KeycloakWebSecurityConfigurerAdapter() {
    }

    @Bean
    protected AdapterDeploymentContext adapterDeploymentContext() throws Exception {
        AdapterDeploymentContextFactoryBean factoryBean;
        if(this.keycloakConfigResolver != null) {
            factoryBean = new AdapterDeploymentContextFactoryBean(new KeycloakSpringConfigResolverWrapper(this.keycloakConfigResolver));
        } else {
            factoryBean = new AdapterDeploymentContextFactoryBean(this.keycloakConfigFileResource);
        }

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    protected AuthenticationEntryPoint authenticationEntryPoint() throws Exception {
        return new KeycloakAuthenticationEntryPoint(this.adapterDeploymentContext());
    }

    protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        return new KeycloakAuthenticationProvider();
    }

    @Bean
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(this.authenticationManagerBean());
        filter.setSessionAuthenticationStrategy(this.sessionAuthenticationStrategy());
        return filter;
    }

    @Bean
    protected KeycloakPreAuthActionsFilter keycloakPreAuthActionsFilter() {
        return new KeycloakPreAuthActionsFilter(this.httpSessionManager());
    }

    protected KeycloakCsrfRequestMatcher keycloakCsrfRequestMatcher() {
        return new KeycloakCsrfRequestMatcher();
    }

    @Bean
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    protected KeycloakLogoutHandler keycloakLogoutHandler() throws Exception {
        return new KeycloakLogoutHandler(this.adapterDeploymentContext());
    }

    protected abstract SessionAuthenticationStrategy sessionAuthenticationStrategy();

    protected void configure(HttpSecurity http) throws Exception {
        ((HttpSecurity)((HttpSecurity)((HttpSecurity)http.csrf().requireCsrfProtectionMatcher(this.keycloakCsrfRequestMatcher()).and()).sessionManagement().sessionAuthenticationStrategy(this.sessionAuthenticationStrategy()).and()).addFilterBefore(this.keycloakPreAuthActionsFilter(), LogoutFilter.class).addFilterBefore(this.keycloakAuthenticationProcessingFilter(), BasicAuthenticationFilter.class).addFilterAfter(this.keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class).addFilterAfter(this.keycloakAuthenticatedActionsRequestFilter(), KeycloakSecurityContextRequestFilter.class).exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint()).and()).logout().addLogoutHandler(this.keycloakLogoutHandler()).logoutUrl("/sso/logout").permitAll().logoutSuccessUrl("/");
    }

    @Bean
    protected KeycloakSecurityContextRequestFilter keycloakSecurityContextRequestFilter() {
        return new KeycloakSecurityContextRequestFilter();
    }

    @Bean
    protected KeycloakAuthenticatedActionsFilter keycloakAuthenticatedActionsRequestFilter() {
        return new KeycloakAuthenticatedActionsFilter();
    }
}
