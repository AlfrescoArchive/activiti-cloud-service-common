package springfox.documentation.spring.data.rest;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

/**
 * Custom CustomSpringDataRestConfiguration
 *
 */
@Configuration
@Import(SpringDataRestConfiguration.class)
public class CustomSpringDataRestConfiguration implements BeanFactoryPostProcessor,
                                                          Ordered {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        for (String beanName : beanFactory.getBeanNamesForType(RequestHandlerProvider.class)) {
            if ("entityServicesProvider".equals(beanName)) {
                beanFactory.getBeanDefinition(beanName).setAutowireCandidate(false);
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}