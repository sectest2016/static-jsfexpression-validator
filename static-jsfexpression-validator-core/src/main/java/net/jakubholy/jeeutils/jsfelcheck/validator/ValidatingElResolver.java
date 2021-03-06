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

package net.jakubholy.jeeutils.jsfelcheck.validator;

/**
 * A "fake" JSF EL resolver, which doesn't resolve any actual values but validates that the
 * expression is correct. That means that such managed beans and local variables exist and
 * have the properties being referenced.
 */
public interface ValidatingElResolver extends JsfElValidator {

	/**
	 * Set the fall-back variable resolver to use when this resolver doesn't know the EL variable
	 * being validated.
	 * @param unknownVariableResolver (optional)
	 */
    void setUnknownVariableResolver(ElVariableResolver unknownVariableResolver);

    /**
     * List all known variables (managed beans etc) in VariableNotFoundException?
     * @param includeKnownVariablesInException true to include them
     */
    void setIncludeKnownVariablesInException(boolean includeKnownVariablesInException);

    /**
     * Throw {@link net.jakubholy.jeeutils.jsfelcheck.validator.exception.ExpressionRejectedByFilterException}
     * for any expression not accepted by the supplied filter.
     * @param elExpressionFilter (required)
     */
    void addElExpressionFilter(ElExpressionFilter elExpressionFilter);

}