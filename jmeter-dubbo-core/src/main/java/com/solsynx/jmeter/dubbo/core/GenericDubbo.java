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

package com.solsynx.jmeter.dubbo.core;

import com.solsynx.jmeter.dubbo.DubboSampleResult;
import com.solsynx.jmeter.dubbo.context.ServiceContext;
import com.solsynx.jmeter.dubbo.utils.JMeterUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.service.GenericService;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于 JMeter 插件的 Dubbo 客户端
 * 提供了对 Dubbo 服务的泛化调用能力，支持通过注册中心或直连方式调用服务
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 * @since 0.0.1
 * @since 0.0.2 提前设置结果上下文信息，优化了 execute 方法的 RpcContext 处理逻辑
 */
public class GenericDubbo {

    public static final String UTF_8 = "UTF-8";
    public static final String ERROR_RESPONSE_CODE = "500";
    static Logger log = LoggerFactory.getLogger(GenericDubbo.class);

    static {
        ApplicationConfig application = new ApplicationConfig();
        application.setName("jmeter-plugin-dubbo");
        application.setQosEnable(false);
        application.setQosAcceptForeignIp(false);
        ApplicationModel.getConfigManager().setApplication(application);
    }

    /**
     * 私有构造函数，防止实例化
     */
    private GenericDubbo() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 执行 Dubbo 服务调用
     *
     * <p>此方法实现了对 Dubbo 服务的泛化调用，包括连接建立、服务调用和结果处理。
     * 在 0.1.0 版本中优化了连接时间计算和异常处理逻辑，提升了性能和稳定性。</p>
     *
     * @param context 服务上下文，包含调用参数和配置信息
     * @return DubboSampleResult 包含调用结果的采样结果对象
     * @throws RuntimeException 当服务调用发生不可恢复错误时
     * @author Solsynx<xy.0520@hotmail.com>
     * @see ServiceContext
     * @see DubboSampleResult
     * @see GenericService#$invoke(String, String[], Object[])
     * @since 0.0.1
     * @since 0.0.2 提前设置结果上下文信息，优化了 execute 方法的 RpcContext 处理逻辑
     */
    public static DubboSampleResult execute(ServiceContext context) {
        DubboSampleResult result = initializeResult(context);
        RpcContext rpcContext = RpcContext.getContext();
        try {
            long connectStartTime = System.currentTimeMillis();
            GenericService service = getService(context);
            long connectTime = System.currentTimeMillis() - connectStartTime;

            result.sampleStart();

            long latencyStartTime = System.currentTimeMillis();
            Object o = executeCall(context, rpcContext, service);
            long latencyTime = System.currentTimeMillis() - latencyStartTime;

            setSuccessResult(result, o);
            result.setConnectTime(connectTime);  // 连接时间
            result.setLatency(latencyTime);      // 延迟时间
        } catch (Throwable throwable) {
            log.warn(throwable.getMessage(), throwable);
            handleException(throwable, result);
        } finally {
            finalizeResult(result, rpcContext);
        }
        return result;
    }

    /**
     * 执行 Dubbo 服务调用
     *
     * @param context    服务上下文
     * @param rpcContext RpcContext 对象
     * @param service    泛化服务实例
     * @since 0.0.2
     */
    private static Object executeCall(ServiceContext context, RpcContext rpcContext, GenericService service) {
        rpcContext.setAttachments(context.getAttachment());
        String[] parameterTypes = context.getParameters().keySet().toArray(new String[0]);
        Object[] parameters = context.getParameters().values().toArray();
        return service.$invoke(context.getMethodName(), parameterTypes, parameters);
    }

    /**
     * 设置成功结果对象
     *
     * @param result 采样结果对象
     * @param o      服务调用返回的对象
     * @since 0.0.2
     */
    private static void setSuccessResult(DubboSampleResult result, Object o) {
        result.setSuccessful(true);
        result.setResponseCodeOK();
        result.setResponseMessageOK();
        result.setDataType(SampleResult.TEXT);
        result.setResponseData(JMeterUtils.toString(o), UTF_8);
    }

