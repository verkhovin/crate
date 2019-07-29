/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package io.crate.sql.tree;

import com.google.common.base.Objects;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * IF(v1,v2[,v3]): CASE WHEN v1 THEN v2 [ELSE v3] END
 */
public class IfExpression<T> extends Expression<T> {

    private final T condition;
    private final T trueValue;
    private final Optional<T> falseValue;

    public IfExpression(T condition, T trueValue, Optional<T> falseValue) {
        this.condition = checkNotNull(condition, "condition is null");
        this.trueValue = checkNotNull(trueValue, "trueValue is null");
        this.falseValue = falseValue;
    }

    public T getCondition() {
        return condition;
    }

    public T getTrueValue() {
        return trueValue;
    }

    public Optional<T> getFalseValue() {
        return falseValue;
    }

    @Override
    public <R, C> R accept(AstVisitor<T, R, C> visitor, C context) {
        return visitor.visitIfExpression(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        IfExpression o = (IfExpression) obj;
        return Objects.equal(condition, o.condition) &&
               Objects.equal(trueValue, o.trueValue) &&
               Objects.equal(falseValue, o.falseValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(condition, trueValue, falseValue);
    }
}
