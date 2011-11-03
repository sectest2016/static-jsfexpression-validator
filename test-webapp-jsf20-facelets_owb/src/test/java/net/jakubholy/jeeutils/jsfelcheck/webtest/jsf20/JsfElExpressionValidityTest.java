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

package net.jakubholy.jeeutils.jsfelcheck.webtest.jsf20;

import net.jakubholy.jeeutils.jsfelcheck.CollectedValidationResults;
import net.jakubholy.jeeutils.jsfelcheck.JsfStaticAnalyzer;
import net.jakubholy.jeeutils.jsfelcheck.config.ManagedBeansAndVariablesConfiguration;
import net.jakubholy.jeeutils.jsfelcheck.webtest.jsf20.test.MyActionBean;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.print.Book;
import java.io.File;

import static net.jakubholy.jeeutils.jsfelcheck.config.LocalVariableConfiguration.declareLocalVariable;
import static net.jakubholy.jeeutils.jsfelcheck.config.ManagedBeansAndVariablesConfiguration.forExtraVariables;
import static org.junit.Assert.assertEquals;

/**
 * Check EL expressions that are supposed to be  validated successfully.
 */
public class JsfElExpressionValidityTest {

	@Ignore("not yet implemented")
    @Test
    public void verify_all_el_expressions_valid() throws Exception {

        JsfStaticAnalyzer jsfStaticAnalyzer = createConfiguredAnalyzer();

        jsfStaticAnalyzer.withLocalVariablesConfiguration(
		        declareLocalVariable("shop.books", Book.class)
				        //.withCustomDataTableTagAlias("t:dataTable"))
				        )
                .withManagedBeansAndVariablesConfiguration(
		                forExtraVariables()
                        //fromFacesConfigFiles(new File("src/main/webapp/WEB-INF/faces-config.xml"))
                            .withExtraVariable("myActionBean", MyActionBean.class))
                ;

        CollectedValidationResults results = jsfStaticAnalyzer.validateElExpressions(new File("src/main/webapp"));

        assertEquals("There shall be no invalid JSF EL expressions; check System.err/.out for details. FAILURE "
                + results.failures()
                , 0, results.failures().size());

    }

    private JsfStaticAnalyzer createConfiguredAnalyzer() {
        JsfStaticAnalyzer jsfStaticAnalyzer = JsfStaticAnalyzer.forFacelets();
        jsfStaticAnalyzer.setPrintCorrectExpressions(false);
        return jsfStaticAnalyzer;
    }

}
