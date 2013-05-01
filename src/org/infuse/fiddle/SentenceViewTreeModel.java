package org.infuse.fiddle;

import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.derric_lang.validator.interpreter.StructureMatch;

public class SentenceViewTreeModel implements TreeModel {

    private String _fileName;
    private List<StructureMatch> _matches;
    
    public SentenceViewTreeModel(String fileName, List<StructureMatch> matches) {
        _fileName = fileName;
        _matches = matches;
    }

    @Override
    public Object getRoot() {
        return _fileName;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent != _fileName) { return null; }
        if (index < 0 || index >= _matches.size()) { return null; }
        return new Node(_matches.get(index));
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent == _fileName) {
            return _matches.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        if (node == _fileName) { return _matches.size() == 0; }
        return true;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent != _fileName) { return -1; }
        for (StructureMatch m : _matches) {
            if (((Node)child).match == m) {
                return _matches.indexOf(m);
            }
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }

}
