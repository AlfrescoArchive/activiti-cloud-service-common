package org.activiti.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "activiti")
@Component
public class ActivitiProperty  {

    private ActivitiCloudProperty cloud = new ActivitiCloudProperty();

    public ActivitiCloudProperty getCloud() {
        return cloud;
    }

    public void setCloud(ActivitiCloudProperty cloud) {
        this.cloud = cloud;
    }

}
