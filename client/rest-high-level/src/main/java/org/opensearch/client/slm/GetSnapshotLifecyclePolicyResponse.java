/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */

package org.opensearch.client.slm;

import org.opensearch.core.xcontent.ToXContentObject;
import org.opensearch.core.xcontent.XContentBuilder;
import org.opensearch.core.xcontent.XContentParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.opensearch.core.xcontent.XContentParserUtils.ensureExpectedToken;

public class GetSnapshotLifecyclePolicyResponse implements ToXContentObject {

    private final Map<String, SnapshotLifecyclePolicyMetadata> policies;

    public GetSnapshotLifecyclePolicyResponse(Map<String, SnapshotLifecyclePolicyMetadata> policies) {
        this.policies = policies;
    }

    public Map<String, SnapshotLifecyclePolicyMetadata> getPolicies() {
        return this.policies;
    }

    public static GetSnapshotLifecyclePolicyResponse fromXContent(XContentParser parser) throws IOException {
        if (parser.currentToken() == null) {
            parser.nextToken();
        }
        ensureExpectedToken(XContentParser.Token.START_OBJECT, parser.currentToken(), parser);
        parser.nextToken();

        Map<String, SnapshotLifecyclePolicyMetadata> policies = new HashMap<>();
        while (parser.isClosed() == false) {
            if (parser.currentToken() == XContentParser.Token.START_OBJECT) {
                final String policyId = parser.currentName();
                SnapshotLifecyclePolicyMetadata policyDefinition = SnapshotLifecyclePolicyMetadata.parse(parser, policyId);
                policies.put(policyId, policyDefinition);
            } else {
                parser.nextToken();
            }
        }
        return new GetSnapshotLifecyclePolicyResponse(policies);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        return builder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GetSnapshotLifecyclePolicyResponse other = (GetSnapshotLifecyclePolicyResponse) o;
        return Objects.equals(this.policies, other.policies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.policies);
    }
}
