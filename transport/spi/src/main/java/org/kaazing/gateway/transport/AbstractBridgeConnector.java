/**
 * Copyright (c) 2007-2014 Kaazing Corporation. All rights reserved.
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.kaazing.gateway.transport;

import static java.lang.String.format;

import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSessionInitializer;

import org.kaazing.gateway.resource.address.ResourceAddress;
import org.kaazing.mina.core.service.IoProcessorEx;
import org.kaazing.mina.core.session.IoSessionConfigEx;

public abstract class AbstractBridgeConnector<S extends AbstractBridgeSession<?, ?>> extends AbstractBridgeService<S> implements BridgeConnector {

    private final AtomicBoolean started;
    
    protected AbstractBridgeConnector(IoSessionConfigEx sessionConfig) {
        super(sessionConfig);
        
        started = new AtomicBoolean(false);
    }

    protected IoProcessorEx<S> initProcessor() {
        return new BridgeConnectProcessor<>();
    }
    
    @Override
    protected IoHandler initHandler() {
        return new BridgeConnectHandler();
    }

    protected abstract boolean canConnect(String transportName);

    @Override
    public final ConnectFuture connect(ResourceAddress address, IoHandler handler, IoSessionInitializer<? extends ConnectFuture> initializer) {

        // connect only address with matching scheme
        URI location = address.getResource();
        String schemeName = location.getScheme();
        if (!canConnect(schemeName)) {
            throw new IllegalArgumentException(format("Unexpected scheme \"%s\" for URI: %s", schemeName, location));
        }

        if (started.get() == false) {
            synchronized (started) {
                if (started.get() == false) {
                    init();
                    started.set(true);
                }
            }
        }

        return connectInternal(address, handler, initializer);
    }

    @Override
    public void connectInit(ResourceAddress address) {
        // no-op by default
    }


    @Override
    public void connectDestroy(ResourceAddress address) {
        // no-op by default
    }

    protected abstract <T extends ConnectFuture> ConnectFuture connectInternal(ResourceAddress address, IoHandler handler, IoSessionInitializer<T> initializer);
}
