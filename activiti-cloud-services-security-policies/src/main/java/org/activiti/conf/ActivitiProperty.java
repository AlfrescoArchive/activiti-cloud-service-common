package org.activiti.conf;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "activiti")
@Component
@RefreshScope
public class ActivitiProperty implements InitializingBean {

    private ActivitiCloudProperty cloud = new ActivitiCloudProperty();

    public ActivitiCloudProperty getCloud() {
        return cloud;
    }

    public void setCloud(ActivitiCloudProperty cloud) {
        this.cloud = cloud;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // do nothing
    }

}
