package org.infuse.fiddle;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SentenceViewTableModel extends AbstractTableModel {
    
    private Sentence _sentence;
    
    public SentenceViewTableModel() {
        _sentence = null;
    }
    
    public void setSentence(Sentence sentence) {
        _sentence = sentence;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        if (_sentence == null) {
            return 0;
        } else {
            return _sentence.matches.size();
        }
    }

    @Override
    public int getColumnCount() {
        return 1;
    }
    
    @Override
    public String getColumnName(int column) {
        return "StructureName";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex > 0) { return null; }
        if (rowIndex >= _sentence.matches.size()) { return null; }
        return _sentence.matches.get(rowIndex).name;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

}
