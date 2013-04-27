package org.infuse.fiddle;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class HighlightTableCellRenderer extends DefaultTableCellRenderer {
    
    public HighlightTableCellRenderer() {
        _selections = new ArrayList<Selection>();
    }
    
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
    
    private class Selection {
        
        public final int offset;
        public final int length;
        
        public Selection(int offset, int length) {
            this.offset = offset;
            this.length = length;
        }
        
        public boolean in(int offset) {
            return offset >= this.offset && (offset < (this.offset + this.length));
        }
        
    }
    
    private final List<Selection> _selections;
    
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
        c.setBackground(Color.WHITE);
        for (Selection s : _selections) {
            if (s.in(offset)) {
                c.setBackground(Color.LIGHT_GRAY);
                return c;
            }
        }
        return c;
    }
    
    public void addHighlight(int offset, int length) {
        _selections.add(new Selection(offset, length));
    }
    
    public void clearHighlights() {
        _selections.clear();
    }
    
}
