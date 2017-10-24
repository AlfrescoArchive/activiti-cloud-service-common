package org.activiti.cloud.services;

import org.activiti.conf.SecurityProperties;
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
    private SecurityProperties securityProperties;

    public boolean policiesDefined(){
        return ((securityProperties.getGroup()!=null && !securityProperties.getGroup().isEmpty()) || (securityProperties.getUser()!=null && !securityProperties.getUser().isEmpty()));
    }


    public Set<String> getProcessDefinitionKeys(String userId, Collection<String> groups, Collection<String> policyLevels){


        Set<String> procDefKeys = new HashSet<String>();

        if(groups != null) {
            for (String group : groups) {
                getProcDefKeysForUserOrGroup(policyLevels, procDefKeys, group, securityProperties.getGroup());
            }
        }

        getProcDefKeysForUserOrGroup(policyLevels, procDefKeys, userId, securityProperties.getUser());

        return procDefKeys;

    }

    private void getProcDefKeysForUserOrGroup(Collection<String> policyLevels, Set<String> procDefKeys, String userOrGroup, Map<String, String> policies) {

        if(userOrGroup == null){
            return;
        }

        // iterate through the properties either by user or group (already pre-filtered)

        for(String key: policies.keySet()){

            //filter to this specific user or group

            if(key!=null && key.toLowerCase().contains(userOrGroup.toLowerCase()+".")){

                //filter for the policy level we're looking for

                for(String policyLevel:policyLevels){

                    if(policyLevel !=null && !policyLevel.isEmpty() && key.toLowerCase().contains("."+policyLevel.toLowerCase())){

                        String propertyValue = policies.get(key);

                        // get the process definition key or keys for this property line

                        if(propertyValue!=null && propertyValue.contains(",")){

                            procDefKeys.addAll(Arrays.asList(propertyValue.split(",")));

                        } else if(propertyValue!=null){

                            procDefKeys.add(propertyValue);
                        }
                    }
                }
            }
        }
    }

    public Set<String> getProcessDefinitionKeys(String userId, Collection<String> groups, String minPolicyLevel){
        if (minPolicyLevel != null && minPolicyLevel.toLowerCase().equalsIgnoreCase("read")){
            return getProcessDefinitionKeys(userId,groups,Arrays.asList("read","write"));
        }
        return getProcessDefinitionKeys(userId,groups,Arrays.asList(minPolicyLevel));
    }

}