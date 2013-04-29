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
        return 1;
    }

    @Override
    public int getColumnCount() {
        if (_sentence == null) {
            return 0;
        } else {
            return _sentence.matches.size();
        }
    }
    
    @Override
    public String getColumnName(int column) {
        return "#" + Integer.toString(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > 0) { return null; }
        if (columnIndex >= _sentence.matches.size()) { return null; }
        return _sentence.matches.get(columnIndex).name;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

}
