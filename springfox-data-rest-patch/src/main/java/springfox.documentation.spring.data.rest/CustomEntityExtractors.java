package springfox.documentation.spring.data.rest;

import java.util.List;

import org.springframework.stereotype.Component;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Custom RequestHandlerExtractorConfiguration
 */
@Component
public class CustomEntityExtractors extends DefaultExtractorConfiguration {
    private final List<EntityOperationsExtractor> customEntityExtractors  = newArrayList(
            new CustomEntitySaveExtractor(),
            new CustomEntityDeleteExtractor(),
            new CustomEntityFindOneExtractor(),
            new CustomEntityFindAllExtractor(),
            new EntitySearchExtractor(),
            new EntityAssociationsExtractor()
    );

    @Override
    public List<EntityOperationsExtractor> getEntityExtractors() {
        return customEntityExtractors;
    }

}
