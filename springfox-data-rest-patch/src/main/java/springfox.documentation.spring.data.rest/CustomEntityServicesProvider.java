package springfox.documentation.spring.data.rest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.stereotype.Component;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.RequestHandlerProvider;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Custom im plementstion for RequestHandlerProvider
 */
@Component
public class CustomEntityServicesProvider implements RequestHandlerProvider {
    private final RepositoryRestConfiguration configuration;
    private final ResourceMappings mappings;
    private final Repositories repositories;
    private final TypeResolver typeResolver;
    private final PersistentEntities entities;
    private final Associations associations;
    private final RequestHandlerExtractorConfiguration extractorConfiguration;

    @Autowired
    public CustomEntityServicesProvider(
            RepositoryRestConfiguration configuration,
            ResourceMappings mappings,
            Repositories repositories,
            TypeResolver typeResolver,
            PersistentEntities entities,
            Associations associations,
            RequestHandlerExtractorConfiguration extractorConfiguration) {
        this.mappings = mappings;
        this.configuration = configuration;
        this.repositories = repositories;
        this.typeResolver = typeResolver;
        this.entities = entities;
        this.associations = associations;
        this.extractorConfiguration = extractorConfiguration;
    }

    @Override
    public List<RequestHandler> requestHandlers() {
        List<EntityContext> contexts = newArrayList();
        for (Class each : repositories) {
            RepositoryInformation repositoryInfo = repositories.getRepositoryInformationFor(each).get();
            Object repositoryInstance = repositories.getRepositoryFor(each);
            ResourceMetadata resource = mappings.getMetadataFor(each);
            if (resource.isExported()) {
                contexts.add(new CustomEntityContext(
                        typeResolver,
                        configuration,
                        repositoryInfo,
                        repositoryInstance,
                        resource,
                        mappings,
                        entities,
                        associations, extractorConfiguration));
            }

        }

        List<RequestHandler> handlers = new ArrayList<RequestHandler>();
        for (EntityContext each : contexts) {
            handlers.addAll(FluentIterable.from(extractorConfiguration.getEntityExtractors())
                                    .transformAndConcat(extractFromContext(each))
                                    .toList());
        }
        return handlers;
    }

    private Function<EntityOperationsExtractor, List<RequestHandler>> extractFromContext(final EntityContext context) {
        return new Function<EntityOperationsExtractor, List<RequestHandler>>() {
            @Override
            public List<RequestHandler> apply(EntityOperationsExtractor input) {
                return input.extract(context);
            }
        };
    }
}