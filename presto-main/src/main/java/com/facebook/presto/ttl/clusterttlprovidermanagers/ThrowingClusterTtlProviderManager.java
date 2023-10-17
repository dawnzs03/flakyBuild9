/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.ttl.clusterttlprovidermanagers;

import com.facebook.presto.spi.ttl.ClusterTtlProviderFactory;
import com.facebook.presto.spi.ttl.ConfidenceBasedTtlInfo;

public class ThrowingClusterTtlProviderManager
        implements ClusterTtlProviderManager
{
    @Override
    public ConfidenceBasedTtlInfo getClusterTtl()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addClusterTtlProviderFactory(ClusterTtlProviderFactory clusterTtlProviderFactory)
    {
    }

    @Override
    public void loadClusterTtlProvider()
    {
    }
}
