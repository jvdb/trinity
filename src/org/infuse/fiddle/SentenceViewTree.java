package org.infuse.fiddle;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.derric_lang.validator.interpreter.StructureMatch;

@SuppressWarnings("serial")
public class SentenceViewTree extends JTree {
    
    public SentenceViewTree() {
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setDragEnabled(false);
        setEditable(false);
        setRootVisible(false);
    }
    
    public void addToSelection(StructureMatch[] matches) {
        List<TreePath> toSelect = new ArrayList<TreePath>();
        SentenceViewTreeModel model = (SentenceViewTreeModel)getModel();
        for (StructureMatch m : matches) {
            toSelect.add(model.getPathToRoot(m));
        }
        TreePath[] selection = getSelectionPaths();
        if (selection != null) {
            for (TreePath p : selection) {
                toSelect.remove(p);
            }
        }
        for (TreePath p : toSelect) {
            addSelectionPath(p);
        }
    }

}
