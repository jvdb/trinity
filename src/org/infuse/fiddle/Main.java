package org.infuse.fiddle;

import javax.swing.SwingUtilities;

public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getClass() + " occurred: " + ex.getMessage());
                }
            }
        });
    }    

}
