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

package org.gradle.api.internal.changedetection.taskcache;

import com.google.common.hash.HashCode;
import org.gradle.api.internal.TaskInternal;

import java.io.File;

public class DefaultTaskInputHasher implements TaskInputHasher {

    private final String cacheVersion;

    public DefaultTaskInputHasher(String cacheVersion) {
        this.cacheVersion = cacheVersion;
    }

    @Override
    public HashCode createHash(TaskInternal task, File cacheRootDir) {
        CacheKeyBuilder cacheKeyBuilder = CacheKeyBuilder.builder(cacheRootDir);

        // Make sure if cache format changes we don't have collisions
        cacheKeyBuilder.put(cacheVersion);
        task.getInputs().appendToCacheKey(cacheKeyBuilder);
        task.getOutputs().appendToCacheKey(cacheKeyBuilder);
        return cacheKeyBuilder.build();
    }
}
