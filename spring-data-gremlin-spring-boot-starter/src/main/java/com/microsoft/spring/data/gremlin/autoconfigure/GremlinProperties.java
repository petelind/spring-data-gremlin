/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.apache.tinkerpop.gremlin.util.ser.Serializers;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "gremlin")
public class GremlinProperties {

    private String endpoint;

    private int port = 443;

    private String username;

    private String password;

    private boolean sslEnabled = true;

    private boolean telemetryAllowed = false;

    private String serializer = Serializers.GRAPHSON_V3.toString();

    private int maxContentLength = 65536;
}
