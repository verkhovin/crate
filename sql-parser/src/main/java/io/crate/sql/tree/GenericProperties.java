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
import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * these are a kind of map/dictionary with string keys and expression values.
 * <p>
 * Valid value expressions are literals or parameters.
 * <p>
 * As it is always possible to have a list of expressions as value, values are always represented as lists.
 * The properties are always merged into a single map in this class, reachable via {@linkplain #properties()}
 * or {@linkplain #get(String)}.
 * <p>
 * Example GenericProperties:
 * <code>
 * a='b',
 * c=1.78
 * d=[1, 2, 3, 'abc']
 * </code>
 */
public class GenericProperties<T> extends Node {

    private static final GenericProperties<?> EMPTY = new GenericProperties<>(ImmutableMap.of());

    public static <T> GenericProperties<T> empty() {
        return (GenericProperties<T>) EMPTY;
    }

    private final Map<String, T> properties;

    public GenericProperties() {
        properties = new HashMap<>();
    }

    private GenericProperties(Map<String, T> map) {
        this.properties = map;
    }

    public Map<String, T> properties() {
        return Collections.unmodifiableMap(properties);
    }

    public T get(String key) {
        return properties.get(key);
    }

    /**
     * merge the given {@linkplain io.crate.sql.tree.GenericProperty} into the contained map.
     *
     * @param property
     */
    public void add(GenericProperty<T> property) {
        properties.put(property.key(), property.value());
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public <U> GenericProperties<U> map(Function<? super T, ? extends U> mapper) {
        Map<String, U> mappedProperties = properties.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> mapper.apply(e.getValue())
        ));
        return new GenericProperties<>(mappedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(properties);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericProperties that = (GenericProperties) o;

        if (!properties.equals(that.properties)) return false;

        return true;
    }

    @Override
    public String toString() {
        return properties.toString();
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitGenericProperties(this, context);
    }

    public int size() {
        return properties.size();
    }

    public Set<String> keys() {
        return properties.keySet();
    }
}
