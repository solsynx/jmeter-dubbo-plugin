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

package com.solsynx.jmeter.dubbo.sampler;

import com.solsynx.jmeter.dubbo.context.ServiceContext;
import com.solsynx.jmeter.dubbo.core.GenericDubbo;
import com.solsynx.jmeter.dubbo.utils.JMeterUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;

import java.io.Serializable;

/**
 * JMeter Dubbo 采样器，用于测试 Dubbo 服务
 * 该类扩展了 AbstractSampler，提供对 Dubbo 服务的调用能力
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 */
public class DubboSampler extends AbstractSampler implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String REGISTRY_TYPE = "DubboSampler.registryType";
    private static final String REGISTRY_ADDRESS = "DubboSampler.registryAddress";
    private static final String REGISTRY_GROUP = "DubboSampler.registryGroup";
    private static final String REGISTRY_TIMEOUT = "DubboSampler.registryTimeout";
    private static final String REGISTRY_USERNAME = "DubboSampler.registryUsername";
    private static final String REGISTRY_PASSWORD = "DubboSampler.registryPassword";
    private static final String DIRECT_URL = "DubboSampler.directUrl";
    private static final String SERVICE_GROUP = "DubboSampler.serviceGroup";
    private static final String INTERFACE_NAME = "DubboSampler.interfaceName";
    private static final String METHOD_NAME = "DubboSampler.methodName";
    private static final String SERVICE_TIMEOUT = "DubboSampler.serviceTimeout";
    private static final String ATTACHMENT = "DubboSampler.attachment";
    private static final String PARAMETERS = "DubboSampler.parameters";

    static {
        JMeterUtils.registerConverters();
    }

    /**
     * 构造函数，创建一个新的 DubboSampler 实例
     */
    public DubboSampler() {
        super();
    }

    /**
     * 执行采样操作
     *
     * @param entry 采样入口点
     * @return 采样结果
     */
    @Override
    public SampleResult sample(Entry entry) {
        return GenericDubbo.execute(new ServiceContext(this));
    }


    /**
     * 获取注册中心类型
     *
     * @return 注册中心类型
     */
    public String getRegistryType() {
        return getPropertyAsString(REGISTRY_TYPE);
    }

    /**
     * 设置注册中心类型
     *
     * @param registryType 注册中心类型
     */
    public void setRegistryType(String registryType) {
        setProperty(REGISTRY_TYPE, registryType);
    }

    /**
     * 获取注册中心地址
     *
     * @return 注册中心地址
     */
    public String getRegistryAddress() {
        return getPropertyAsString(REGISTRY_ADDRESS);
    }

    /**
     * 设置注册中心地址
     *
     * @param registryAddress 注册中心地址
     */
    public void setRegistryAddress(String registryAddress) {
        setProperty(REGISTRY_ADDRESS, registryAddress);
    }

    /**
     * 获取注册中心组
     *
     * @return 注册中心组
     */
    public String getRegistryGroup() {
        return getPropertyAsString(REGISTRY_GROUP);
    }

    /**
     * 设置注册中心组
     *
     * @param registryGroup 注册中心组
     */
    public void setRegistryGroup(String registryGroup) {
        setProperty(REGISTRY_GROUP, registryGroup);
    }

    /**
     * 获取注册中心超时时间
     *
     * @return 注册中心超时时间
     */
    public String getRegistryTimeout() {
        return getPropertyAsString(REGISTRY_TIMEOUT);
    }

    /**
     * 设置注册中心超时时间
     *
     * @param registryTimeout 注册中心超时时间
     */
    public void setRegistryTimeout(String registryTimeout) {
        setProperty(REGISTRY_TIMEOUT, registryTimeout);
    }

    /**
     * 获取注册中心用户名
     *
     * @return 注册中心用户名
     */
    public String getRegistryUsername() {
        return getPropertyAsString(REGISTRY_USERNAME);
    }

    /**
     * 设置注册中心用户名
     *
     * @param registryUsername 注册中心用户名
     */
    public void setRegistryUsername(String registryUsername) {
        setProperty(REGISTRY_USERNAME, registryUsername);
    }

    /**
     * 获取注册中心密码
     *
     * @return 注册中心密码
     */
    public String getRegistryPassword() {
        return getPropertyAsString(REGISTRY_PASSWORD);
    }

    /**
     * 设置注册中心密码
     *
     * @param registryPassword 注册中心密码
     */
    public void setRegistryPassword(String registryPassword) {
        setProperty(REGISTRY_PASSWORD, registryPassword);
    }

    /**
     * 获取直连 URL
     *
     * @return 直连 URL
     */
    public String getDirectUrl() {
        return getPropertyAsString(DIRECT_URL);
    }

    /**
     * 设置直连 URL
     *
     * @param directUrl 直连 URL
     */
    public void setDirectUrl(String directUrl) {
        setProperty(DIRECT_URL, directUrl);
    }

    /**
     * 获取服务组
     *
     * @return 服务组
     */
    public String getServiceGroup() {
        return getPropertyAsString(SERVICE_GROUP);
    }

    /**
     * 设置服务组
     *
     * @param serviceGroup 服务组
     */
    public void setServiceGroup(String serviceGroup) {
        setProperty(SERVICE_GROUP, serviceGroup);
    }

    /**
     * 获取接口名称
     *
     * @return 接口全限定名
     */
    public String getInterfaceName() {
        return getPropertyAsString(INTERFACE_NAME);
    }

    /**
     * 设置接口名称
     *
     * @param interfaceName 接口全限定名
     */
    public void setInterfaceName(String interfaceName) {
        setProperty(INTERFACE_NAME, interfaceName);
    }

    /**
     * 获取方法名称
     *
     * @return 方法名
     */
    public String getMethodName() {
        return getPropertyAsString(METHOD_NAME);
    }

    /**
     * 设置方法名称
     *
     * @param methodName 方法名
     */
    public void setMethodName(String methodName) {
        setProperty(METHOD_NAME, methodName);
    }

    /**
     * 获取服务超时时间
     *
     * @return 服务超时时间
     */
    public String getServiceTimeout() {
        return getPropertyAsString(SERVICE_TIMEOUT);
    }

    /**
     * 设置服务超时时间
     *
     * @param serviceTimeout 服务超时时间
     */
    public void setServiceTimeout(String serviceTimeout) {
        setProperty(SERVICE_TIMEOUT, serviceTimeout);
    }

    /**
     * 获取附件参数
     *
     * @return 附件参数集合
     */
    public Arguments getAttachment() {
        JMeterProperty property = getProperty(ATTACHMENT);
        if (property instanceof NullProperty) {
            return new Arguments();
        }
        return (Arguments) property.getObjectValue();
    }

    /**
     * 设置附件参数
     *
     * @param attachment 附件参数集合
     */
    public void setAttachment(Arguments attachment) {
        setProperty(new TestElementProperty(ATTACHMENT, attachment));
    }

    /**
     * 获取方法参数
     *
     * @return 方法参数集合
     */
    public Arguments getParameters() {
        JMeterProperty property = getProperty(PARAMETERS);
        if (property instanceof NullProperty) {
            return new Arguments();
        }
        return (Arguments) property.getObjectValue();
    }

    /**
     * 设置方法参数
     *
     * @param parameters 方法参数集合
     */
    public void setParameters(Arguments parameters) {
        setProperty(new TestElementProperty(PARAMETERS, parameters));
    }
}
