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

package net.jakubholy.jeeutils.jsfelcheck.jasperelcustomizer

import com.sun.faces.el.impl.Expression
import com.sun.faces.el.impl.ExpressionInfo

public class RecordingNodeStub extends Expression {

    def called = false
    def value;

    RecordingNodeStub(value) {
        this.value = value
    }

    @Override
    Object evaluate(ExpressionInfo expressionInfo) {
        called = true;
        return value;
    }



    RecordingNodeStub withValue(value) {
        this.value = value;
        this
    }

    @Override
    String getExpressionString() {
        return "<dummy expression implementation class>"
    }
}
