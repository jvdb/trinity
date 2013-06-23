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
