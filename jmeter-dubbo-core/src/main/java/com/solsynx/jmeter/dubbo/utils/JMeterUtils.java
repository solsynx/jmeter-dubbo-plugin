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

package com.solsynx.jmeter.dubbo.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solsynx.jmeter.dubbo.DubboSampleResult;
import com.solsynx.jmeter.dubbo.converter.DubboSampleResultConverter;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.URL;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.save.SaveService;
import org.apache.jorphan.gui.JLabeledTextField;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * JMeter 工具类
 * 提供各种与 JMeter 相关的实用方法，包括资源字符串获取、参数转换、URL 处理等功能
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 */
public class JMeterUtils {
    private static final ResourceBundle RB = ResourceBundle.getBundle("com/solsynx/jmeter/dubbo/messages");
    private static final ObjectMapper OM = new ObjectMapper();

    static {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OM.setDateFormat(df);
        OM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 私有构造函数，防止实例化
     */
    private JMeterUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取资源字符串
     *
     * @param key 资源键
     * @return 对应的资源字符串
     */
    public static String getResString(String key) {
        return RB.getString(key);
    }

    /**
     * 创建带标签的文本字段
     *
     * @param labelKey 标签资源键
     * @param size     文本字段大小
     * @return JLabeledTextField 实例
     */
    public static JLabeledTextField createJLabeledTextField(String labelKey, int size) {
        return new JLabeledTextField(getResString(labelKey), size);
    }

    /**
     * 将 Arguments 转换为 Map
     *
     * @param args Arguments 对象，可以为 null
     * @return 参数映射表
     */
    public static Map<String, String> toMap(Arguments args) {
        if (args == null) {
            return new LinkedHashMap<>();
        }
        return args.getArgumentsAsMap();
    }

    /**
     * 将 Map 转换为字符串表示
     *
     * @param map 包含键值对的映射表
     * @return 映射表的字符串表示
     */
    public static String mapAsString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(toString(entry.getValue())).append("\n");
        }
        return StringUtils.removeEnd(sb.toString(), "\n");
    }

    /**
     * 将对象转换为字符串
     *
     * @param value 要转换的对象
     * @return 对象的字符串表示
     */
    public static String toString(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof Number) {
            return value.toString();
        }
        try {
            return OM.writeValueAsString(value);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 返回默认值（如果原值为空白）
     *
     * @param value 原始值
     * @return 如果原值不为空白则返回原值，否则返回 "N/A"
     */
    public static String defaultValue(String value) {
        return StringUtils.defaultIfBlank(value, "N/A");
    }

    /**
     * 将 URL 转换为其标识字符串
     *
     * @param url URL 对象，可以为 null
     * @return URL 的标识字符串
     */
    public static String toIdentityString(URL url) {
        return url == null ? "" : url.toIdentityString();
    }

    /**
     * 将字符串转换为 URL 对象
     *
     * @param url URL 字符串
     * @return URL 对象，如果字符串为空白则返回 null
     */
    public static URL toURL(String url) {
        return StringUtils.isBlank(url) ? null : URL.valueOf(url);
    }

    /**
     * 注册 XStream 转换器
     * 用于序列化和反序列化 DubboSampleResult 对象
     */
    public static void registerConverters() {
        XStream xStream = getXStream("JTLSAVER");
        xStream.registerConverter(new DubboSampleResultConverter(xStream.getMapper()));
        xStream.alias("dubboSample", DubboSampleResult.class);
    }

    /**
     * 通过反射获取 SaveService 中的 XStream 实例
     *
     * @param name XStream 实例的字段名
     * @return XStream 实例
     * @throws RuntimeException 如果反射操作失败
     */
    public static XStream getXStream(String name) {
        try {
            Field jtlSaverField = SaveService.class.getDeclaredField(name);
            jtlSaverField.setAccessible(true);
            return (XStream) jtlSaverField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to register DubboSampleResultConverter", e);
        }

    }
}
