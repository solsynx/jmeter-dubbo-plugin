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

package com.solsynx.jmeter.dubbo.gui;

import com.solsynx.jmeter.dubbo.config.RegistryTypeManager;
import com.solsynx.jmeter.dubbo.config.RegistryTypeProvider;
import com.solsynx.jmeter.dubbo.sampler.DubboSampler;
import com.solsynx.jmeter.dubbo.utils.JMeterUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledChoice;
import org.apache.jorphan.gui.JLabeledTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * DubboSampler 的 GUI 类
 * 该类提供了 JMeter 中 Dubbo 采样器的图形用户界面
 *
 * @author Solsynx&lt;xy.0520@hotmail.com&gt;
 * @version 0.0.1
 */
public class DubboSamplerGui extends AbstractSamplerGui implements ActionListener, ItemListener {

    // Registry configuration fields
    private JLabeledChoice registryTypeChoice;
    private JLabeledTextField registryAddressField;
    private JLabeledTextField registryGroupField;
    private JLabeledTextField registryTimeoutField;
    private JLabeledTextField registryUsernameField;
    private JLabeledTextField registryPasswordField;

    // Service configuration fields
    private JLabeledTextField directUrlField;
    private JLabeledTextField serviceGroupField;
    private JLabeledTextField interfaceNameField;
    private JLabeledTextField methodNameField;
    private JLabeledTextField serviceTimeoutField;

    // Attachment panel
    private ArgumentsPanel attachmentPanel;
    private Arguments attachments;

    // Parameters panel
    private ArgumentsPanel parametersPanel;
    private Arguments parameters;

    /**
     * 构造函数，创建一个新的 DubboSamplerGui 实例
     */
    public DubboSamplerGui() {
        init();
    }

    /**
     * 获取静态标签
     * @return 静态标签字符串
     */
    @Override
    public String getStaticLabel() {
        return JMeterUtils.getResString("displayName");
    }

    /**
     * 获取标签资源键
     * @return 标签资源键
     */
    @Override
    public String getLabelResource() {
        return "displayName";
    }

    /**
     * 创建测试元素
     * @return 创建的测试元素
     */
    @Override
    public TestElement createTestElement() {
        DubboSampler sampler = new DubboSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    /**
     * 修改测试元素
     * @param element 要修改的测试元素
     */
    @Override
    public void modifyTestElement(TestElement element) {
        configureTestElement(element);

        if (element instanceof DubboSampler) {
            DubboSampler sampler = (DubboSampler) element;

            // Registry configuration
            sampler.setRegistryType(registryTypeChoice.getText());
            sampler.setRegistryAddress(registryAddressField.getText());
            sampler.setRegistryGroup(registryGroupField.getText());
            sampler.setRegistryTimeout(registryTimeoutField.getText());
            sampler.setRegistryUsername(registryUsernameField.getText());
            sampler.setRegistryPassword(registryPasswordField.getText());

            // Service configuration
            sampler.setDirectUrl(directUrlField.getText());
            sampler.setServiceGroup(serviceGroupField.getText());
            sampler.setInterfaceName(interfaceNameField.getText());
            sampler.setMethodName(methodNameField.getText());
            sampler.setServiceTimeout(serviceTimeoutField.getText());

            // Attachment configuration
            attachmentPanel.modifyTestElement(attachments);
            sampler.setAttachment(attachments);

            // Parameters configuration
            parametersPanel.modifyTestElement(parameters);
            sampler.setParameters(parameters);
        }
    }

    /**
     * 配置测试元素
     * @param element 要配置的测试元素
     */
    @Override
    public void configure(TestElement element) {
        super.configure(element);

        if (element instanceof DubboSampler) {
            DubboSampler sampler = (DubboSampler) element;
            // Registry configuration
            registryTypeChoice.setText(sampler.getRegistryType());
            registryAddressField.setText(sampler.getRegistryAddress());
            registryGroupField.setText(sampler.getRegistryGroup());
            registryTimeoutField.setText(sampler.getRegistryTimeout());
            registryUsernameField.setText(sampler.getRegistryUsername());
            registryPasswordField.setText(sampler.getRegistryPassword());

            // Service configuration
            directUrlField.setText(sampler.getDirectUrl());
            serviceGroupField.setText(sampler.getServiceGroup());
            interfaceNameField.setText(sampler.getInterfaceName());
            methodNameField.setText(sampler.getMethodName());
            serviceTimeoutField.setText(sampler.getServiceTimeout());

            // Attachment configuration
            Arguments args = sampler.getAttachment();
            if (args != null) {
                attachmentPanel.configure(args);
            }

            // Parameters configuration
            Arguments params = sampler.getParameters();
            if (params != null) {
                parametersPanel.configure(params);
            }
        }
    }

    /**
     * 清空 GUI 界面
     */
    @Override
    public void clearGui() {
        super.clearGui();

        // Registry configuration defaults
        String[] supportedTypes = RegistryTypeManager.getSupportedTypes().toArray(new String[0]);
        if (supportedTypes.length > 0) {
            String defaultType = supportedTypes[0];
            registryTypeChoice.setText(defaultType);

            // Set default values based on registry type
            updateRegistryDefaults(defaultType);
        }

        registryUsernameField.setText("");
        registryPasswordField.setText("");

        // Service configuration defaults
        directUrlField.setText("");
        serviceGroupField.setText("");
        interfaceNameField.setText("");
        methodNameField.setText("");
        serviceTimeoutField.setText("1000");

        // Attachment configuration defaults
        attachmentPanel.clearGui();

        // Parameters configuration defaults
        parametersPanel.clearGui();
    }

    /**
     * 根据选择的注册中心类型更新默认值
     *
     * @param type 注册中心类型
     */
    private void updateRegistryDefaults(String type) {
        RegistryTypeProvider.RegistryDefaults defaults = RegistryTypeManager.getDefaults(type);
        if (defaults != null) {
            registryAddressField.setText(defaults.getDefaultAddress());
            registryGroupField.setText(defaults.getDefaultGroup());
            registryTimeoutField.setText(defaults.getDefaultTimeout());
        } else {
            // Clear fields if no defaults found
            registryAddressField.setText("");
            registryGroupField.setText("");
            registryTimeoutField.setText("");
        }
    }

    /**
     * 初始化 GUI 组件
     */
    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        JPanel mainPanel = new VerticalPanel();
        mainPanel.add(createRegistryConfigPanel());
        mainPanel.add(createServiceConfigPanel());
        mainPanel.add(createParametersPanel());
        mainPanel.add(createAttachmentPanel());

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * 创建注册中心配置面板
     * @return 注册中心配置面板
     */
    private JPanel createRegistryConfigPanel() {
        JPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("registry_config")));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // First row: registryType and registryAddress
        JPanel registryTypeAddressPanel = new HorizontalPanel();
        // Get supported registry types from configuration
        String[] registryTypes = RegistryTypeManager.getSupportedTypes().toArray(new String[0]);
        registryTypeChoice = new JLabeledChoice(JMeterUtils.getResString("registryType.displayName"), registryTypes);
        registryAddressField = JMeterUtils.createJLabeledTextField("registryAddress.displayName", 50);

