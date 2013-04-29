package org.infuse.fiddle;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class SentenceViewTable extends JTable {
    
    public SentenceViewTable() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setModel(new SentenceViewTableModel());
        setFillsViewportHeight(true);
        setCellSelectionEnabled(true);
    }

}
