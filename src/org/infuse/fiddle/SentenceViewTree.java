package org.infuse.fiddle;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;

@SuppressWarnings("serial")
public class SentenceViewTree extends JTree {
    
    public SentenceViewTree() {
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setDragEnabled(false);
        setEditable(false);
        setRootVisible(false);
        setCellRenderer(new SentenceViewTreeCellRenderer());
    }
    
    public void addHighlight(StructureMatch match) {
        SentenceViewTreeModel model = (SentenceViewTreeModel)getModel();
        ((SentenceViewTreeCellRenderer)getCellRenderer()).addStructureSelection(getRowForPath(model.getPathToRoot(match)));
    }
    
    public void addHighlight(FieldMatch match) {
        SentenceViewTreeModel model = (SentenceViewTreeModel)getModel();
        ((SentenceViewTreeCellRenderer)getCellRenderer()).addFieldSelection(getRowForPath(model.getPathToRoot(match)));
    }
    
    public void clearHighlights() {
        ((SentenceViewTreeCellRenderer)getCellRenderer()).clearSelection();
    }

}
