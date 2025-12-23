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

package com.solsynx.jmeter.dubbo.converter;

import com.solsynx.jmeter.dubbo.DubboSampleResult;
import com.solsynx.jmeter.dubbo.utils.JMeterUtils;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.save.converters.SampleResultConverter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dubbo 采样结果转换器
 * 用于将 DubboSampleResult 对象序列化为 XML 格式或从 XML 格式反序列化为对象
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 */
public class DubboSampleResultConverter extends SampleResultConverter {

    protected static final String TAG_REGISTRY_CENTER = "registryCenter";
    protected static final String TAG_REGISTRY_GROUP = "registryGroup";
    protected static final String TAG_DIRECT_URL = "directUrl";
    protected static final String TAG_PROVIDER_URL = "providerUrl";
    protected static final String TAG_SERVICE_GROUP = "serviceGroup";
    protected static final String TAG_INTERFACE_NAME = "interfaceName";
    protected static final String TAG_METHOD_NAME = "methodName";
    protected static final String TAG_PARAMETERS = "parameters";

    /**
     * 构造函数，创建一个新的 DubboSampleResultConverter 实例
     *
     * @param mapper 映射器对象
     */
    public DubboSampleResultConverter(Mapper mapper) {
        super(mapper);
    }

    /**
     * 从 XML 读取 Dubbo 相关项并设置到结果对象
     *
     * @param reader  层次化读取器
     * @param result  结果对象
     * @param subItem 子项对象
     */
    private static void retrieveDubboItem(
        HierarchicalStreamReader reader,
        DubboSampleResult result, Object subItem) {
        String nodeName = reader.getNodeName();
        String value = (String) subItem;
        switch (nodeName) {
            case TAG_REGISTRY_CENTER:
                result.setRegistryCenter(value);
                break;
            case TAG_REGISTRY_GROUP:
                result.setRegistryGroup(value);
                break;
            case TAG_DIRECT_URL:
                result.setDirectUrl(JMeterUtils.toURL(value));
                break;
            case TAG_PROVIDER_URL:
                result.setProviderUrl(JMeterUtils.toURL(value));
                break;
            case TAG_SERVICE_GROUP:
                result.setServiceGroup(value);
                break;
            case TAG_INTERFACE_NAME:
                result.setInterfaceName(value);
                break;
            case TAG_METHOD_NAME:
                result.setMethodName(value);
                break;
            case TAG_PARAMETERS:
                result.setParameters(readParameters(reader));
                break;
        }
    }

    /**
     * 从 XML 读取参数映射
     *
     * @param reader 层次化读取器
     * @return 参数映射表
     */
    private static Map<String, String> readParameters(HierarchicalStreamReader reader) {
        Map<String, String> parameters = new LinkedHashMap<>();

        if (reader.hasMoreChildren()) {
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                if ("item".equals(reader.getNodeName())) {
                    String key = reader.getAttribute("key");
                    String value = reader.getValue();
                    parameters.put(key, value);
                }
                reader.moveUp();
            }
        }

        return parameters;
    }

    /**
     * 将对象序列化为 XML 格式
     *
     * @param obj     要序列化的对象
     * @param writer  层次化写入器
     * @param context 序列化上下文
     */
    @Override
    public void marshal(
        Object obj, HierarchicalStreamWriter writer,
        MarshallingContext context) {
        DubboSampleResult result = (DubboSampleResult) obj;
        SampleSaveConfiguration save = result.getSaveConfig();
        setAttributes(writer, context, result, save);
        saveAssertions(writer, context, result, save);
        saveSubResults(writer, context, result, save);
        saveRequestHeaders(writer, context, result, save);
        saveResponseData(writer, context, result, save);
        saveSamplerData(writer, result, save);
    }

    /**
     * 保存采样器数据到 XML
     *
     * @param writer 层次化写入器
     * @param result 结果对象
     * @param save   保存配置
     */
    private void saveSamplerData(
        HierarchicalStreamWriter writer, DubboSampleResult result,
        SampleSaveConfiguration save) {
        if (save.saveSamplerData(result)) {
            writeString(writer, TAG_REGISTRY_CENTER, result.getRegistryCenter());
            writeString(writer, TAG_REGISTRY_GROUP, result.getRegistryGroup());
            writeString(writer, TAG_DIRECT_URL, JMeterUtils.toIdentityString(result.getDirectUrl()));
            writeString(writer, TAG_PROVIDER_URL, JMeterUtils.toIdentityString(result.getProviderUrl()));
            writeString(writer, TAG_SERVICE_GROUP, result.getServiceGroup());
            writeString(writer, TAG_INTERFACE_NAME, result.getInterfaceName());
            writeString(writer, TAG_METHOD_NAME, result.getMethodName());
            writeParameters(writer, result.getParameters());
        }
    }

    /**
     * 将参数映射写入 XML
     *
     * @param writer     层次化写入器
     * @param parameters 参数映射表
     */
    private void writeParameters(HierarchicalStreamWriter writer, Map<String, String> parameters) {
        writer.startNode(TAG_PARAMETERS);
        writer.addAttribute("class", LinkedHashMap.class.getName());
        if (parameters != null && !parameters.isEmpty()) {

            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                writer.startNode("item");
                writer.addAttribute("key", entry.getKey());
                writer.setValue(entry.getValue());
                writer.endNode();
            }
        }
        writer.endNode();
    }

    /**
     * 从 XML 反序列化为对象
     *
     * @param reader  层次化读取器
     * @param context 反序列化上下文
     * @return 反序列化后的对象
     */
    @Override
    public Object unmarshal(
        HierarchicalStreamReader reader,
        UnmarshallingContext context) {
        DubboSampleResult result = (DubboSampleResult) createCollection(context.getRequiredType());
        retrieveAttributes(reader, context, result);
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            Object subItem = readBareItem(reader, context, result);
            if (!retrieveItem(reader, context, result, subItem)) {
                retrieveDubboItem(reader, result, subItem);
            }
            reader.moveUp();
        }

        // If we have a file, but no data, then read the file
        String resultFileName = result.getResultFileName();
        if (!resultFileName.isEmpty()
            && result.getResponseData().length == 0) {
            readFile(resultFileName, result);
        }
        return result;
    }

    /**
     * 判断是否可以转换指定的类
     *
     * @param clazz 要检查的类
     * @return 如果可以转换则返回 true，否则返回 false
     */
    @Override
    public boolean canConvert(Class clazz) {
        return DubboSampleResult.class.equals(clazz);
    }
}
