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

package io.crate.expression.reference.sys.shard;

import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Singleton;
import org.elasticsearch.index.IndexService;
import org.elasticsearch.index.engine.Segment;
import org.elasticsearch.index.shard.IndexShard;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.indices.IndicesService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Singleton
public class ShardSegments implements Iterable<ShardSegment> {

    private final IndicesService indicesService;

    @Inject
    public ShardSegments(IndicesService indicesService) {
        this.indicesService = indicesService;
    }

    @Override
    public Iterator<ShardSegment> iterator() {
        List<ShardSegment> result = new ArrayList<>();
        for (IndexService indexService : indicesService) {
            for (IndexShard indexShard : indexService) {
                ShardId shardId = indexShard.shardId();
                ShardRouting shardRouting = indexShard.routingEntry();
                if (shardId != null && !shardRouting.unassigned()) {
                    for (Segment segment : indexShard.segments(false)) {
                        result.add(new ShardSegment(shardId.id(), shardId.getIndexName(), segment, shardRouting.primary()));
                    }
                }
            }
        }
        return result.iterator();
    }
}
