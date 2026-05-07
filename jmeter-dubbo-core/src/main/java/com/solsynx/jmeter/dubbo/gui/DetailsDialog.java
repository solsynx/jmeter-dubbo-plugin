package com.solsynx.jmeter.dubbo.gui;

import com.solsynx.jmeter.dubbo.utils.JMeterUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 参数详情编辑对话框
 */
public class DetailsDialog extends JDialog implements ActionListener {
    private static final String OK_COMMAND = "ok";
    private static final String CANCEL_COMMAND = "cancel";
    private final Argument argument;
    private final int rowIndex;
    private final String[] headers;
    private JTextField nameField;
    private JSyntaxTextArea valueField;
    private JTextField descriptionArea;
    private boolean confirmed = false;

    /**
     * 构造函数
     *
     * @param parent   父窗口
     * @param arg      要编辑的参数对象
     * @param rowIndex 参数在表格中的行索引
     * @param headers  表格列标题数组
     */
    public DetailsDialog(Frame parent, Argument arg, int rowIndex, String[] headers) {
        super(parent, JMeterUtils.getResString("param_detail"), true);
        this.argument = arg;
        this.rowIndex = rowIndex;
        this.headers = headers;
        init();
    }

    /**
     * 初始化对话框界面
     */
    private void init() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

// 创建输入面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

// ===================== 第一行：参数名称 固定高度 =====================
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        inputPanel.add(new JLabel(headers[0] + ":"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nameField = new JTextField(argument.getName());
        inputPanel.add(nameField, gbc);

// ===================== 第二行：描述 固定高度 =====================
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        inputPanel.add(new JLabel(headers[2] + ":"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        descriptionArea = new JTextField(argument.getDescription());
        inputPanel.add(descriptionArea, gbc);

// ===================== 第三行：参数值 占满剩余高度 =====================
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        inputPanel.add(new JLabel(headers[1] + ":"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        valueField = JSyntaxTextArea.getInstance(2, 50);
        valueField.setLineWrap(true);
        valueField.setWrapStyleWord(true);
// 1. 先设置文本
        valueField.setInitialText(argument.getValue());
// 2. 再识别语言 + 设置语言（触发高亮）
        String content = argument.getValue() == null ? "" : argument.getValue().trim();
        String lang = autoDetectLanguage(content);
        valueField.setLanguage(lang);

// 显示行号
        RTextScrollPane sp = new RTextScrollPane(valueField);
        sp.setLineNumbersEnabled(true);
        inputPanel.add(sp, gbc);

// ===================== 按钮面板 =====================
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton(JMeterUtils.getResString("mapArguments.ok"));
        okButton.setActionCommand(OK_COMMAND);
        okButton.addActionListener(this);
        getRootPane().setDefaultButton(okButton);
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton(JMeterUtils.getResString("mapArguments.cancel"));
        cancelButton.setActionCommand(CANCEL_COMMAND);
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

// 主布局
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

// 窗口属性
        setSize(500, 300);
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(getParent());
        setResizable(true);
    }

    /**
     * 自动识别内容类型：json / xml / text
     */
    private String autoDetectLanguage(String content) {
        if (content == null || StringUtils.isBlank(content)) {
            return "text";
        }
        String trim = content.trim();
        if (trim.startsWith("{") || trim.startsWith("[")) {
            return "json";
        }
        if (trim.startsWith("<")) {
            return "xml";
        }
        return "text";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (OK_COMMAND.equals(command)) {
            // 更新参数对象
            valueField.transferFocus();
            argument.setName(nameField.getText());
            argument.setValue(valueField.getText());
            argument.setDescription(descriptionArea.getText());
            confirmed = true;
            dispose();
        } else if (CANCEL_COMMAND.equals(command)) {
            confirmed = false;
            dispose();
        }
    }

    /**
     * 检查对话框是否确认保存
     *
     * @return 如果用户点击了确定按钮则返回true，否则返回false
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * 获取编辑后的参数对象
     *
     * @return 编辑后的参数对象
     */
    public Argument getArgument() {
        return argument;
    }
}
