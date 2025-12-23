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

package com.solsynx.jmeter.dubbo;

import com.solsynx.jmeter.dubbo.utils.JMeterUtils;
import org.apache.dubbo.common.URL;
import org.apache.jmeter.samplers.SampleResult;

import java.util.Map;

/**
 * 扩展的 SampleResult 用于 Dubbo 调用
 * 此类增加了存储 Dubbo 特定信息的功能，如提供者实例详情和附件
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 */
public class DubboSampleResult extends SampleResult {

    private static final long serialVersionUID = 1L;

    private String registryCenter;
    private String registryGroup;
    private URL directUrl;
    private URL providerUrl;
    private String interfaceName;
    private String methodName;
    private String serviceGroup;

    private Map<String, String> parameters;

    /**
     * 构造函数，创建一个新的 DubboSampleResult 实例
     */
    public DubboSampleResult() {
        super();
    }

    /**
     * 获取注册中心
     * @return 注册中心地址
     */
    public String getRegistryCenter() {
        return registryCenter;
    }

    /**
     * 设置注册中心
     * @param registryCenter 注册中心地址
     */
    public void setRegistryCenter(String registryCenter) {
        this.registryCenter = registryCenter;
    }

    /**
     * 获取注册组
     * @return 注册组名称
     */
    public String getRegistryGroup() {
        return registryGroup;
    }

    /**
     * 设置注册组
     * @param registryGroup 注册组名称
     */
    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    /**
     * 获取直连 URL
     * @return 直连 URL
     */
    public URL getDirectUrl() {
        return directUrl;
    }

    /**
     * 设置直连 URL
     * @param directUrl 直连 URL
     */
    public void setDirectUrl(URL directUrl) {
        this.directUrl = directUrl;
    }

    /**
     * 获取提供者 URL
     * @return 提供者 URL
     */
    public URL getProviderUrl() {
        return providerUrl;
    }

    /**
     * 设置提供者 URL
     * @param providerUrl 提供者 URL
     */
    public void setProviderUrl(URL providerUrl) {
        this.providerUrl = providerUrl;
    }

    /**
     * 获取接口名称
     * @return 接口全限定名
     */
    public String getInterfaceName() {
        return interfaceName;
    }

    /**
     * 设置接口名称
     * @param interfaceName 接口全限定名
     */
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    /**
     * 获取方法名称
     * @return 方法名
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置方法名称
     * @param methodName 方法名
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * 获取服务组
     * @return 服务组名称
     */
    public String getServiceGroup() {
        return serviceGroup;
    }

    /**
     * 设置服务组
     * @param serviceGroup 服务组名称
     */
    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    /**
     * 获取参数映射
     * @return 参数映射表
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * 设置参数映射
     * @param parameters 参数映射表
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /**
     * 获取采样器数据字符串
     * @return 采样器数据字符串，包含注册中心、接口、方法等信息
     */
    @Override
    public String getSamplerData() {
        return String.join("\n",
                           "RregistryCenter: ".concat(JMeterUtils.defaultValue(registryCenter)),
                           "RegistryGroup: ".concat(JMeterUtils.defaultValue(registryGroup)),
                           "DirectUrl: ".concat(JMeterUtils.defaultValue(JMeterUtils.toIdentityString(directUrl))),
                           "ProviderUrl: ".concat(JMeterUtils.defaultValue(JMeterUtils.toIdentityString(providerUrl))),
                           "ServiceGroup: ".concat(JMeterUtils.defaultValue(serviceGroup)),
                           "InterfaceName: ".concat(JMeterUtils.defaultValue(interfaceName)),
                           "MethodName: ".concat(JMeterUtils.defaultValue(methodName))) + "\n\n" +
            getParametersAsString();
    }

    /**
     * 获取参数字符串表示
     * @return 参数的字符串表示形式
     */
    public String getParametersAsString() {
        StringBuilder sb = new StringBuilder("Parameters: \n");
        if (parameters == null || parameters.isEmpty()) {
            sb.append("No parameters");
        } else {
            append(sb, parameters);
        }
        return sb.toString();
    }

    /**
     * 将参数映射追加到字符串构建器
     * @param sb 字符串构建器
     * @param map 参数映射表
     */
    private void append(StringBuilder sb, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
    }
}
