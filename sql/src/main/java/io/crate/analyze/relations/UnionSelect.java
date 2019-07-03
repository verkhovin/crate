/*
 * Licensed to Crate under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Crate licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial
 * agreement.
 */

package io.crate.analyze.relations;

import io.crate.analyze.Fields;
import io.crate.analyze.HavingClause;
import io.crate.analyze.OrderBy;
import io.crate.analyze.WhereClause;
import io.crate.exceptions.ColumnUnknownException;
import io.crate.expression.symbol.Field;
import io.crate.expression.symbol.Symbol;
import io.crate.expression.symbol.Symbols;
import io.crate.metadata.ColumnIdent;
import io.crate.metadata.table.Operation;
import io.crate.sql.tree.QualifiedName;

import javax.annotation.Nullable;
import java.util.List;

public class UnionSelect implements AnalyzedRelation {

    private final Fields fields;
    private final AnalyzedRelation left;
    private final AnalyzedRelation right;
    private final QualifiedName name;

    public UnionSelect(AnalyzedRelation left, AnalyzedRelation right) {
        this.left = left;
        this.right = right;
        this.name = left.getQualifiedName();

        var fieldsFromLeft = left.fields();
        fields = new Fields(fieldsFromLeft.size());
        for (Symbol field : fieldsFromLeft) {
            // Creating a field that points to the field of the left relation isn't 100% accurate.
            // We're pointing to *two* symbols (both left AND right).
            // We could either use a `InputColumn` to do that (by pointing to a position) - (but might be confusing to have InputColumns in the analysis already)
            // Or introduce a `UnionSymbol` or `UnionField` which would take two symbols it is pointing to
            // Since this currently has no effect we go with the left symbol until there is a good reason to change it.
            ColumnIdent path = Symbols.pathFromSymbol(field);
            fields.add(path, new Field(this, path, field));
        }
    }

    public AnalyzedRelation left() {
        return left;
    }

    public AnalyzedRelation right() {
        return right;
    }

    @Override
    public <C, R> R accept(AnalyzedRelationVisitor<C, R> visitor, C context) {
        return visitor.visitUnionSelect(this, context);
    }

    @Override
    public Symbol getField(ColumnIdent path, Operation operation) throws UnsupportedOperationException, ColumnUnknownException {
        if (operation != Operation.READ) {
            throw new UnsupportedOperationException("getField on MultiSourceSelect is only supported for READ operations");
        }
        return fields.getWithSubscriptFallback(path, this, left);
    }

    @Override
    public List<Symbol> fields() {
        return fields.asList();
    }

    @Override
    public QualifiedName getQualifiedName() {
        return name;
    }

    @Override
    public WhereClause where() {
        return WhereClause.MATCH_ALL;
    }

    @Override
    public List<Symbol> groupBy() {
        return List.of();
    }

    @Nullable
    @Override
    public HavingClause having() {
        return null;
    }

    @Nullable
    @Override
    public OrderBy orderBy() {
        return null;
    }

    @Nullable
    @Override
    public Symbol limit() {
        return null;
    }

    @Nullable
    @Override
    public Symbol offset() {
        return null;
    }

    @Override
    public boolean hasAggregates() {
        return false;
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public String toString() {
        return "US{" + left.getQualifiedName().toString() + ',' + right.getQualifiedName().toString() + '}';
    }
}
