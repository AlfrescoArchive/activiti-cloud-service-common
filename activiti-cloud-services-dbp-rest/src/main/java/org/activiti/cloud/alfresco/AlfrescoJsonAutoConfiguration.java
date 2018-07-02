package org.activiti.cloud.alfresco;

import org.activiti.cloud.alfresco.argument.resolver.AlfrescoPageArgumentMethodResolver;
import org.activiti.cloud.alfresco.argument.resolver.AlfrescoPageParameterParser;
import org.activiti.cloud.alfresco.config.AlfrescoWebConfigurer;
import org.activiti.cloud.alfresco.converter.json.AlfrescoJackson2HttpMessageConverter;
import org.activiti.cloud.alfresco.converter.json.PageMetadataConverter;
import org.activiti.cloud.alfresco.converter.json.PagedResourcesConverter;
import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.alfresco.data.domain.ExtendedPageMetadataConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.web.util.UriComponents;

@Configuration
@Import({AlfrescoWebConfigurer.class,})
public class AlfrescoJsonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    AlfrescoPageParameterParser alfrescoPageParameterParser(@Value("${spring.data.rest.default-page-size:100}") int defaultPageSize){
        return new AlfrescoPageParameterParser(defaultPageSize);
    }

    @Bean
    @ConditionalOnMissingBean
    public AlfrescoPageArgumentMethodResolver alfrescoPageArgumentMethodResolver(AlfrescoPageParameterParser pageParameterParser,
                                                                                 PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver){
        return new AlfrescoPageArgumentMethodResolver(pageParameterParser,pageableHandlerMethodArgumentResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public PageMetadataConverter pageMetadataConverter(){
        return new PageMetadataConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public PagedResourcesConverter pagedResourcesConverter(PageMetadataConverter pageMetadataConverter){
        return new PagedResourcesConverter(pageMetadataConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public AlfrescoJackson2HttpMessageConverter<?> alfrescoJackson2HttpMessageConverter(PagedResourcesConverter pagedResourcesConverter){
        return new AlfrescoJackson2HttpMessageConverter<>(pagedResourcesConverter);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public AlfrescoPagedResourcesAssembler<?> alfrescoPagedResourcesAssembler(@Nullable HateoasPageableHandlerMethodArgumentResolver resolver,
                                                                           @Nullable UriComponents baseUri,
                                                                           ExtendedPageMetadataConverter extendedPageMetadataConverter){
        return new AlfrescoPagedResourcesAssembler<>(resolver,baseUri,extendedPageMetadataConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExtendedPageMetadataConverter extendedPageMetadataConverter(){
        return new ExtendedPageMetadataConverter();
    }
}
