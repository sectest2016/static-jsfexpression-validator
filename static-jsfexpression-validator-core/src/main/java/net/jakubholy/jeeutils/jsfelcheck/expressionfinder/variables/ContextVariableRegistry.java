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

package net.jakubholy.jeeutils.jsfelcheck.expressionfinder.variables;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.jakubholy.jeeutils.jsfelcheck.expressionfinder.impl.jasper.AttributesValidationResult;
import net.jakubholy.jeeutils.jsfelcheck.expressionfinder.pagenodes.PageNode;
import net.jakubholy.jeeutils.jsfelcheck.validator.ElVariableResolver;

/**
 * Registry of local JSF EL variables defined in a view page used for resolving
 * them in a context-sensitive way.
 *
 * It delegates the actual resolution to individual tag resolversIn respecting the current
 * context (scope) starting from the innermost context and proceeding upwards.
 */
public class ContextVariableRegistry implements ElVariableResolver {

    /**
     * Dummy type used for local variables where the actual type is not known because
     * it haven't been declared by the user.
     */
    public static class Error_YouMustDelcareTypeForThisVariable {}  // SUPPRESS CHECKSTYLE

    private static class VariableContex {
        private final long tagId;
        private final VariableInfo variable;

        public VariableContex(long tagId, VariableInfo variableInfo) {
            this.tagId = tagId;
            this.variable = variableInfo;
        }

        public long getTagId() {
            return tagId;
        }

        public VariableInfo getVariable() {
            return variable;
        }

    }

    private final Map<String, TagJsfVariableResolver> resolvers = new Hashtable<String, TagJsfVariableResolver>();

    private final List<VariableContex> contextStack = new LinkedList<VariableContex>();

    /**
     * Register a new "resolver," which is able to extract local variables from tags of the
     * given qualified name.
     *
     * @param tagQName (required) ex.: h:dataTable
     * @param resolver (required)
     * @return this
     */
    public ContextVariableRegistry registerResolverForTag(String tagQName,
            TagJsfVariableResolver resolver) {
        resolvers.put(tagQName, resolver);
        return this;
    }

    @Override
    public Class<?> resolveVariable(String name) {
        for (VariableContex varContext : contextStack) {
            VariableInfo contextVariable = varContext.getVariable();
            if (contextVariable.getVariableName().equals(name)) {
                return contextVariable.getDeclaredVariableType();
            }
        }

        return null;
    }

    /**
     * Extract local variables defined in the given tag, if any.
     * @param jspTag (required) the tag
     * @param resolvedJsfExpressions (required) results of resolving EL expressions used as values in the
     * tag's attributes
     * @throws MissingLocalVariableTypeDeclarationException see
     * {@link TagJsfVariableResolver#extractContextVariables(Map, AttributesValidationResult)}
     */
    public void extractContextVariables(PageNode jspTag,
            AttributesValidationResult resolvedJsfExpressions) throws MissingLocalVariableTypeDeclarationException {

        TagJsfVariableResolver resolverForTag = resolvers.get(jspTag.getQName());

        if (resolverForTag != null) {
            try {
                VariableInfo variable = resolverForTag.extractContextVariables(
                        jspTag.getAttributes(), resolvedJsfExpressions);
                // if not null => create new context & store it
                storeNewContextVariable(jspTag, variable);
            } catch (MissingLocalVariableTypeDeclarationException e) {
                // Add a fake context variable to make the error messages clearer
                // (instead of unknown variable)
                storeNewContextVariable(jspTag, new VariableInfo(
                        e.getVariableName(), Error_YouMustDelcareTypeForThisVariable.class));
                throw e;
            } catch (RuntimeException e) {
	            throw new RuntimeException("Failed to extract local variables from " + jspTag + ":" + e
			            , e);
            }
        }
    }

    private void storeNewContextVariable(PageNode jspTag, VariableInfo variable) {
        if (variable != null) {
            contextStack.add(0, new VariableContex(jspTag.getId(), variable));
        }
    }

    /**
     * Called when closing tag encountered to discard local variables defined in the scope of that tag.
     * @param jspTag (required)
     */
    public void discardContextFor(PageNode jspTag) {
        if (!contextStack.isEmpty() && contextStack.get(0).getTagId() == jspTag.getId()) {
            contextStack.remove(0);
        }

    }

    /** *For testing only* */
    public Map<String, TagJsfVariableResolver> getRegisteredResolvers() {
        return Collections.unmodifiableMap(resolvers);
    }
}
