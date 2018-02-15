package springfox.documentation.spring.data.rest;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ResolvedMethodParameter;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.data.rest.webmvc.RestMediaTypes.HAL_JSON;
import static org.springframework.data.rest.webmvc.RestMediaTypes.SPRING_DATA_COMPACT_JSON;
import static org.springframework.data.rest.webmvc.RestMediaTypes.TEXT_URI_LIST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static springfox.documentation.spring.data.rest.RequestExtractionUtils.actionName;

/**
 * Custom EntityFindAllExtractor
 */
public class CustomEntityFindAllExtractor implements EntityOperationsExtractor {
    @Override
    public List<RequestHandler> extract(EntityContext context) {
        final List<RequestHandler> handlers = newArrayList();
        final PersistentEntity<?, ?> entity = context.entity();
        CrudMethods crudMethods = context.crudMethods();
        TypeResolver resolver = context.getTypeResolver();
        RepositoryMetadata repository = context.getRepositoryMetadata();
        if (crudMethods.hasFindAllMethod()) {
            HandlerMethod handler = new HandlerMethod(
                    context.getRepositoryInstance(),
                    crudMethods.getFindAllMethod().get());
            ActionSpecification spec = new ActionSpecification(
                    actionName(entity, crudMethods.getFindAllMethod().get()),
                    String.format("%s%s",
                                  context.basePath(),
                                  context.resourcePath()),
                    newHashSet(RequestMethod.GET),
                    newHashSet(
                            APPLICATION_JSON,
                            HAL_JSON ,
                            SPRING_DATA_COMPACT_JSON,
                            TEXT_URI_LIST),
                    new HashSet<MediaType>(),
                    handler,
                    findAllParameters(context.getConfiguration(), context.getTypeResolver()),
                    resolver.resolve(Resources.class, repository.getReturnedDomainClass(handler.getMethod())));
            handlers.add(new SpringDataRestRequestHandler(context, spec));
        }
        return handlers;
    }

    private ArrayList<ResolvedMethodParameter> findAllParameters(
            RepositoryRestConfiguration configuration,
            TypeResolver resolver) {
        ArrayList<ResolvedMethodParameter> parameters = new ArrayList<ResolvedMethodParameter>();
        parameters.add(new ResolvedMethodParameter(
                0,
                configuration.getPageParamName(),
                Lists.<Annotation>newArrayList(),
                resolver.resolve(String.class)));
        parameters.add(new ResolvedMethodParameter(
                1,
                configuration.getLimitParamName(),
                Lists.<Annotation>newArrayList(),
                resolver.resolve(String.class)));
        parameters.add(new ResolvedMethodParameter(
                2,
                configuration.getSortParamName(),
                Lists.<Annotation>newArrayList(),
                resolver.resolve(String.class)));
        return parameters;
    }

}
