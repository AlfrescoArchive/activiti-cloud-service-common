package springfox.documentation.spring.data.rest;

import java.util.HashSet;
import java.util.List;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ResolvedMethodParameter;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static springfox.documentation.spring.data.rest.RequestExtractionUtils.actionName;
import static springfox.documentation.spring.data.rest.RequestExtractionUtils.pathAnnotations;

/**
 * Custom EntityDeleteExtractor
 */
public class CustomEntityDeleteExtractor implements EntityOperationsExtractor {
    @Override
    public List<RequestHandler> extract(EntityContext context) {
        final List<RequestHandler> handlers = newArrayList();
        final PersistentEntity<?, ?> entity = context.entity();
        CrudMethods crudMethods = context.crudMethods();
        TypeResolver resolver = context.getTypeResolver();
        RepositoryMetadata repository = context.getRepositoryMetadata();
        if (crudMethods.hasDelete()) {
            HandlerMethod handler = new HandlerMethod(
                    context.getRepositoryInstance(),
                    crudMethods.getDeleteMethod().get());
            ActionSpecification spec = new ActionSpecification(
                    actionName(entity, crudMethods.getDeleteMethod().get()),
                    String.format("%s%s/{id}",
                                  context.basePath(),
                                  context.resourcePath()),
                    newHashSet(RequestMethod.DELETE),
                    new HashSet<MediaType>(),
                    new HashSet<MediaType>(),
                    handler,
                    newArrayList(new ResolvedMethodParameter(
                            0,
                            "id",
                            pathAnnotations("id", handler),
                            resolver.resolve(repository.getIdType()))),
                    resolver.resolve(Void.TYPE));
            handlers.add(new SpringDataRestRequestHandler(context, spec));
        }
        return handlers;
    }

}
