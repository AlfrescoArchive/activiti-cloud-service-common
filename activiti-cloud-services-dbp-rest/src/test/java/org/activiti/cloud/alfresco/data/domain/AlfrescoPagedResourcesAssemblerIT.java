/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.alfresco.data.domain;

import org.activiti.cloud.alfresco.config.AlfrescoApplicationIT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AlfrescoApplicationIT.class)
@EnableSpringDataWebSupport
@AutoConfigureMockMvc
@ComponentScan(basePackages = {"org.activiti.cloud.alfresco"})
public class AlfrescoPagedResourcesAssemblerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void testGetHalEmptyPage() throws Exception {
        mockMvc
                //when
                .perform(get("/mock/empty-list").accept(HAL_JSON_VALUE))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.strings",
                                    hasSize(0)));
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void testGetAlfrescoEmptyPage() throws Exception {
        mockMvc
                //when
                .perform(get("/mock/empty-list").accept(APPLICATION_JSON_VALUE))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.entries",
                                    hasSize(0)));
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void testGetHalSingletonPage() throws Exception {
        mockMvc
                //when
                .perform(get("/mock/singleton-list").accept(HAL_JSON_VALUE))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.strings",
                                    hasSize(1)));
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void testGetAlfrescoSingletonPage() throws Exception {
        mockMvc
                //when
                .perform(get("/mock/singleton-list").accept(APPLICATION_JSON_VALUE))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.entries",
                                    hasSize(1)));
    }
}
