package springfox.documentation.spring.data.rest;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.mapping.Associations;

/**
 * Custom EntityContext
 */
public class CustomEntityContext extends EntityContext {

    private final ResourceMetadata resource;

    private final PersistentEntities entities;

    public CustomEntityContext(TypeResolver typeResolver,
                               RepositoryRestConfiguration configuration,
                               RepositoryInformation repository,
                               Object repositoryInstance,
                               ResourceMetadata resource,
                               ResourceMappings mappings,
                               PersistentEntities entities,
                               Associations associations,
                               RequestHandlerExtractorConfiguration extractorConfiguration) {
        super(typeResolver,
              configuration,
              repository,
              repositoryInstance,
              resource,
              mappings,
              entities,
              associations,
              extractorConfiguration);
        this.entities = entities;
        this.resource = resource;
    }

    @Override
    public PersistentEntity<?, ?> entity() {
        return entities.getPersistentEntity(resource.getDomainType()).get();
    }
}
