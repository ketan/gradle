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

package org.gradle.api.internal.tasks;

import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.TaskExecutionHistory;
import org.gradle.api.internal.TaskInternal;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.TaskOutputs;

import java.util.Collection;

public interface TaskOutputsInternal extends TaskOutputs {
    Spec<? super TaskInternal> getUpToDateSpec();

    /**
     * Returns true if the task declares any outputs.
     */
    boolean getDeclaresOutput();

    /**
     * Check if caching is explicitly enabled for the task outputs.
     */
    boolean isCacheEnabled();

    /**
     * Checks if caching is allowed based on the output properties.
     */
    boolean isCacheAllowed();

    FileCollection getPreviousFiles();

    void setHistory(TaskExecutionHistory history);

    Collection<TaskPropertyOutput> getPropertyOutputs();

    TaskPropertyOutput getPropertyOutput(String propertyName);
}
