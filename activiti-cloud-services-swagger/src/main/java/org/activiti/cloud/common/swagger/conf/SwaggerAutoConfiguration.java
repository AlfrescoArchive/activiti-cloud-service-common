/*
 * Copyright 2019 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.common.swagger.conf;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.fasterxml.classmate.TypeResolver;
import org.activiti.cloud.common.swagger.DocketCustomizer;
import org.activiti.cloud.common.swagger.SwaggerDocketBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.keycloak.OAuth2Constants.CLIENT_SECRET;

/**
 * Provides base springfox configuration for swagger auto-generated specification file. It provides two
 * swagger specification files: the default one is available under `v2/api-docs` or `v2/api-docs?group=default`
 * and provides specification for Alfresco MediaType format; the HAL one is available under `v2/api-docs?group=hal`
 * and provides specification for HAL format
 *
 * This configuration is not self-contained: the one adding this as dependency should provide a bean of type
 * {@link Predicate<RequestHandler>} that will be injected under {@link Docket#select()}. I.e
 * <code>test</code>
 * {@code test}
 * <pre>
 *     &#64;Bean
 *     public Predicate&#60;RequestHandler&#62; apiSelector() {
 *         return RequestHandlerSelectors.basePackage("org.activiti.cloud.services")::apply;
 *     }
 *  </pre>
 */
@Configuration
@EnableSwagger2
public class SwaggerAutoConfiguration {

    @Value("${keycloak.auth-server-url}")
    private String AUTH_SERVER;

    @Value("${keycloak.credentials.secret}")
    private String CLIENT_SECRET;

    @Value("${keycloak.resource}")
    private String CLIENT_ID;

    @Value("${keycloak.realm}")
    private String REALM;

    private static final String OAUTH_NAME = "spring_oauth";
    private static final String ALLOWED_PATHS = "/v1/*";
    private static final String GROUP_NAME = "default";

    @Bean
    @ConditionalOnMissingBean
    public SwaggerDocketBuilder swaggerDocketBuilder(Predicate<RequestHandler> apiSelector,
                                                     TypeResolver typeResolver,
                                                     @Autowired(required = false) List<DocketCustomizer> docketCustomizers,
                                                     @Autowired(required = false) ApiInfo apiInfo) {
        return new SwaggerDocketBuilder(apiSelector,
                                        typeResolver,
                                        docketCustomizers,
                                        apiInfo);
    }

    @Bean(name = "halAPIDocket")
    @ConditionalOnMissingBean(name = "halAPIDocket")
    public Docket halAPIDocket(SwaggerDocketBuilder swaggerDocketBuilder) {
        return swaggerDocketBuilder.buildHalAPIDocket(securityScheme(), securityContext());
    }

    @Bean(name = "alfrescoAPIDocket")
    @ConditionalOnMissingBean(name = "alfrescoAPIDocket")
    public Docket alfrescoAPIDocket(SwaggerDocketBuilder swaggerDocketBuilder) {
        return swaggerDocketBuilder.buildAlfrescoAPIDocket(securityScheme(), securityContext());
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .realm(REALM)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .appName(GROUP_NAME)
                .scopeSeparator(" ")
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType =
                new AuthorizationCodeGrantBuilder()
                        .tokenEndpoint(new TokenEndpoint(AUTH_SERVER + "/realms/" + REALM + "/protocol/openid-connect/token", GROUP_NAME))
                        .tokenRequestEndpoint(
                                new TokenRequestEndpoint(AUTH_SERVER + "/realms/" + REALM + "/protocol/openid-connect/auth", CLIENT_ID, CLIENT_SECRET))
                        .build();

        SecurityScheme oauth =
                new OAuthBuilder()
                        .name(OAUTH_NAME)
                        .grantTypes(Arrays.asList(grantType))
                        .scopes(Arrays.asList(scopes()))
                        .build();
        return oauth;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("user", "for CRUD operations"),
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations")
        };
        return scopes;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference(OAUTH_NAME, scopes())))
                .forPaths(PathSelectors.regex(ALLOWED_PATHS))
                .build();
    }

}
