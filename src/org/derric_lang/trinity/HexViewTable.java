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
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;

@SuppressWarnings("serial")
public class HexViewTable extends JTable {
    
    public final HexViewTableCellRenderer _renderer;
    
    public HexViewTable() throws IOException {
        setFont(new Font("Monospaced", getFont().getStyle(), getFont().getSize()));
        setBackground(MainFrame.BG);
        setForeground(MainFrame.FG);
        getTableHeader().setReorderingAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setModel(new HexViewTableModel(null));
        TableColumnModel cm = getColumnModel();
        fixWidth(cm, 0, 61);
        for (int i = 1; i < 17; i++) {
            fixWidth(cm, i, 21);
        }
        cm.getColumn(17).setMinWidth(115);
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        setFillsViewportHeight(true);
        setCellSelectionEnabled(true);
        _renderer = new HexViewTableCellRenderer();
    }
    
    private void fixWidth(TableColumnModel cm, int index, int width) {
        cm.getColumn(index).setMinWidth(width);
        cm.getColumn(index).setPreferredWidth(width);
        cm.getColumn(index).setMaxWidth(width);
    }
    
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return _renderer;
    }
    
    public void clearHighlights() {
        _renderer.clearHighlights();
    }
    
    public void addHighlight(StructureMatch structure) {
        _renderer.addStructureHighlight(structure.inputLocation.getOffset(), structure.inputLocation.getLength());
    }
    
    public void addHighlight(FieldMatch field) {
        _renderer.addFieldHighlight(field.inputLocation.getOffset(), field.inputLocation.getLength());
    }
    
    public void addCoverage(Sentence sentence) {
        for (StructureMatch s : sentence.matches) {
            _renderer.addCoverage(s.inputLocation.getOffset(), s.inputLocation.getLength());
        }
    }
    
    public void setViewable(Selection selection) {
        if (selection != null) {
            int offset = selection.field == null ? selection.structure.inputLocation.getOffset() : selection.field.inputLocation.getOffset();
            int row = offset / HexViewTableModel.WIDTH;
            int col = (offset % HexViewTableModel.WIDTH) + 1;
            scrollRectToVisible(getCellRect(row, col, true));
        }
    }
    
}
