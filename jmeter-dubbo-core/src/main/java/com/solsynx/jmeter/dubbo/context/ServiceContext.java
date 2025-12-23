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

package com.solsynx.jmeter.dubbo.context;

import com.solsynx.jmeter.dubbo.sampler.DubboSampler;
import com.solsynx.jmeter.dubbo.utils.JMeterUtils;

import java.util.Map;

/**
 * 服务上下文类
 * 用于封装 Dubbo 服务调用所需的上下文信息，包括注册中心配置、服务配置等
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 */
public class ServiceContext {

    private String label;

    private String registryType;
    private String registryAddress;
    private String registryGroup;
    private String registryTimeout;
    private String registryUsername;
    private String registryPassword;

    private String directUrl;
    private String serviceGroup;
    private String interfaceName;
    private String methodName;
    private String serviceTimeout;
    private Map<String, String> attachment;
    private Map<String, String> parameters;

    /**
     * 默认构造函数
     */
    public ServiceContext() {
    }

    /**
     * 通过 DubboSampler 构造服务上下文
     * @param sampler Dubbo 采样器实例
     */
    public ServiceContext(DubboSampler sampler) {
        label = sampler.getName();
        registryType = sampler.getRegistryType();
        registryAddress = sampler.getRegistryAddress();
        registryGroup = sampler.getRegistryGroup();
        registryTimeout = sampler.getRegistryTimeout();
        registryUsername = sampler.getRegistryUsername();
        registryPassword = sampler.getRegistryPassword();
        directUrl = sampler.getDirectUrl();
        serviceGroup = sampler.getServiceGroup();
        interfaceName = sampler.getInterfaceName();
        methodName = sampler.getMethodName();
        serviceTimeout = sampler.getServiceTimeout();
        attachment = JMeterUtils.toMap(sampler.getAttachment());
        parameters = JMeterUtils.toMap(sampler.getParameters());
    }


    /**
     * 获取标签
     * @return 标签
     */
    public String getLabel() {
        return label;
    }

    /**
     * 设置标签
     * @param label 标签
     * @return 当前服务上下文实例
     */
    public ServiceContext setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * 获取注册中心类型
     * @return 注册中心类型
     */
    public String getRegistryType() {
        return registryType;
    }

    /**
     * 设置注册中心类型
     * @param registryType 注册中心类型
     * @return 当前服务上下文实例
     */
    public ServiceContext setRegistryType(String registryType) {
        this.registryType = registryType;
        return this;
    }

    /**
     * 获取注册中心地址
     * @return 注册中心地址
     */
    public String getRegistryAddress() {
        return registryAddress;
    }

    /**
     * 设置注册中心地址
     * @param registryAddress 注册中心地址
     * @return 当前服务上下文实例
     */
    public ServiceContext setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
        return this;
    }

    /**
     * 获取注册中心组
     * @return 注册中心组
     */
    public String getRegistryGroup() {
        return registryGroup;
    }

    /**
     * 设置注册中心组
     * @param registryGroup 注册中心组
     * @return 当前服务上下文实例
     */
    public ServiceContext setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
        return this;
    }

    /**
     * 获取注册中心超时时间
     * @return 注册中心超时时间
     */
    public String getRegistryTimeout() {
        return registryTimeout;
    }

    /**
     * 设置注册中心超时时间
     * @param registryTimeout 注册中心超时时间
     * @return 当前服务上下文实例
     */
    public ServiceContext setRegistryTimeout(String registryTimeout) {
        this.registryTimeout = registryTimeout;
        return this;
    }

    /**
     * 获取注册中心用户名
     * @return 注册中心用户名
     */
    public String getRegistryUsername() {
        return registryUsername;
    }

    /**
     * 设置注册中心用户名
     * @param registryUsername 注册中心用户名
     * @return 当前服务上下文实例
     */
    public ServiceContext setRegistryUsername(String registryUsername) {
        this.registryUsername = registryUsername;
        return this;
    }

    /**
     * 获取注册中心密码
     * @return 注册中心密码
     */
    public String getRegistryPassword() {
        return registryPassword;
    }

    /**
     * 设置注册中心密码
     * @param registryPassword 注册中心密码
     * @return 当前服务上下文实例
     */
    public ServiceContext setRegistryPassword(String registryPassword) {
        this.registryPassword = registryPassword;
        return this;
    }

    /**
     * 获取直连 URL
     * @return 直连 URL
     */
    public String getDirectUrl() {
        return directUrl;
    }

    /**
     * 设置直连 URL
     * @param directUrl 直连 URL
     * @return 当前服务上下文实例
     */
    public ServiceContext setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
        return this;
    }

    /**
     * 获取服务组
     * @return 服务组
     */
    public String getServiceGroup() {
        return serviceGroup;
    }

    /**
     * 设置服务组
     * @param serviceGroup 服务组
     * @return 当前服务上下文实例
     */
    public ServiceContext setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
        return this;
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
     * @return 当前服务上下文实例
     */
    public ServiceContext setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
        return this;
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
     * @return 当前服务上下文实例
     */
    public ServiceContext setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    /**
     * 获取服务超时时间
     * @return 服务超时时间
     */
    public String getServiceTimeout() {
        return serviceTimeout;
    }

    /**
     * 设置服务超时时间
     * @param serviceTimeout 服务超时时间
     * @return 当前服务上下文实例
     */
    public ServiceContext setServiceTimeout(String serviceTimeout) {
        this.serviceTimeout = serviceTimeout;
        return this;
    }

    /**
     * 获取附件参数
     * @return 附件参数映射表
     */
    public Map<String, String> getAttachment() {
        return attachment;
    }

    /**
     * 设置附件参数
     * @param attachment 附件参数映射表
     * @return 当前服务上下文实例
     */
    public ServiceContext setAttachment(Map<String, String> attachment) {
        this.attachment = attachment;
        return this;
    }

    /**
     * 获取方法参数
     * @return 方法参数映射表
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * 设置方法参数
     * @param parameters 方法参数映射表
     * @return 当前服务上下文实例
     */
    public ServiceContext setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * 获取注册中心地址
     * @return 注册中心地址字符串
     */
    public String getRegistryCenter() {
        return registryType + "://" + registryAddress;
    }
}
