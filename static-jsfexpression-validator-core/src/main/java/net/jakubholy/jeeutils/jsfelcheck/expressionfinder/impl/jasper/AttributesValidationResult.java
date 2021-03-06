/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jakubholy.jeeutils.jsfelcheck.expressionfinder.impl.jasper;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import net.jakubholy.jeeutils.jsfelcheck.validator.results.ValidationResult;

/**
 * Results of validation JSF EL expressions in attributes of a (JSF) tag.
 */
public class AttributesValidationResult extends ValidationResult {

    private Map<String, ValidationResult> results = new Hashtable<String, ValidationResult>();
    private boolean error = false;

    /**
     * Add results of validation of the given attribute.
     * @param attribute (required) the name of the tag's attribute
     * @param result (required)
     */
    public void add(String attribute, ValidationResult result) {
        if (result == null) {
            throw new IllegalArgumentException("result: ValidationResult may not be null");
        }
        results.put(attribute, result);
        error |= result.hasErrors();
    }

    /**
     * Get results of validating EL in the given attribute.
     * @param attribute (required)
     * @return null if the attribute had no EL
     */
    public ValidationResult get(String attribute) {
        return results.get(attribute);
    }

    @Override
    public boolean hasErrors() {
        return error;
    }

    /**
     * True if any attribute's value contained an EL.
     * @return see above
     */
    public boolean jsfExpressionsFound() {
        return !results.isEmpty();
    }

    public Collection<ValidationResult> getAllResults() {
        return results.values();
    }

    public Map<String, ValidationResult> getAllResultsMap() {
        return Collections.unmodifiableMap(results);
    }

}
