/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.plugin.use.internal;

import org.gradle.api.Action;
import org.gradle.api.Namer;
import org.gradle.api.internal.DefaultNamedDomainObjectList;
import org.gradle.api.internal.artifacts.DependencyResolutionServices;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.VersionSelectorScheme;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.plugins.dsl.PluginRepositoryHandler;
import org.gradle.api.internal.plugins.repositories.MavenPluginRepository;
import org.gradle.api.internal.plugins.repositories.PluginRepository;
import org.gradle.internal.Factory;
import org.gradle.internal.reflect.Instantiator;

class DefaultPluginRepositoryHandler extends DefaultNamedDomainObjectList<PluginRepository> implements PluginRepositoryHandler {

    private FileResolver fileResolver;
    private Factory<DependencyResolutionServices> dependencyResolutionServicesFactory;
    private VersionSelectorScheme versionSelectorScheme;

    public DefaultPluginRepositoryHandler(FileResolver fileResolver, Factory<DependencyResolutionServices> dependencyResolutionServicesFactory, VersionSelectorScheme versionSelectorScheme, Instantiator instantiator) {
        super(PluginRepository.class, instantiator, new RepositoryNamer());
        this.fileResolver = fileResolver;
        this.dependencyResolutionServicesFactory = dependencyResolutionServicesFactory;
        this.versionSelectorScheme = versionSelectorScheme;
    }

    private static class RepositoryNamer implements Namer<PluginRepository> {
        @Override
        public String determineName(PluginRepository repository) {
            return repository.getName();
        }
    }

    @Override
    public MavenPluginRepository maven(Action<? super MavenPluginRepository> configurationAction) {
        DefaultMavenPluginRepository mavenPluginRepository = getInstantiator()
            .newInstance(DefaultMavenPluginRepository.class, fileResolver, dependencyResolutionServicesFactory.create(), versionSelectorScheme);
        configurationAction.execute(mavenPluginRepository);
        uniquifyName(mavenPluginRepository);
        add(mavenPluginRepository);
        return mavenPluginRepository;
    }

    private void uniquifyName(MavenPluginRepository mavenPluginRepository) {
        String name = mavenPluginRepository.getName();
        if (name == null) {
            name = "maven";
        }
        name = uniquifyName(name);
        mavenPluginRepository.setName(name);
    }

    private String uniquifyName(String proposedName) {
        if (findByName(proposedName) == null) {
            return proposedName;
        }
        for (int index = 2; true; index++) {
            String candidate = String.format("%s%d", proposedName, index);
            if (findByName(candidate) == null) {
                return candidate;
            }
        }
    }

}
