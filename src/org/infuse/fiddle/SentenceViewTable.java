package org.infuse.fiddle;

import javax.swing.JTree;

@SuppressWarnings("serial")
public class SentenceViewTable extends JTree {
    
    public SentenceViewTable() {
        setDragEnabled(false);
        setEditable(false);
        setRootVisible(false);
    }

}
