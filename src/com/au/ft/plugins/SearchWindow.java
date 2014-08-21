package com.au.ft.plugins;

import com.au.ft.helpers.*;
import com.eviware.soapui.support.UISupport;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/*
 * This class implements Search Window UI
 */

public class SearchWindow
{
	public Integer windowCloseFlag = 0;
	SoapUISearchAction searchAction;
	List<String> dropDownList;

	//Declare window elements
	JFrame frame = new JFrame();
	JDialog dialog = new JDialog(frame,"SoapUI Search");
	JComboBox comboBox = new JComboBox();
	JTextField textField = new JTextField("",25);
	JCheckBox regExCheckBox = new JCheckBox("Use Search String as Regular Expression");
	JCheckBox onlyNameCheckBox = new JCheckBox("Search item names ONLY");
	JButton searchButton = new JButton("Search !!");	
	JButton cancelButton = new JButton("Cancel");

	public SearchWindow(SoapUISearchAction searchAction, List<String> dropDownList)
	{
		this.searchAction = searchAction;
		this.dropDownList = dropDownList;
	}

	public void OpenSearchWindow( )
	{

		try 
		{

			//Retrieving images

			//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ClassLoader classLoader = this.getClass().getClassLoader();
			InputStream searchImage = classLoader.getResourceAsStream("SearchImage.png");
			InputStream searchTitle = classLoader.getResourceAsStream("SearchTitle.jpg");

			//Declare window elements

			JLabel comboLabel = new JLabel("Select Project/TestSuite (Optional)");
			JLabel textBoxLabel = new JLabel("<html><font color='red'>*</font> Look For</html>");
			JLabel imageLabel = new JLabel(new ImageIcon(ImageIO.read(searchImage)));
			comboBox = new JComboBox(dropDownList.toArray());
			SpringLayout layout = new SpringLayout();
			JPanel contentPane = new JPanel();
			Image img = ImageIO.read(searchTitle);


			//Setting properties for window elements
			comboLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
			comboLabel.setLabelFor(comboBox);
			textBoxLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
			textBoxLabel.setLabelFor(textField);
			comboBox.setFont(new Font("Calibri", Font.PLAIN, 14));
			textField.setFont(new Font("Calibri", Font.PLAIN, 14));
			searchButton.setName("SearchButton");
			searchButton.setFont(new Font("Calibri", Font.PLAIN, 14));
			searchButton.setPreferredSize(new Dimension(80, 30));
			cancelButton.setName("CancelButton");
			cancelButton.setFont(new Font("Calibri", Font.PLAIN, 14));
			cancelButton.setPreferredSize(new Dimension(80, 30));
			contentPane.setLayout(layout);
			contentPane.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
			contentPane.setOpaque(true);
			dialog.setIconImage(img); 		
			dialog.setSize(new Dimension(570, 340));
			dialog.setLocationRelativeTo(frame);
			dialog.setModal(true);

			//Adding listeners for window elements
			textField.addKeyListener(new SearchActionListener(textField,null,dialog,this));
			regExCheckBox.addKeyListener(new SearchActionListener(regExCheckBox,null,dialog,this));
			onlyNameCheckBox.addKeyListener(new SearchActionListener(onlyNameCheckBox,null,dialog,this));
			searchButton.addActionListener(new SearchActionListener(searchButton,textField,dialog,this));
			searchButton.addKeyListener(new SearchActionListener(searchButton,textField,dialog,this));
			cancelButton.addActionListener(new SearchActionListener(cancelButton,null,dialog,this));
			cancelButton.addKeyListener(new SearchActionListener(cancelButton,null,dialog,this));
			dialog.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { windowCloseFlag = 1; } });


			//Adding elements to containers
			contentPane.add(comboLabel);
			contentPane.add(comboBox);
			contentPane.add(textBoxLabel);
			contentPane.add(textField);
			contentPane.add(imageLabel);
			contentPane.add(regExCheckBox);
			contentPane.add(onlyNameCheckBox);
			contentPane.add(searchButton);
			contentPane.add(cancelButton);
			dialog.setContentPane(contentPane);

			//Position the window elements
			layout.putConstraint(SpringLayout.WEST, comboLabel,150,SpringLayout.WEST, contentPane);
			layout.putConstraint(SpringLayout.NORTH, comboLabel,10,SpringLayout.NORTH, contentPane);
			layout.putConstraint(SpringLayout.WEST, comboBox,150,SpringLayout.WEST, contentPane);
			layout.putConstraint(SpringLayout.NORTH, comboBox,30,SpringLayout.NORTH, contentPane);
			layout.putConstraint(SpringLayout.WEST, textBoxLabel,150,SpringLayout.WEST, contentPane);
			layout.putConstraint(SpringLayout.NORTH, textBoxLabel,65,SpringLayout.NORTH, contentPane);
			layout.putConstraint(SpringLayout.WEST, textField,150,SpringLayout.WEST, contentPane);
			layout.putConstraint(SpringLayout.NORTH, textField,85,SpringLayout.NORTH, contentPane);
			layout.putConstraint(SpringLayout.WEST, regExCheckBox,150,SpringLayout.WEST, contentPane);
			layout.putConstraint(SpringLayout.NORTH, regExCheckBox,130,SpringLayout.NORTH, contentPane);
			layout.putConstraint(SpringLayout.WEST, onlyNameCheckBox,150,SpringLayout.WEST, contentPane);
			layout.putConstraint(SpringLayout.NORTH, onlyNameCheckBox,155,SpringLayout.NORTH, contentPane);
			layout.putConstraint(SpringLayout.NORTH, searchButton,110,SpringLayout.NORTH, textField);
			layout.putConstraint(SpringLayout.WEST, searchButton,160,SpringLayout.WEST, contentPane);
			layout.putConstraint(SpringLayout.NORTH, cancelButton,110,SpringLayout.NORTH, textField);
			layout.putConstraint(SpringLayout.WEST, cancelButton,270,SpringLayout.WEST, contentPane);

			dialog.setVisible(true);

		}
		catch(IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			UISupport.showErrorMessage("Failed during Search Window Load. Check Error log file for details.");
		}
		catch(Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			UISupport.showErrorMessage("Failed during Search Window Load. Check Error log file for details.");
		}
	}

	public void ExecuteSearch() {
		try {
			//Retrieving user inputs
			String token=textField.getText();
			String tokenOrig=token;
			String selectedItem=comboBox.getSelectedItem().toString();
			Integer nameSearchFlag = 0;
			if ( !regExCheckBox.isSelected())
			{
				token = StringUtility.ReplaceSpecialCharacters(token);
			}


			if ( onlyNameCheckBox.isSelected())
			{
				nameSearchFlag = 1;
			}

			//Passing user inputs to SoapUISearchAction class
			searchAction.windowCloseFlag = windowCloseFlag;
			searchAction.tokenOrig = tokenOrig;
			searchAction.token	= token;
			searchAction.selectedItem = selectedItem;
			searchAction.nameSearchFlag = nameSearchFlag;

			searchAction.SearchAndDisplayResults(dialog);
		}	catch(Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//dialog.setVisible(false);
			//dialog.dispose();
			UISupport.showErrorMessage("Failed during Search Operation. Check Error log file for details.");
			dialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			textField.requestFocusInWindow();
		}

	}
}
