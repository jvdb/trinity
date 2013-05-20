package org.infuse.fiddle;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class HexViewTableCellRenderer extends DefaultTableCellRenderer {
    
    private final List<Selection> _structures;
    private final List<Selection> _fields;
    
    public HexViewTableCellRenderer() {
        _structures = new ArrayList<Selection>();
        _fields = new ArrayList<Selection>();
    }
    
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
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setForeground(MainFrame.FG);
        if (column < 1 || column > HexViewTableModel.WIDTH) {
            c.setBackground(MainFrame.BG);
            return c;
        }
        int offset = (row * HexViewTableModel.WIDTH) + (column - 1);
        c.setBackground(MainFrame.BG);
        for (Selection s : _structures) {
            if (s.in(offset)) {
                c.setBackground(MainFrame.BG_SEL1);
            }
        }
        for (Selection s : _fields) {
            if (s.in(offset)) {
                c.setBackground(MainFrame.BG_SEL2);
            }
        }
        return c;
    }
    
    public void addStructureHighlight(int offset, int length) {
        _structures.add(new Selection(offset, length));
    }
    
    public void addFieldHighlight(int offset, int length) {
        _fields.add(new Selection(offset, length));
    }
    
    public void clearHighlights() {
        _structures.clear();
        _fields.clear();
    }
    
}
