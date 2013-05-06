package org.infuse.fiddle;

import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class SentenceViewTreeCellRenderer extends DefaultTreeCellRenderer {
    
    private final HashSet<Integer> _selections;
    
    public SentenceViewTreeCellRenderer() {
        _selections = new HashSet<Integer>();
    }
    
    public void addSelection(int row) {
        _selections.add(row);
    }
    
    public void clearSelection() {
        _selections.clear();
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JComponent c = (JComponent)super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        c.setOpaque(true);
        c.setForeground(Color.BLACK);
        if (_selections.contains(row)) {
            c.setBackground(Color.LIGHT_GRAY);
        } else {
            c.setBackground(Color.WHITE);
        }
        return c;
    }
}
