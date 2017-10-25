package org.activiti.cloud.services;

import org.activiti.conf.ActivitiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class SecurityPolicyService {

    @Autowired
    private ActivitiProperty securityProperties;

    public boolean policiesDefined(){
        return ((securityProperties.getCloud().getGroup()!=null && !securityProperties.getCloud().getGroup().isEmpty()) || (securityProperties.getCloud().getUser()!=null && !securityProperties.getCloud().getUser().isEmpty()));
    }


    public Set<String> getProcessDefinitionKeys(String userId, Collection<String> groups, Collection<SecurityPolicy> policyLevels){


        Set<String> procDefKeys = new HashSet<String>();

        if(groups != null) {
            for (String group : groups) {
                getProcDefKeysForUserOrGroup(policyLevels, procDefKeys, group, securityProperties.getCloud().getGroup());
            }
        }

        getProcDefKeysForUserOrGroup(policyLevels, procDefKeys, userId, securityProperties.getCloud().getUser());

        return procDefKeys;

    }

    private void getProcDefKeysForUserOrGroup(Collection<SecurityPolicy> policyLevels, Set<String> procDefKeys, String userOrGroup, Map<String, String> policies) {

        if(userOrGroup == null || policies == null){
            return;
        }

        // iterate through the properties either by user or group (already pre-filtered)

        for(String key: policies.keySet()){

            //filter to this specific user or group

            if(key!=null && key.toLowerCase().contains(userOrGroup.toLowerCase()+".")){

                //filter for the policy level we're looking for

                for(SecurityPolicy policyLevel:policyLevels){

                    if(policyLevel !=null && key.toLowerCase().contains("."+policyLevel.name())){

                        String propertyValue = policies.get(key);

                        // get the process definition key or keys for this property line

                        if(propertyValue!=null && propertyValue.contains(",")){

                            procDefKeys.addAll(Arrays.asList(propertyValue.split(",")));

                        } else if(propertyValue!=null && !propertyValue.isEmpty()){

                            procDefKeys.add(propertyValue);
                        }
                    }
                }
            }
        }
    }

    public Set<String> getProcessDefinitionKeys(String userId, Collection<String> groups, SecurityPolicy minPolicyLevel){
        if (minPolicyLevel != null && minPolicyLevel.equals(SecurityPolicy.read)){
            return getProcessDefinitionKeys(userId,groups,Arrays.asList(SecurityPolicy.read,SecurityPolicy.write));
        }
        return getProcessDefinitionKeys(userId,groups,Arrays.asList(minPolicyLevel));
    }

}