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

package io.crate.analyze;

import io.crate.analyze.relations.AnalyzedRelation;

import javax.annotation.Nullable;

public class AnalyzedStatementVisitor<C, R> {

    public R process(AnalyzedStatement analyzedStatement, @Nullable C context) {
        return analyzedStatement.accept(this, context);
    }

    protected R visitAnalyzedStatement(AnalyzedStatement analyzedStatement, C context) {
        return null;
    }

    protected R visitCopyFromStatement(CopyFromAnalyzedStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    protected R visitCopyToStatement(CopyToAnalyzedStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    protected R visitCreateTableStatement(CreateTableAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitCreateRepositoryAnalyzedStatement(AnalyzedCreateRepository analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitInsertFromValuesStatement(InsertFromValuesAnalyzedStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    protected R visitInsertFromSubQueryStatement(InsertFromSubQueryAnalyzedStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    public R visitSelectStatement(AnalyzedRelation relation, C context) {
        return visitAnalyzedStatement(relation, context);
    }

    protected R visitCreateFunctionStatement(CreateFunctionAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitDropFunctionStatement(DropFunctionAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitCreateUserStatement(CreateUserAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitDropUserStatement(DropUserAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitCreateAnalyzerStatement(CreateAnalyzerAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitDropAnalyzerStatement(DropAnalyzerStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitDDLStatement(DDLStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    public R visitCreateBlobTableStatement(CreateBlobTableAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitOptimizeTableStatement(AnalyzedOptimizeTable analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitRefreshTableStatement(AnalyzedRefreshTable analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitAlterTableStatement(AlterTableAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitAnalyzedAlterTableRename(AnalyzedAlterTableRename analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitRerouteRetryFailedStatement(RerouteRetryFailedAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitAlterBlobTableStatement(AlterBlobTableAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitAlterUserStatement(AlterUserAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitSetStatement(SetAnalyzedStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    public R visitSetLicenseStatement(SetLicenseAnalyzedStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    public R visitAddColumnStatement(AddColumnAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitAnalyzedAlterTableOpenClose(AnalyzedAlterTableOpenClose analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitKillAnalyzedStatement(KillAnalyzedStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    public R visitDeallocateAnalyzedStatement(DeallocateAnalyzedStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    public R visitShowCreateTableAnalyzedStatement(AnalyzedShowCreateTable analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    public R visitDropRepositoryAnalyzedStatement(AnalyzedDropRepository analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitDropSnapshotAnalyzedStatement(AnalyzedDropSnapshot analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitCreateSnapshotAnalyzedStatement(AnalyzedCreateSnapshot analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitRestoreSnapshotAnalyzedStatement(RestoreSnapshotAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    public R visitResetAnalyzedStatement(ResetAnalyzedStatement resetAnalyzedStatement, C context) {
        return visitAnalyzedStatement(resetAnalyzedStatement, context);
    }

    public R visitExplainStatement(ExplainAnalyzedStatement explainAnalyzedStatement, C context) {
        return visitAnalyzedStatement(explainAnalyzedStatement, context);
    }

    public R visitBegin(AnalyzedBegin analyzedBegin, C context) {
        return visitAnalyzedStatement(analyzedBegin, context);
    }

    public R visitCommit(AnalyzedCommit analyzedCommit, C context) {
        return visitAnalyzedStatement(analyzedCommit, context);
    }

    public R visitPrivilegesStatement(PrivilegesAnalyzedStatement analysis, C context) {
        return visitDCLStatement(analysis, context);
    }

    public R visitDCLStatement(DCLStatement analysis, C context) {
        return visitAnalyzedStatement(analysis, context);
    }

    protected R visitRerouteMoveShard(RerouteMoveShardAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitRerouteAllocateReplicaShard(RerouteAllocateReplicaShardAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitRerouteCancelShard(RerouteCancelShardAnalyzedStatement analysis, C context) {
        return visitDDLStatement(analysis, context);
    }

    protected R visitAnalyzedDeleteStatement(AnalyzedDeleteStatement statement, C context) {
        return visitAnalyzedStatement(statement, context);
    }

    public R visitAnalyzedUpdateStatement(AnalyzedUpdateStatement statement, C context) {
        return visitAnalyzedStatement(statement, context);
    }

    public R visitInsert(AnalyzedInsertStatement insert, C context) {
        return visitAnalyzedStatement(insert, context);
    }

    public R visitCreateViewStmt(CreateViewStmt createViewStmt, C context) {
        return visitAnalyzedStatement(createViewStmt, context);
    }

    public R visitDropView(DropViewStmt dropViewStmt, C context) {
        return visitAnalyzedStatement(dropViewStmt, context);
    }

    public R visitSwapTable(AnalyzedSwapTable swapTable, C context) {
        return visitAnalyzedStatement(swapTable, context);
    }

    public R visitGCDanglingArtifacts(AnalyzedGCDanglingArtifacts gcDanglingIndices, C context) {
        return visitAnalyzedStatement(gcDanglingIndices, context);
    }

    public R visitDecommissionNode(AnalyzedDecommissionNodeStatement decommissionNode, C context) {
        return visitAnalyzedStatement(decommissionNode, context);
    }

    public R visitReroutePromoteReplica(PromoteReplicaStatement promoteReplicaStatement, C context) {
        return visitDDLStatement(promoteReplicaStatement, context);
    }

    public R visitDropTable(DropTableAnalyzedStatement<?> dropTable, C context) {
        return visitDDLStatement(dropTable, context);
    }

    public R visitCreateTable(AnalyzedCreateTable createTable, C context) {
        return visitDDLStatement(createTable, context);
    }

    public R visitAlterTableAddColumn(AnalyzedAlterTableAddColumn alterTableAddColumn, C context) {
        return visitDDLStatement(alterTableAddColumn, context);
    }

    public R visitAlterTable(AnalyzedAlterTable alterTable, C context) {
        return visitDDLStatement(alterTable, context);
    }
}
