/* Copyright 2013 Netherlands Forensic Institute and
                       Centrum Wiskunde & Informatica

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.derric_lang.trinity;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;

public class SentenceViewTreeModel implements TreeModel {

    private String _fileName;
    private List<StructureMatch> _matches;
    
    public SentenceViewTreeModel(String fileName, List<StructureMatch> matches) {
        _fileName = fileName;
        _matches = new ArrayList<StructureMatch>();
        for (StructureMatch m : matches) {
            _matches.add(m);
        }
    }

    @Override
    public Object getRoot() {
        return _fileName;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent == _fileName && index >= 0 && index < _matches.size()) {
            return _matches.get(index);
        } else if (parent instanceof StructureMatch && index >= 0 && index < ((StructureMatch)parent).fields.size()) {
            return ((StructureMatch)parent).fields.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent == _fileName) {
            return _matches.size();
        } else if (parent instanceof StructureMatch && _matches.contains((StructureMatch)parent)) {
            return _matches.get(_matches.indexOf((StructureMatch)parent)).fields.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        if (node == _fileName) {
            return _matches.size() == 0;
        } else if (node instanceof StructureMatch) {
            return ((StructureMatch)node).fields.size() == 0;
        } else {
            return true;
        }
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == _fileName) {
            return _matches.indexOf(child);
        } else if (parent instanceof StructureMatch && child instanceof FieldMatch && _matches.contains((StructureMatch)parent) && _matches.get(_matches.indexOf((StructureMatch)parent)).fields.contains((FieldMatch)child)) {
            return _matches.get(_matches.indexOf((StructureMatch)parent)).fields.indexOf((FieldMatch)child);
        } else {
            return -1;
        }
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }
    
    public TreePath getPathToRoot(Object match) {
        for (StructureMatch n : _matches) {
            if (n == match) {
                return new TreePath(new Object[] { _fileName, n });
            }
            for (FieldMatch f : n.fields) {
                if (f == match) {
                    return new TreePath(new Object[] { _fileName, n, f });
                }
            }
        }
        return null;
    }

}
