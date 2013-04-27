package org.infuse.fiddle;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class HighlightTableCellRenderer extends DefaultTableCellRenderer {
    
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
    
    private int _offset = 0;
    private int _length = 0;
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            c.setBackground(Color.DARK_GRAY);
            return c;
        }
        if (column < 1 || column > HexViewTableModel.WIDTH) {
            c.setBackground(Color.WHITE);
            return c;
        }
        int offset = (row * HexViewTableModel.WIDTH) + (column - 1);
        if (offset >= _offset && offset < (_offset + _length)) {
            c.setBackground(Color.LIGHT_GRAY);
        } else {
            c.setBackground(Color.WHITE);
        }
        return c;
    }
    
    public void setSelection(int offset, int length) {
        _offset = offset;
        _length = length;
    }
}
