/**
Copyright (c) 2011-present - Luu Gia Thuy

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

package com.luugiathuy.games.reversi;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;

/**
 *
 * @author luugiathuy
 */
public class ReversiApplet extends javax.swing.JApplet {

	private static final long serialVersionUID = -6493102144060177243L;
	
	/** Reversi panel */
	private ReversiPanel mReversiPanel;
	
	/** Initializes the applet ReversiApplet */
    public void init() {
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                    addPanel();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void addPanel() {
    	// add reversi panel
        mReversiPanel = new ReversiPanel();
        this.add(mReversiPanel, BorderLayout.CENTER);
    }
    
    private Frame findParentFrame() { 
    	Container c = this;
    	while(c != null) {
    		if (c instanceof Frame)
    			return (Frame)c; 
    		
    		c = c.getParent();
		}
    	return (Frame)null;
	}

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar = new javax.swing.JMenuBar();
        gameMenu = new javax.swing.JMenu();
        mnuNewGame = new javax.swing.JMenuItem();

        gameMenu.setText("Game");

        mnuNewGame.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mnuNewGame.setText("New...");
        mnuNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewGameActionPerformed(evt);
            }
        });
        gameMenu.add(mnuNewGame);

        jMenuBar.add(gameMenu);

        setJMenuBar(jMenuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void mnuNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewGameActionPerformed
    	Frame f = findParentFrame();
    	if ( f != null) {
    		NewGameDialog newGameDialog = new NewGameDialog(f, true);
    		newGameDialog.setVisible(true);
    	}
    }//GEN-LAST:event_mnuNewGameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu gameMenu;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem mnuNewGame;
    // End of variables declaration//GEN-END:variables

}