        registryTypeAddressPanel.add(registryTypeChoice);
        registryTypeAddressPanel.add(registryAddressField);

        // Second row: registryTimeout, registryUsername, registryPassword
        JPanel registryDetailsPanel = new HorizontalPanel();
        registryGroupField = JMeterUtils.createJLabeledTextField("registryGroup.displayName", 10);
        registryTimeoutField = JMeterUtils.createJLabeledTextField("registryTimeout.displayName", 10);
        registryUsernameField = JMeterUtils.createJLabeledTextField("registryUsername.displayName", 12);
        registryPasswordField = JMeterUtils.createJLabeledTextField("registryPassword.displayName", 12);

        registryDetailsPanel.add(registryGroupField);
        registryDetailsPanel.add(registryTimeoutField);
        registryDetailsPanel.add(registryUsernameField);
        registryDetailsPanel.add(registryPasswordField);

        panel.add(registryTypeAddressPanel);
        panel.add(registryDetailsPanel);
        panel.setMinimumSize(panel.getPreferredSize());
        return panel;
    }

    /**
     * 创建服务配置面板
     * @return 服务配置面板
     */
    private JPanel createServiceConfigPanel() {
        JPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("service_config")));

        JPanel urlPanel = new HorizontalPanel();
        // Interface and method panel - interface takes 2/3, method takes 1/3
        JPanel interfacePanel = new HorizontalPanel();
        interfaceNameField = JMeterUtils.createJLabeledTextField("interfaceName.displayName", 40);
        methodNameField = JMeterUtils.createJLabeledTextField("methodName.displayName", 16);

        interfacePanel.add(interfaceNameField);
        interfacePanel.add(methodNameField);

        // Service group and timeout panel - each takes 1/3, with 1/3 empty space

        serviceGroupField = JMeterUtils.createJLabeledTextField("serviceGroup.displayName", 12);
        serviceTimeoutField = JMeterUtils.createJLabeledTextField("serviceTimeout.displayName", 12);

        interfacePanel.add(serviceGroupField);
        interfacePanel.add(serviceTimeoutField);
        // Add an empty space component to fill the remaining 1/3

        directUrlField = JMeterUtils.createJLabeledTextField("directUrl.displayName", 60);
        urlPanel.add(directUrlField);

        panel.add(interfacePanel);
        panel.add(urlPanel);

        return panel;
    }

    /**
     * 创建附件配置面板
     * @return 附件配置面板
     */
    private JPanel createAttachmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("attachment.displayName")));

        attachments = new Arguments();
        attachmentPanel = new ArgumentsPanel(JMeterUtils.getResString("attachment.displayName"), null, true, false,
                                             null,
                                             false, null);

        // Control the height of the ArgumentsPanel
        Dimension prefSize = new Dimension(attachmentPanel.getPreferredSize().width, 200);
        attachmentPanel.setPreferredSize(prefSize);

        panel.add(attachmentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 创建参数配置面板
     * @return 参数配置面板
     */
    private JPanel createParametersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("parameters.displayName")));

        parameters = new Arguments();
        parametersPanel = new ArgumentsPanel(JMeterUtils.getResString("parameters.displayName"), null, true, false,
                                             null,
                                             false, null);

        // Control the height of the ArgumentsPanel
        Dimension prefSize = new Dimension(parametersPanel.getPreferredSize().width, 250);
        parametersPanel.setPreferredSize(prefSize);
        panel.add(parametersPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * 处理动作事件
     * @param e 动作事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // No action needed as ArgumentsPanel handles its own events
    }

    /**
     * 处理选项状态改变事件
     * @param e 选项事件
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == registryTypeChoice) {
            updateRegistryDefaults(registryTypeChoice.getText());
        }
    }
}
