/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.solsynx.jmeter.dubbo.registry.zookeeper;

import java.util.Collections;
import java.util.Set;

import com.solsynx.jmeter.dubbo.config.RegistryTypeProvider;

/**
 * Zookeeper 注册中心类型提供者
 * 实现了 RegistryTypeProvider 接口，提供对 Zookeeper 注册中心的支持
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 */
public class ZookeeperRegistryTypeProvider implements RegistryTypeProvider {

    private static final String ZOOKEEPER_TYPE = "zookeeper";
    private static final Set<String> SUPPORTED_TYPES = Collections.singleton(ZOOKEEPER_TYPE);

    /**
     * 获取此提供者支持的注册中心类型
     *
     * @return 支持的注册中心类型的集合
     */
    @Override
    public Set<String> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    /**
     * 检查指定的注册中心类型是否被支持
     *
     * @param type 注册中心类型
     * @return 如果支持则返回 true，否则返回 false
     */
    @Override
    public boolean isSupported(String type) {
        return ZOOKEEPER_TYPE.equals(type);
    }

    /**
     * 获取指定注册中心类型的默认值
     *
     * @param type 注册中心类型
     * @return 包含默认值的 RegistryDefaults 对象
     */
    @Override
    public RegistryTypeProvider.RegistryDefaults getDefaults(String type) {
        if (isSupported(type)) {
            return new RegistryTypeProvider.RegistryDefaults("localhost:2181", "dubbo", "3000");
        }
        return null;
    }
}
