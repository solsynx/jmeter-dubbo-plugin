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

import java.util.Set;

/**
 * 注册中心类型的提供者接口
 * 每个注册中心模块都应提供此接口的实现
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 */
public interface RegistryTypeProvider {

    /**
     * 获取此提供者支持的所有注册中心类型
     *
     * @return 支持的注册中心类型的集合
     */
    Set<String> getSupportedTypes();

    /**
     * 检查某个注册中心类型是否被此提供者支持
     *
     * @param type 注册中心类型
     * @return 如果支持则返回 true，否则返回 false
     */
    boolean isSupported(String type);

    /**
     * 获取指定注册中心类型的默认值
     *
     * @param type 注册中心类型
     * @return 包含默认值的 RegistryDefaults 对象
     */
    RegistryDefaults getDefaults(String type);

    /**
     * 注册中心默认值容器
     *
     * @author Yang Jun
     * @version 1.0.0
     * @since 2025
     */
    class RegistryDefaults {
        private final String defaultAddress;
        private final String defaultGroup;
        private final String defaultTimeout;

        /**
         * 构造函数，创建一个新的 RegistryDefaults 实例
         *
         * @param defaultAddress 默认地址
         * @param defaultGroup 默认组
         * @param defaultTimeout 默认超时时间
         */
        public RegistryDefaults(String defaultAddress, String defaultGroup, String defaultTimeout) {
            this.defaultAddress = defaultAddress;
            this.defaultGroup = defaultGroup;
            this.defaultTimeout = defaultTimeout;
        }

        /**
         * 获取默认地址
         *
         * @return 默认地址
         */
        public String getDefaultAddress() {
            return defaultAddress;
        }

        /**
         * 获取默认组
         *
         * @return 默认组
         */
        public String getDefaultGroup() {
            return defaultGroup;
        }

        /**
         * 获取默认超时时间
         *
         * @return 默认超时时间
         */
        public String getDefaultTimeout() {
            return defaultTimeout;
        }
    }
}
