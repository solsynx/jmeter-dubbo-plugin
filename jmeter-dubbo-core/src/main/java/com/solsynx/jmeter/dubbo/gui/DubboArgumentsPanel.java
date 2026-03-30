package com.solsynx.jmeter.dubbo.gui;

import com.solsynx.jmeter.dubbo.utils.JMeterUtils;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.gui.util.HeaderAsPropertyRenderer;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * 自定义参数面板，隐藏标签，保留按钮功能
 * 使用 JMeter 自带的 Argument 类
 */
public class DubboArgumentsPanel extends JPanel implements ActionListener {
    private static final Logger log = LoggerFactory.getLogger(DubboArgumentsPanel.class);
    private static final long serialVersionUID = 241L;

    private static final String DELETE_COMMAND = "delete";
    private static final String ADD_COMMAND = "add";
    private static final String UP_COMMAND = "up";
    private static final String DOWN_COMMAND = "down";
    private static final String DETAILS_COMMAND = "details";
    private static final String NAME = "mapArguments.displayName";

    private JTable table;
    private JScrollPane tableScroll;
    private JButton add;
    private JButton delete;
    private JButton up;
    private JButton down;
    private JButton details;
    private ObjectTableModel model;
    private final String[] headers;

    /**
     * 构造函数，使用国际化资源
     * @param headers 表格列标题数组
     */
    public DubboArgumentsPanel(String[] headers) {
        super();
        this.headers = Arrays.stream(headers).map(JMeterUtils::getResString).toArray(String[]::new);
        init();
    }

    /**
     * 初始化界面组件
     */
    private void init() {
        setLayout(new BorderLayout());
        setName(NAME);

        // 创建表格模型，使用 JMeter 的 Argument 类
        model = new ObjectTableModel(this.headers,
                                     Argument.class,
                                     new Functor[] {
                                         new Functor("getName"),
                                         new Functor("getValue"),
                                         new Functor("getDescription")
                                     },
                                     new Functor[] {
                                         new Functor("setName"),
                                         new Functor("setValue"),
                                         new Functor("setDescription")
                                     },
                                     new Class[] {String.class, String.class, String.class}
        );

        // 创建表格
        table = new JTable(model);
        table.getTableHeader().setDefaultRenderer(new HeaderAsPropertyRenderer());

        // 设置表格列宽 - 根据实际列数动态调整
        if (headers.length >= 1) {
            table.getColumnModel().getColumn(0).setPreferredWidth(150);
            table.getColumnModel().getColumn(0).setMinWidth(80);
        }
        if (headers.length >= 2) {
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(1).setMinWidth(100);
        }
        if (headers.length >= 3) {
            table.getColumnModel().getColumn(2).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setMinWidth(80);
        }

        tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(300, 70));

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // 添加按钮
        add = new JButton(JMeterUtils.getResString("mapArguments.add"));
        add.addActionListener(this);
        add.setActionCommand(ADD_COMMAND);
        buttonPanel.add(add);
        buttonPanel.add(Box.createVerticalStrut(5));

        // 删除按钮
        delete = new JButton(JMeterUtils.getResString("mapArguments.delete"));
        delete.addActionListener(this);
        delete.setActionCommand(DELETE_COMMAND);
        buttonPanel.add(delete);
        buttonPanel.add(Box.createVerticalStrut(5));

        // 详情按钮
        details = new JButton(JMeterUtils.getResString("mapArguments.details"));
        details.addActionListener(this);
        details.setActionCommand(DETAILS_COMMAND);
        buttonPanel.add(details);
        buttonPanel.add(Box.createVerticalStrut(5));

        // 上移按钮
        up = new JButton(JMeterUtils.getResString("mapArguments.up"));
        up.addActionListener(this);
        up.setActionCommand(UP_COMMAND);
        buttonPanel.add(up);
        buttonPanel.add(Box.createVerticalStrut(5));

        // 下移按钮
        down = new JButton(JMeterUtils.getResString("mapArguments.down"));
        down.addActionListener(this);
        down.setActionCommand(DOWN_COMMAND);
        buttonPanel.add(down);

