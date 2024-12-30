package com.operating.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

/**
 * 表格工具类，用于动态创建表格列
 */
public class TableViewUtils {

    /**
     * 创建表格列
     */
    public static <T> TableColumn<T, String> createTableColumn(String title, String property, double width) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(
                        cellData.getValue().getClass().getMethod("get" + capitalize(property)).invoke(cellData.getValue()).toString()
                );
            } catch (Exception e) {
                e.printStackTrace();
                return new SimpleStringProperty("");
            }
        });
        column.setPrefWidth(width);
        return column;
    }

    /**
     * 首字母大写
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
