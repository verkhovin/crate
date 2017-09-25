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

package io.crate.operation.scalar.geo;

import io.crate.analyze.symbol.Literal;
import io.crate.exceptions.ConversionException;
import io.crate.operation.scalar.AbstractScalarFunctionsTest;
import io.crate.types.DataTypes;
import org.junit.Test;

import static io.crate.testing.SymbolMatchers.isLiteral;

public class DistanceFunctionTest extends AbstractScalarFunctionsTest {

    @Test
    public void testResolveWithTooManyArguments() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The number of arguments is incorrect");
        assertNormalize("distance('POINT (10 20)', 'POINT (11 21)', 'foo')", null);
    }

    @Test
    public void testResolveWithInvalidType() throws Exception {
        expectedException.expect(ConversionException.class);
        expectedException.expectMessage("Cannot cast '1' to type geo_point");
        assertNormalize("distance(1, 'POINT (11 21)')", null);
    }

    @Test
    public void testEvaluateWithTwoGeoPointLiterals() throws Exception {
        assertEvaluate("distance(geopoint, geopoint)", 144572.67952051832,
            Literal.of(DataTypes.GEO_POINT, new Double[]{10.04, 28.02}),
            Literal.of(DataTypes.GEO_POINT, DataTypes.GEO_POINT.value("POINT(10.30 29.3)")));
    }

    @Test
    public void testNormalizeWithStringTypes() throws Exception {
        // do not evaluate additional to the normalization as strings are only supported on normalization here
        assertNormalize("distance('POINT (10 20)', 'POINT (11 21)')", isLiteral(152354.3209044634), false);
    }

    @Test
    public void testNormalizeWithDoubleArray() throws Exception {
        assertNormalize("distance([10.0, 20.0], [11.0, 21.0])", isLiteral(152354.3209044634), false);
    }

    @Test
    public void testNormalizeWithInvalidReferences() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cannot cast [10.04, 28.02] to type string");
        assertNormalize("distance(name, [10.04, 28.02])", null);
    }

    @Test
    public void testWithNullValue() throws Exception {
        assertEvaluate("distance(geopoint, geopoint)", null,
            Literal.of(DataTypes.GEO_POINT, null),
            Literal.of(DataTypes.GEO_POINT, DataTypes.GEO_POINT.value("POINT (10 20)")));
        assertEvaluate("distance(geopoint, geopoint)", null,
            Literal.of(DataTypes.GEO_POINT, DataTypes.GEO_POINT.value("POINT (10 20)")),
            Literal.of(DataTypes.GEO_POINT, null));
    }
}
