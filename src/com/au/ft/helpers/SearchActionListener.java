package com.au.ft.helpers;

import com.au.ft.plugins.*;
import javax.swing.*;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/*
 * This class implements actions based on user clicks or selections
 */

 public class SearchActionListener implements ActionListener, KeyListener {

	Object jObject;
	JTextField jTextField;
	JDialog dialog;
	String searchString;
	SearchWindow searchWindow;

    public SearchActionListener(Object jObject, JTextField jTextField,JDialog dialog, SearchWindow searchWindow){
        this.jObject = jObject;
        this.jTextField = jTextField;
		this.dialog = dialog;
		this.searchWindow = searchWindow;
    }
    

    @Override
    public void actionPerformed(ActionEvent submitClicked) {

    	//Check for button clicks
    	if ( jObject instanceof JButton && ((JButton) jObject).getName() == "SearchButton")
    	{
			if (jTextField.getText().trim().equals(""))
			{  
				JOptionPane.showMessageDialog(null,"Please enter a search string.");
				jTextField.requestFocusInWindow();
			}
			else
			{
				dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				searchWindow.ExecuteSearch();			
			}
    	}
    	else if ( jObject instanceof JButton && ((JButton) jObject).getName() == "CancelButton")
    	{
    		searchWindow.windowCloseFlag=1;
    		dialog.setVisible(false);
    	}

    }

    @Override
    public void keyPressed(KeyEvent e) {
    	//Check for "ENTER" key press
		if( e.getKeyCode() == KeyEvent.VK_ENTER )
		{
			if ( jObject instanceof JTextField)
			{
				jTextField = (JTextField) jObject;
			}
			
			if ((jObject instanceof JButton && ((JButton) jObject).getName() == "SearchButton") || jObject instanceof JTextField )
			{
				if (jTextField.getText().trim().equals(""))
				{  
					JOptionPane.showMessageDialog(null,"Please enter a search string.");
					jTextField.requestFocusInWindow();
				}
				else
				{
					dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					searchWindow.ExecuteSearch();
				}
			}
			else if (jObject instanceof JButton && ((JButton) jObject).getName() == "CancelButton" )
			{
				searchWindow.windowCloseFlag=1;
	    		dialog.setVisible(false);
			}
			
			//Select or Unselect checkboxes for each "ENTER" key press
			else if (jObject instanceof JCheckBox )
			{
				if (((JCheckBox)jObject).isSelected())
				{
					((JCheckBox)jObject).setSelected(false);
				}
				else
				{
					((JCheckBox)jObject).setSelected(true);
				}
			}
			
		}

    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }
    
    
}