        // 组装主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tableScroll, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case ADD_COMMAND:
                model.addRow(new Argument());
                table.editCellAt(model.getRowCount() - 1, 0);
                break;
            case DELETE_COMMAND:
                int rowSelected = table.getSelectedRow();
                if (rowSelected >= 0) {
                    model.removeRow(rowSelected);
                }
                break;
            case UP_COMMAND:
                moveRow(-1);
                break;
            case DOWN_COMMAND:
                moveRow(1);
                break;
            case DETAILS_COMMAND:
                showDetailsDialog();
                break;
        }
    }

    /**
     * 显示详情对话框
     */
    private void showDetailsDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                                          JMeterUtils.getResString("select_param_to_edit"),
                                          JMeterUtils.getResString("warning"),
                                          JOptionPane.WARNING_MESSAGE);
            return;
        }

        Argument selectedArg = extractArgumentFromRow(selectedRow);
        if (selectedArg == null) {
            JOptionPane.showMessageDialog(this,
                                          "Error retrieving parameter data",
                                          JMeterUtils.getResString("error"),
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建详情编辑对话框
        DetailsDialog dialog = new DetailsDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                                 selectedArg, selectedRow, headers);
        dialog.setVisible(true);

        // 更新表格中的数据
        if (dialog.isConfirmed()) {
            model.setValueAt(dialog.getArgument(), selectedRow, 0);
        }
    }

    /**
     * 从表格行中提取 Argument 对象
     * @param rowIndex 行索引
     * @return Argument 对象，如果提取失败则返回 null
     */
    private Argument extractArgumentFromRow(int rowIndex) {
        try {
            Object value = model.getValueAt(rowIndex, 0);
            if (value instanceof Argument) {
                return (Argument) value;
            }

            // 如果不是 Argument 类型，则从各列构建新的 Argument
            String name = getStringValueAt(rowIndex, 0);
            String val = getStringValueAt(rowIndex, 1);
            String desc = getStringValueAt(rowIndex, 2);

            return new Argument(name, val, "", desc);
        } catch (Exception ex) {
            log.error("Failed to extract argument from row {}", rowIndex, ex);
            return null;
        }
    }

    /**
     * 安全获取指定位置的字符串值
     * @param row 行索引
     * @param column 列索引
     * @return 字符串值，null 或空时返回空字符串
     */
    private String getStringValueAt(int row, int column) {
        try {
            Object value = model.getValueAt(row, column);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 移动行位置
     *
     * @param direction 移动方向，-1为上移，1为下移
     */
    private void moveRow(int direction) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int targetRow = selectedRow + direction;
        if (targetRow < 0 || targetRow >= model.getRowCount()) {
            return;
        }

        // 获取当前行和目标行的 Argument 对象
        Argument currentArg;
        Argument targetArg;

        try {
            Object currentValue = model.getValueAt(selectedRow, 0);
            Object targetValue = model.getValueAt(targetRow, 0);

            if (currentValue instanceof Argument) {
                currentArg = (Argument) currentValue;
            } else {
                currentArg = new Argument("", ""); // 使用正确的构造函数
            }

            if (targetValue instanceof Argument) {
                targetArg = (Argument) targetValue;
            } else {
                targetArg = new Argument("", ""); // 使用正确的构造函数
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error moving rows: " + ex.getMessage(),
                JMeterUtils.getResString("error"),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 交换两行数据
        model.setValueAt(targetArg, selectedRow, 0);
        model.setValueAt(currentArg, targetRow, 0);

        // 重新选择目标行
        table.setRowSelectionInterval(targetRow, targetRow);
    }

    /**
     * 将参数数据设置到面板
     *
     * @param arguments 参数集合
     */
    public void setArguments(Arguments arguments) {
        model.clearData();
        if (arguments != null) {
            CollectionProperty props = arguments.getArguments();
            for (int i = 0; i < props.size(); i++) {
                Argument argument = (Argument) props.get(i).getObjectValue();
                String name = argument.getName();
                String value = argument.getValue();
                String metadata = argument.getMetaData() != null ? argument.getMetaData() : "";
                String description = argument.getDescription() != null ? argument.getDescription() : "";
                Argument arg = new Argument(name, value, metadata, description);
                model.addRow(arg);
            }
        }
    }

    /**
     * 从面板获取参数数据
     *
     * @return 参数集合
     */
    public Arguments getArguments() {
        Arguments arguments = new Arguments();
        int errorCount = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                String name = getStringValueAt(i, 0);
                String val = getStringValueAt(i, 1);
                String desc = getStringValueAt(i, 2);

                Argument arg = new Argument(name, val, "", desc);
                arguments.addArgument(arg);
            } catch (Exception ex) {
                log.error("Failed to get argument at index {}, skipping", i, ex);
                errorCount++;
            }
        }

        if (errorCount > 0) {
            log.warn("Total {} arguments failed to retrieve", errorCount);
        }

        return arguments;
    }

    /**
     * 清空所有参数
     */
    public void clearData() {
        model.clearData();
    }

}