    /**
     * 完善结果对象的最终信息
     *
     * @param result     采样结果对象
     * @param rpcContext RpcContext 对象
     * @since 0.0.2
     */
    private static void finalizeResult(DubboSampleResult result, RpcContext rpcContext) {
        result.setProviderUrl(JMeterUtils.toURL(JMeterUtils.toIdentityString(rpcContext.getUrl())));
        result.setRequestHeaders(JMeterUtils.mapAsString(rpcContext.getObjectAttachments()));
        if (result.getEndTime() == 0) {
            result.sampleEnd();
        }
    }

    /**
     * 执行 Dubbo 服务调用
     *
     * @param throwable 错误对象
     * @param result    采样结果对象
     * @since 0.0.2
     */
    private static void handleException(Throwable throwable, DubboSampleResult result) {
        result.setSuccessful(false);
        result.setResponseCode(ERROR_RESPONSE_CODE);
        result.setResponseMessage(throwable.getClass().getName());
        result.setDataType(SampleResult.TEXT);
        result.setResponseData(ExceptionUtils.getStackTrace(throwable), UTF_8);
    }

    /**
     * 初始化采样结果对象
     *
     * @param context 服务上下文
     * @return 初始化的 DubboSampleResult 对象
     * @since 0.0.2
     */
    private static DubboSampleResult initializeResult(ServiceContext context) {
        DubboSampleResult result = new DubboSampleResult();
        result.setSampleLabel(context.getLabel());
        setContext(result, context);
        return result;
    }

    /**
     * 设置结果上下文信息
     *
     * @param result  采样结果对象
     * @param context 服务上下文
     */
    private static void setContext(DubboSampleResult result, ServiceContext context) {
        if (StringUtils.isBlank(context.getDirectUrl())) {
            result.setRegistryCenter(String.join(
                "://",
                JMeterUtils.defaultValue(context.getRegistryType()),
                JMeterUtils.defaultValue(context.getRegistryAddress())
            ));
            result.setRegistryGroup(context.getRegistryGroup());
        } else {
            result.setDirectUrl(JMeterUtils.toURL(context.getDirectUrl()));
        }
        result.setInterfaceName(context.getInterfaceName());
        result.setMethodName(context.getMethodName());
        result.setServiceGroup(context.getServiceGroup());
        result.setParameters(context.getParameters());
    }

    /**
     * 获取泛化服务实例
     *
     * @param context 服务上下文
     * @return GenericService 泛化服务实例
     * @since 0.0.2
     */
    public static GenericService getService(ServiceContext context) {
        return ReferenceConfigCache.getCache().get(getReferenceConfig(context));
    }

    /**
     * 获取引用配置
     *
     * @param context 服务上下文
     * @return ReferenceConfig 引用配置对象
     */
    public static ReferenceConfig<GenericService> getReferenceConfig(ServiceContext context) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        if (StringUtils.isBlank(context.getDirectUrl())) {
            RegistryConfig registry = new RegistryConfig();
            registry.setProtocol(context.getRegistryType());
            registry.setAddress(context.getRegistryAddress());
            registry.setGroup(context.getRegistryGroup());
            registry.setTimeout(Integer.parseInt(context.getRegistryTimeout()));
            registry.setUsername(context.getRegistryUsername());
            registry.setPassword(context.getRegistryPassword());
            reference.setRegistry(registry);
        } else {
            reference.setUrl(context.getDirectUrl());
        }
        // 服务接口全限定名
        reference.setInterface(context.getInterfaceName());
        // 声明为泛化接口
        reference.setGeneric("true");
        reference.setTimeout(Integer.parseInt(context.getServiceTimeout()));
        // 关闭重试
        reference.setRetries(0);
        return reference;
    }
}
