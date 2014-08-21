package com.au.ft.plugins;

import com.au.ft.helpers.*;
import com.eviware.soapui.support.*;
import com.eviware.soapui.model.*;	
import com.eviware.soapui.model.workspace.*;	
import com.eviware.soapui.support.components.*;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import com.eviware.soapui.support.action.support.AbstractSoapUIAction;
import com.eviware.soapui.plugins.ActionConfiguration;
import com.eviware.soapui.SoapUI;

/*
 * For SoapUI Search Action. 
 */
@ActionConfiguration(actionGroup = ActionGroups.WORKSPACE_ACTIONS,keyStroke = "ctrl F")
public class SoapUISearchAction extends AbstractSoapUIAction
{
	public Integer windowCloseFlag = 0;
	public String token = "";
	public String tokenOrig = "";
	public String selectedItem = "";
	public Integer nameSearchFlag = 0;
	public boolean soapUIProFlag = false;
	public List<ModelItem> modelItemList;
	public List<ModelItem> projectList;

	public SoapUISearchAction()
	{
		super( "Search", "Provides SoapUI Search Functionality" );
	}

	/*
	 * This Method Retrieves the list of all projects for populating in dropdown of Search Window
	 * Calls the Search Window class to display the UI
	 */
	public void perform( ModelItem target, Object param )
	{

		try 
		{
			if ( ClassUtility.ClassExists("com.eviware.soapui.impl.wsdl.WsdlProjectPro"))
			{
				soapUIProFlag = true;
			}

			List<String> dropDownList = new ArrayList<>();
			modelItemList = new ArrayList<>();
			projectList = new ArrayList<>();
			projectList = (List<ModelItem>) ((Workspace)target).getProjectList();
			dropDownList.add(" ");

			//Fetch all projects & testsuites
			for(ModelItem mItem : projectList) 
			{
				modelItemList.add(mItem);
				dropDownList.add(mItem.getName()+" -> Project");

				for ( ModelItem child : mItem.getChildren() )
				{
					modelItemList.add(child);
					dropDownList.add(child.getName()+" -> TestSuite");
				}
			}

			//Open the Search window
			SearchWindow searchWindow = new SearchWindow(this,dropDownList);
			searchWindow.OpenSearchWindow();
		}
		catch(Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			UISupport.showErrorMessage("Failed during Search Window Load. Check Error log file for details.");
		}
	}

	/*
	 * This Method calls the SearchUtility class to perform the search operation
	 * Shows search results in a new search window
	 */
	public void SearchAndDisplayResults(JDialog dialog) throws Exception
	{

			Integer searchListSize;
			List<ModelItem> tempSearchResult = new ArrayList<>();
			List<ModelItem> searchResult = new ArrayList<>();
			List<ModelItem> searchList = new ArrayList<>();

			//Check if the window was closed by user
			if ( windowCloseFlag == 0 )
			{
				//Check if user selected any project or testsuite to search on
				if ( selectedItem != null && selectedItem.trim() != "" )
				{
					selectedItem = selectedItem.split("->")[0].trim();

					for (ModelItem modelItem : modelItemList) 
					{ 
						if (modelItem.getName().trim().equalsIgnoreCase(selectedItem))
						{
							searchList.add(modelItem);
						}

					} 
				}

				searchListSize = searchList.size();

				//Get all projects if user did not select any project or testsuite.
				if ( searchListSize == 0 )
				{
					searchList.addAll(projectList);
				}

				
				//Iterate through projects
				for (ModelItem modelItem : searchList) 
				{ 
					SearchUtility sUtility = new SearchUtility(nameSearchFlag);
					tempSearchResult = sUtility.SearchItemInSoapUI(modelItem, token,dialog,soapUIProFlag);
					searchResult.addAll(tempSearchResult); // Save/append the search result in a list
				} 

				if ( searchResult.isEmpty())
				{
					UISupport.showErrorMessage("No items matching "+"\""+tokenOrig+"\""+" has been found in project ");
					dialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					//perform(target,param);
				}
				else
				{
					//Show results window and close the search window
					dialog.setVisible(false);
					dialog.dispose();
					UISupport.showDesktopPanel( new ModelItemListDesktopPanel("Search Result", "The following items matched the search string "+"\""+tokenOrig+"\"",searchResult.toArray(new ModelItem[searchResult.size()])));
				}

			}

	}

}