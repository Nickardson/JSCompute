/*
 * Copyright 2013, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.util.rhino.sandbox;

import org.mozilla.javascript.ClassShutter;

/**
 * A {@link ClassShutter} that locks out access to all native classes.
 */
public class SandboxClassShutter implements ClassShutter {

    public static boolean UNRESTRICTED = false;

    public boolean visibleToScripts(String fullClassName) {
        return UNRESTRICTED || fullClassName.startsWith("com.nickardson.jscomputing.javascript.api");
    }
}
