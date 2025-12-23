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

package com.solsynx.jmeter.dubbo.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 注册中心类型管理器
 * 通过 SPI 动态加载可用模块中支持的注册中心类型
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 */
public class RegistryTypeManager {

    private static final Logger log = LoggerFactory.getLogger(RegistryTypeManager.class);

    private static final Set<RegistryTypeProvider> PROVIDERS = loadProviders();

    /**
     * 私有构造函数，防止实例化
     */
    private RegistryTypeManager() {
        // Utility class
    }

    /**
     * 从可用模块获取所有支持的注册中心类型
     *
     * @return 支持的注册中心类型的集合
     */
    public static Set<String> getSupportedTypes() {
        Set<String> types = new HashSet<>();
        for (RegistryTypeProvider provider : PROVIDERS) {
            types.addAll(provider.getSupportedTypes());
        }
        return Collections.unmodifiableSet(types);
    }

    /**
     * 获取指定注册中心类型的默认值
     *
     * @param type 注册中心类型
     * @return 包含默认值的 RegistryDefaults 对象，如果类型不支持则返回 null
     */
    public static RegistryTypeProvider.RegistryDefaults getDefaults(String type) {
        for (RegistryTypeProvider provider : PROVIDERS) {
            if (provider.isSupported(type)) {
                return provider.getDefaults(type);
            }
        }
        return null;
    }

    /**
     * 检查注册中心类型是否被支持
     *
     * @param type 注册中心类型
     * @return 如果支持则返回 true，否则返回 false
     */
    public static boolean isSupported(String type) {
        for (RegistryTypeProvider provider : PROVIDERS) {
            if (provider.isSupported(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过 SPI 加载注册中心类型提供者
     *
     * @return 注册中心类型提供者的集合
     */
    private static Set<RegistryTypeProvider> loadProviders() {
        Set<RegistryTypeProvider> providers = new HashSet<>();

        ServiceLoader<RegistryTypeProvider> loader = ServiceLoader.load(RegistryTypeProvider.class);
        for (RegistryTypeProvider provider : loader) {
            providers.add(provider);
            log.info("Loaded registry type provider: {}", provider.getClass().getName());
        }

        return Collections.unmodifiableSet(providers);
    }
}
