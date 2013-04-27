package org.infuse.fiddle;

import java.awt.Font;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class HexViewTable extends JTable {
    
    public final HighlightTableCellRenderer _renderer;
    
    public HexViewTable() throws IOException {
        setModel(new HexViewTableModel(null));
        setFont(new Font("Courier New", getFont().getStyle(), getFont().getSize()));
        TableColumnModel cm = getColumnModel();
        fixWidth(cm, 0, 61);
        for (int i = 1; i < 17; i++) {
            fixWidth(cm, i, 21);
        }
        cm.getColumn(17).setMinWidth(115);
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        setFillsViewportHeight(true);
        setCellSelectionEnabled(true);
        _renderer = new HighlightTableCellRenderer();
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
    
}
