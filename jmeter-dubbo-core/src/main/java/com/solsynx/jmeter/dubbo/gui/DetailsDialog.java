package com.solsynx.jmeter.dubbo.gui;

import com.solsynx.jmeter.dubbo.utils.JMeterUtils;
import org.apache.jmeter.config.Argument;

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

    private JTextField nameField;
    private JTextField valueField;
    private JTextArea descriptionArea;
    private final Argument argument;
    private boolean confirmed = false;
    private final int rowIndex;
    private final String[] headers;

    /**
     * 构造函数
     *
     * @param parent  父窗口
     * @param arg     要编辑的参数对象
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

        // 参数名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel(headers[0] + ":"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nameField = new JTextField(argument.getName());
        inputPanel.add(nameField, gbc);

        // 参数值
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(new JLabel(headers[1] + ":"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        valueField = new JTextField(argument.getValue());
        inputPanel.add(valueField, gbc);

        // 描述
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(new JLabel(headers[2] + ":"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        descriptionArea = new JTextArea(argument.getDescription());
        descriptionArea.setRows(3);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton(JMeterUtils.getResString("mapArguments.ok"));
        okButton.setActionCommand(OK_COMMAND);
        okButton.addActionListener(this);
        getRootPane().setDefaultButton(okButton); // 设置回车键默认点击确定按钮
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton(JMeterUtils.getResString("mapArguments.cancel"));
        cancelButton.setActionCommand(CANCEL_COMMAND);
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        // 主面板
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 设置对话框属性
        setSize(400, 200);
        setMinimumSize(new Dimension(400, 200));
        setLocationRelativeTo(getParent());
        setResizable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (OK_COMMAND.equals(command)) {
            // 更新参数对象
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
