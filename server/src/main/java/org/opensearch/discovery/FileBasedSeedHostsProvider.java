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
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package org.opensearch.discovery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.opensearch.core.common.transport.TransportAddress;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An implementation of {@link SeedHostsProvider} that reads hosts/ports
 * from {@link #UNICAST_HOSTS_FILE}.
 *
 * Each host/port that is part of the discovery process must be listed on
 * a separate line. If the port is left off an entry, we default to the
 * first port in the {@code transport.port} range.
 * An example unicast hosts file could read:
 *
 * 67.81.244.10
 * 67.81.244.11:9305
 * 67.81.244.15:9400
 *
 * @opensearch.internal
 */
public class FileBasedSeedHostsProvider implements SeedHostsProvider {

    private static final Logger logger = LogManager.getLogger(FileBasedSeedHostsProvider.class);

    public static final String UNICAST_HOSTS_FILE = "unicast_hosts.txt";

    private final Path unicastHostsFilePath;

    public FileBasedSeedHostsProvider(Path configFile) {
        this.unicastHostsFilePath = configFile.resolve(UNICAST_HOSTS_FILE);
    }

    private List<String> getHostsList() {
        if (Files.exists(unicastHostsFilePath)) {
            try (Stream<String> lines = Files.lines(unicastHostsFilePath)) {
                return lines.filter(line -> line.startsWith("#") == false) // lines starting with `#` are comments
                    .collect(Collectors.toList());
            } catch (IOException e) {
                logger.warn(() -> new ParameterizedMessage("failed to read file [{}]", unicastHostsFilePath), e);
                return Collections.emptyList();
            }
        }

        logger.warn("expected, but did not find, a dynamic hosts list at [{}]", unicastHostsFilePath);

        return Collections.emptyList();
    }

    @Override
    public List<TransportAddress> getSeedAddresses(HostsResolver hostsResolver) {
        final List<TransportAddress> transportAddresses = hostsResolver.resolveHosts(getHostsList());
        logger.debug("seed addresses: {}", transportAddresses);
        return transportAddresses;
    }
}
