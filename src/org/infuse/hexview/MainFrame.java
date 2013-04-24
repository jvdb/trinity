package org.infuse.hexview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.swing.CodeEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    
    private HexViewTable _hexView;
    private CodeEditorPane _codeView;
    private File _codeFile;
    private File _dataFile;
    private final Interpreter _interpreter;
    
    public MainFrame() throws IOException {
        _interpreter = new Interpreter();
        initGUI();
    }
    
    private void initGUI() throws IOException {
        // Main window
        setTitle("Fiddle");
        setSize(1056, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Hex view
        _hexView = new HexViewTable();
        JScrollPane tsp = new JScrollPane(_hexView);
        tsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tsp.setPreferredSize(new Dimension(
                _hexView.getPreferredSize().width + 24,
                _hexView.getPreferredSize().height));
        add(tsp, BorderLayout.WEST);
        
        _codeView = new CodeEditorPane();
        _codeView.setKeywordColor(getDerricColors());
        _codeView.setVerticalLineAtPos(80);
        JScrollPane esp = new JScrollPane(_codeView);
        esp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(esp, BorderLayout.CENTER);
        
        // Menu
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("File");
        m.setMnemonic(KeyEvent.VK_F);
        JMenuItem mi = new JMenuItem("Open...", KeyEvent.VK_O);
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFiles();
            }
        });
        m.add(mi);
        mb.add(m);
        setJMenuBar(mb);
        
        // Finalize window
        setVisible(true);
    }
    
    private void chooseFiles() {
        try {
            FileNameExtensionFilter derricFilter = new FileNameExtensionFilter(
                    "Derric descriptions", "derric");
            _codeFile = getFile(derricFilter);
            if (_codeFile != null) {
                List<String> lines = java.nio.file.Files.readAllLines(
                        Paths.get(_codeFile.getPath()), Charset.forName("UTF8"));
                StringBuilder sb = new StringBuilder();
                for (String line : lines) {
                    sb.append(line);
                    sb.append("\n");
                }
                _codeView.setText(sb.toString());
                _dataFile = getFile(null);
                if (_dataFile != null) {
                    _interpreter.run(_codeFile.getPath(), _dataFile.getPath());
                    ((HexViewTableModel) _hexView.getModel()).setFile(_dataFile);
                    _hexView.revalidate();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private File getFile(FileNameExtensionFilter filter) {
        JFileChooser fc = new JFileChooser();
        if (filter != null) {
            fc.setFileFilter(filter);
        }
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(MainFrame.this)) {
            File sf = fc.getSelectedFile();
            if (sf.exists() && !sf.isDirectory()) {
                return sf;
            }
        }
        return null;
    }
    
    private HashMap<String, Color> getDerricColors() {
        HashMap<String, Color> syntax = new HashMap<String, Color>();
        return syntax;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame();
                } catch (IOException ex) {
                    // TODO: Show message
                }
            }
        });
    }
    
}
