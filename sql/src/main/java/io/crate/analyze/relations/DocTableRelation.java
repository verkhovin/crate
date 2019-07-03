/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package io.crate.analyze.relations;

import com.google.common.annotations.VisibleForTesting;
import io.crate.exceptions.ColumnUnknownException;
import io.crate.exceptions.ColumnValidationException;
import io.crate.expression.symbol.DynamicReference;
import io.crate.expression.symbol.Symbol;
import io.crate.metadata.ColumnIdent;
import io.crate.metadata.GeneratedReference;
import io.crate.metadata.Reference;
import io.crate.metadata.doc.DocTableInfo;
import io.crate.metadata.table.Operation;

import javax.annotation.Nullable;
import java.util.List;

public class DocTableRelation extends AbstractTableRelation<DocTableInfo> {

    public DocTableRelation(DocTableInfo tableInfo) {
        super(tableInfo);
    }

    @Override
    public <C, R> R accept(AnalyzedRelationVisitor<C, R> visitor, C context) {
        return visitor.visitDocTableRelation(this, context);
    }

    @Nullable
    @Override
    public Symbol getField(ColumnIdent path) {
        return getField(path, Operation.READ);
    }

    @Override
    public Symbol getField(ColumnIdent path, Operation operation) throws UnsupportedOperationException, ColumnUnknownException {
        if (operation == Operation.UPDATE) {
            ensureColumnCanBeUpdated(path);
        }
        Reference reference = tableInfo.getReference(path);
        if (reference == null) {
            reference = tableInfo.indexColumn(path);
            if (reference == null) {
                DynamicReference dynamic = tableInfo.getDynamic(path,
                    operation == Operation.INSERT || operation == Operation.UPDATE);
                if (dynamic == null) {
                    return null;
                } else {
                    return dynamic;
                }
            }
        }
        return makeArrayIfContainedInObjectArray(reference);
    }

    /**
     * @throws io.crate.exceptions.ColumnValidationException if the column cannot be updated
     */
    @VisibleForTesting
    void ensureColumnCanBeUpdated(ColumnIdent ci) {
        if (ci.isSystemColumn()) {
            throw new ColumnValidationException(ci.toString(), tableInfo.ident(),
                "Updating a system column is not supported");
        }
        for (ColumnIdent pkIdent : tableInfo.primaryKey()) {
            ensureNotUpdated(ci, pkIdent, "Updating a primary key is not supported");
        }
        if (tableInfo.clusteredBy() != null) {
            ensureNotUpdated(ci, tableInfo.clusteredBy(), "Updating a clustered-by column is not supported");
        }
        List<GeneratedReference> generatedReferences = tableInfo.generatedColumns();

        for (Reference partitionRef : tableInfo.partitionedByColumns()) {
            ensureNotUpdated(ci, partitionRef.column(), "Updating a partitioned-by column is not supported");
            if (!(partitionRef instanceof GeneratedReference)) {
                continue;
            }
            int idx = generatedReferences.indexOf(partitionRef);
            if (idx >= 0) {
                GeneratedReference generatedReference = generatedReferences.get(idx);
                for (Reference reference : generatedReference.referencedReferences()) {
                    ensureNotUpdated(ci, reference.column(),
                        "Updating a column which is referenced in a partitioned by generated column expression is not supported");
                }
            }
        }
    }

    private void ensureNotUpdated(ColumnIdent columnUpdated, ColumnIdent protectedColumnIdent, String errorMessage) {
        if (columnUpdated.equals(protectedColumnIdent) || protectedColumnIdent.isChildOf(columnUpdated)) {
            throw new ColumnValidationException(columnUpdated.toString(), tableInfo.ident(), errorMessage);
        }
    }
}
