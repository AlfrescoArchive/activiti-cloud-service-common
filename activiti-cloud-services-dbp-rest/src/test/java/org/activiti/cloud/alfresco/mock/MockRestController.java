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

package org.activiti.cloud.alfresco.mock;

import java.util.List;

import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Mock rest controller
 */
@RestController
@RequestMapping(
        value = "/mock",
        produces = {
                HAL_JSON_VALUE,
                APPLICATION_JSON_VALUE
        }
)
public class MockRestController {

    @Autowired
    private AlfrescoPagedResourcesAssembler<String> pagedResourcesAssembler;

    @Autowired
    private MockRestResourceAssembler resourceAssembler;

    @RequestMapping(method = GET, path = "/empty-list")
    public PagedResources<Resource<String>> getEmptyPage(Pageable pageable) {
        return pagedResourcesAssembler.toResource(pageable,
                                                  createPage(emptyList(),
                                                             pageable),
                                                  String.class,
                                                  resourceAssembler);
    }

    @RequestMapping(method = GET, path = "/singleton-list")
    public PagedResources<Resource<String>> getSingletonPage(Pageable pageable) {
        return pagedResourcesAssembler.toResource(pageable,
                                                  createPage(singletonList("any"),
                                                             pageable),
                                                  String.class,
                                                  resourceAssembler);
    }

    private Page<String> createPage(List<String> content,
                                    Pageable pageable) {
        return PageableExecutionUtils
                .getPage(content,
                         pageable,
                         () -> 100);
    }
}
