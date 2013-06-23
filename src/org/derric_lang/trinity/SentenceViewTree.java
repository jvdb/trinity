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

import java.awt.Font;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;

@SuppressWarnings("serial")
public class SentenceViewTree extends JTree {
    
    public SentenceViewTree() {
        setFont(new Font("Monospaced", getFont().getStyle(), getFont().getSize()));
        setBackground(MainFrame.BG);
        setForeground(MainFrame.FG);
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
    
    public void setViewable(Selection selection) {
        if (selection != null) {
            makeVisible(((SentenceViewTreeModel)getModel()).getPathToRoot(selection.field == null ? selection.structure : selection.field));
        }
    }
    
}
