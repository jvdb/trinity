package org.infuse.fiddle;

import java.awt.Component;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class SentenceViewTreeCellRenderer extends DefaultTreeCellRenderer {
    
    private final HashSet<Integer> _structures;
    private final HashSet<Integer> _fields;
    
    public SentenceViewTreeCellRenderer() {
        _structures = new HashSet<Integer>();
        _fields = new HashSet<Integer>();
    }
    
    public void addStructureSelection(int row) {
        _structures.add(row);
    }
    
    public void addFieldSelection(int row) {
        _fields.add(row);
    }
    
    public void clearSelection() {
        _structures.clear();
        _fields.clear();
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JComponent c = (JComponent)super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        c.setOpaque(true);
        c.setForeground(MainFrame.FG);
        if (_structures.contains(row)) {
            c.setBackground(MainFrame.BG_SEL1);
        } else if (_fields.contains(row)) {
            c.setBackground(MainFrame.BG_SEL2);
        } else {
            c.setBackground(MainFrame.BG);
        }
        return c;
    }
}
