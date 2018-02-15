package springfox.documentation.spring.data.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ResolvedMethodParameter;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static springfox.documentation.spring.data.rest.RequestExtractionUtils.actionName;
import static springfox.documentation.spring.data.rest.RequestExtractionUtils.bodyAnnotations;
import static springfox.documentation.spring.data.rest.RequestExtractionUtils.pathAnnotations;

/**
 * Custom EntitySaveExtractor
 */
public class CustomEntitySaveExtractor implements EntityOperationsExtractor {

    @Override
    public List<RequestHandler> extract(EntityContext context) {
        final List<RequestHandler> handlers = newArrayList();
        final PersistentEntity<?, ?> entity = context.entity();
        CrudMethods crudMethods = context.crudMethods();
        if (crudMethods.hasSaveMethod()) {
            HandlerMethod handler = new HandlerMethod(
                    context.getRepositoryInstance(),
                    crudMethods.getSaveMethod().get());
            RepositoryMetadata resource = context.getRepositoryMetadata();
            ActionSpecification put = saveActionSpecification(
                    entity,
                    newHashSet(PUT, PATCH),
                    String.format("%s%s/{id}",
                                  context.basePath(),
                                  context.resourcePath()),
                    handler,
                    context.getTypeResolver(),
                    resource, newArrayList(
                            new ResolvedMethodParameter(
                                    0,
                                    "id",
                                    pathAnnotations("id", handler),
                                    context.getTypeResolver().resolve(resource.getIdType())),
                            new ResolvedMethodParameter(
                                    0,
                                    "body",
                                    bodyAnnotations(handler),
                                    context.getTypeResolver().resolve(resource.getDomainType()))));
            handlers.add(new SpringDataRestRequestHandler(context, put));
            ActionSpecification post = saveActionSpecification(
                    entity,
                    newHashSet(POST),
                    String.format("%s%s", context.basePath(), context.resourcePath()),
                    handler,
                    context.getTypeResolver(), resource, newArrayList(
                            new ResolvedMethodParameter(
                                    0,
                                    "body",
                                    bodyAnnotations(handler),
                                    context.getTypeResolver().resolve(resource.getDomainType()))));
            handlers.add(new SpringDataRestRequestHandler(context, post));
        }
        return handlers;
    }

    private ActionSpecification saveActionSpecification(
            PersistentEntity<?, ?> entity,
            Set<RequestMethod> methods,
            String path,
            HandlerMethod handler,
            TypeResolver typeResolver,
            RepositoryMetadata repository,
            List<ResolvedMethodParameter> parameters) {

        return new ActionSpecification(
                actionName(entity, handler.getMethod()),
                path,
                methods,
                new HashSet<MediaType>(),
                new HashSet<MediaType>(),
                handler,
                parameters,
                typeResolver.resolve(Resource.class, repository.getReturnedDomainClass(handler.getMethod())));
    }
}
