package com.au.ft.helpers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JDialog;
import com.eviware.soapui.reporting.support.ReportTemplate;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.model.*;	
import com.eviware.soapui.config.JMSPropertyConfig;
import com.eviware.soapui.impl.wsdl.loadtest.WsdlLoadTest;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockOperation;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockResponse;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockService;
import com.eviware.soapui.impl.wsdl.project.WsdlRequirement;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.WsdlTestCasePro;
import com.eviware.soapui.model.iface.Attachment;
import com.eviware.soapui.model.iface.MessagePart;
import com.eviware.soapui.model.mock.MockResponse;
import com.eviware.soapui.model.security.SecurityScan;
import com.eviware.soapui.model.testsuite.*;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.impl.wsdl.teststeps.*;
import com.eviware.soapui.impl.wsdl.teststeps.datagen.*;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.basic.*;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.json.JsonPathContentAssertion;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.json.JsonPathExistenceAssertion;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.json.JsonPathRegExAssertion;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.*;
import com.eviware.soapui.impl.wsdl.support.connections.DatabaseConnection;
import com.eviware.soapui.impl.rest.mock.*;
import com.eviware.soapui.impl.rest.support.RestParamProperty;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.support.*;
import com.eviware.soapui.impl.rest.*;
import com.eviware.soapui.impl.wsdl.loadtest.*;
import com.eviware.soapui.security.*;
import com.eviware.soapui.security.result.SecurityScanRequestResult;

/* 
 * This class provides SoapUI search related utility functions
 */

public class SearchUtility{

	private List<ModelItem> searchResult = new ArrayList<>();;
	private String token;
	private ModelItem parent;
	private Integer matchFlag;
	private Integer nameSearchFlag;
	private JDialog dialog;
	private boolean soapUIProFlag;

	/*
	 *The constructor 
	 */
	public SearchUtility(Integer nameSearchFlag)
	{
		this.nameSearchFlag = nameSearchFlag;
	}

	/*
	 *This method triggers SoapUI Search for the provided String
	 */

	public List<ModelItem> SearchItemInSoapUI(ModelItem parent, String token,JDialog dialog, Boolean soapUIProFlag) throws Exception
	{
		this.token			= token.toLowerCase();
		this.parent			= parent;
		this.dialog			= dialog;
		this.soapUIProFlag	= soapUIProFlag;
		//this.searchResult	= searchResult;
		SearchParentElement(parent)	;
		SearchChildElements(parent);
		return searchResult;
	}

	private void SearchParentElement(ModelItem parent) throws Exception
	{

		SearchItem(parent);		

	}
	private void SearchChildElements(ModelItem parent) throws Exception
	{

		for ( ModelItem child : parent.getChildren() )
		{
			SearchItem(child)	;	
			SearchChildElements(child);
		}

	}


	/*
	 * This Method does the actual SoapUI Search across all soapUI items.
	 */
	private void SearchItem(ModelItem modelItem) throws Exception
	{

			matchFlag = 0;
			token = token.toLowerCase();
			String modelItemName = modelItem.getName().toLowerCase();	
			CompareStrings(modelItemName);

			//Check if only item name needs to be searched on
			if ( nameSearchFlag == 0)
			{	
				String modelItemDesc = modelItem.getDescription();
				CompareStrings(modelItemDesc);

				if ( modelItem instanceof WsdlTestStep   )
				{
					CompareStrings(((WsdlTestStep) modelItem).getConfig().toString());
				}

				else if ( modelItem instanceof WsdlLoadTest  )
				{
					CompareStrings(((WsdlLoadTest) modelItem).getConfig().toString());
				}

				else if ( modelItem instanceof SecurityTest  )
				{
					CompareStrings(((SecurityTest) modelItem).getConfig().toString());					
				}

				else if ( modelItem instanceof WsdlTestCase  )
				{
					CompareStrings(((WsdlTestCase) modelItem).getSetupScript());
					CompareStrings(((WsdlTestCase) modelItem).getTearDownScript());
				}

				else if ( modelItem instanceof WsdlTestSuite )
				{
					CompareStrings(((WsdlTestSuite) modelItem).getSetupScript());
					CompareStrings(((WsdlTestSuite) modelItem).getTearDownScript());
				}

				else if ( modelItem instanceof WsdlMockResponse )
				{
					CompareStrings(((WsdlMockResponse) modelItem).getConfig().toString());
				}
				
				else if ( modelItem instanceof WsdlMockOperation )
				{
					CompareStrings(((WsdlMockOperation)modelItem).getDefaultResponse());
					CompareStrings(((WsdlMockOperation)modelItem).getScript());
				}
				
				else if ( modelItem instanceof WsdlMockService )
				{
					CompareStrings(((WsdlMockService)modelItem).getHelpUrl());
					CompareStrings(((WsdlMockService)modelItem).getHost());
					CompareStrings(((WsdlMockService)modelItem).getIconName());
					CompareStrings(((WsdlMockService)modelItem).getIncomingWss());
					CompareStrings(((WsdlMockService)modelItem).getLocalMockServiceEndpoint());
					CompareStrings(((WsdlMockService)modelItem).getMockServiceEndpoint());
					CompareStrings(((WsdlMockService)modelItem).getOutgoingWss());
					CompareStrings(((WsdlMockService)modelItem).getAfterRequestScript());
					CompareStrings(((WsdlMockService)modelItem).getDocroot());
					CompareStrings(((WsdlMockService)modelItem).getLocalEndpoint());
					CompareStrings(((WsdlMockService)modelItem).getOnRequestScript());
					CompareStrings(((WsdlMockService)modelItem).getPath());
					CompareStrings(((WsdlMockService)modelItem).getMockServiceEndpoint());
					CompareStrings(((WsdlMockService)modelItem).getStartScript());
					CompareStrings(((WsdlMockService)modelItem).getStopScript());
				}

				else if ( modelItem instanceof RestMockResponse )
				{
					CompareStrings(((RestMockResponse) modelItem).getConfig().toString());
				}

				else if ( modelItem instanceof RestMockAction )
				{
					CompareStrings(((RestMockAction)modelItem).getDefaultResponse());
					CompareStrings(((RestMockAction)modelItem).getScript());
					CompareStrings(((RestMockAction)modelItem).getResourcePath());
					CompareStrings(((RestMockAction)modelItem).getHelpUrl());
				}
	
				else if ( modelItem instanceof RestMockService )
				{
					CompareStrings(((RestMockService)modelItem).getHelpUrl());
					CompareStrings(((RestMockService)modelItem).getHost());
					CompareStrings(((RestMockService)modelItem).getIconName());
					CompareStrings(((RestMockService)modelItem).getAfterRequestScript());
					CompareStrings(((RestMockService)modelItem).getDocroot());
					CompareStrings(((RestMockService)modelItem).getLocalEndpoint());
					CompareStrings(((RestMockService)modelItem).getOnRequestScript());
					CompareStrings(((RestMockService)modelItem).getPath());
					CompareStrings(((RestMockService)modelItem).getStartScript());
					CompareStrings(((RestMockService)modelItem).getStopScript());
				}

				else if ( modelItem instanceof RestRequest )
				{
					CompareStrings(((RestRequest) modelItem).getConfig().toString());
				}
				
				else if ( modelItem instanceof RestMethod )
				{
					CompareStrings(((RestMethod)modelItem).getDefaultRequestMediaType());
					
					if ( ((RestMethod)modelItem).getResponseMediaTypes() != null )
					CompareStrings(((RestMethod)modelItem).getResponseMediaTypes().toString());
					
					RestRepresentation[] repList = ((RestMethod)modelItem).getRepresentations();

					for (RestRepresentation restRep : repList) 
					{ 
						CompareStrings(restRep.getDefaultContent());
						CompareStrings(restRep.getDescription());
						CompareStrings(restRep.getMediaType());
					} 

					if ( ((RestMethod)modelItem).getParams() != null && ((RestMethod)modelItem).getParams().getPropertyNames() != null )
					{
					for (String propName : ((TestPropertyHolder)((RestMethod)modelItem).getParams()).getPropertyNames()) 
					{ 
						CompareStrings(propName);
						CompareStrings( ((TestPropertyHolder)((RestMethod)modelItem).getParams()).getPropertyValue(propName));
					} 
					}

					if ( ((RestMethod)modelItem).getOverlayParams() != null && ((RestMethod)modelItem).getOverlayParams().getPropertyNames() != null )
					{
					for (String propName : ((TestPropertyHolder)((RestMethod)modelItem).getOverlayParams()).getPropertyNames()) 
					{ 
						CompareStrings(propName);
						CompareStrings( ((TestPropertyHolder)((RestMethod)modelItem).getOverlayParams()).getPropertyValue(propName));
					} 
					}
				}

				else if ( modelItem instanceof RestResource )
				{
					CompareStrings(((RestResource)modelItem).getFullPath());
					CompareStrings(((RestResource)modelItem).getPath());
					
					if ( ((RestResource)modelItem).getRequestMediaTypes() != null )
					CompareStrings(((RestResource)modelItem).getRequestMediaTypes().toString());
					
					if ( ((RestResource)modelItem).getResponseMediaTypes() != null )
					CompareStrings(((RestResource)modelItem).getResponseMediaTypes().toString() );

					RestParamProperty[] restParamList = ((RestResource)modelItem).getDefaultParams();

					for (RestParamProperty restParam : restParamList) 
					{ 
						CompareStrings(restParam.getName());
						CompareStrings(restParam.getDescription());
						CompareStrings(restParam.getValue());
						
						if ( restParam.getOptions() != null )
						CompareStrings(restParam.getOptions().toString());
						CompareStrings(restParam.getPath());
					} 

					if ( ((RestResource)modelItem).getParams() != null && ((RestResource)modelItem).getParams().getPropertyNames() != null )
					{
					for (String propName : ((TestPropertyHolder)((RestResource)modelItem).getParams()).getPropertyNames()) 
					{ 
						CompareStrings(propName);
						CompareStrings( ((TestPropertyHolder)((RestResource)modelItem).getParams()).getPropertyValue(propName));
					} 
					}

					MessagePart[] mReqPartList = ((RestResource)modelItem).getDefaultRequestParts() ;

					for (MessagePart mReqPart : mReqPartList) 
					{ 
						CompareStrings(mReqPart.getName());
						CompareStrings(mReqPart.getDescription());
					} 

					MessagePart[] mRespPartList = ((RestResource)modelItem).getDefaultResponseParts() ;

					for (MessagePart mRespPart : mRespPartList) 
					{ 
						CompareStrings(mRespPart.getName());
						CompareStrings(mRespPart.getDescription());
					} 
				}
						
				else if ( modelItem instanceof RestService )
				{
					CompareStrings(((RestService)modelItem).getBasePath());
					CompareStrings(((RestService)modelItem).getInferredSchema());
					CompareStrings(((RestService)modelItem).getInterfaceType());
					CompareStrings(((RestService)modelItem).getTechnicalId());
					CompareStrings(((RestService)modelItem).getType());
					CompareStrings(((RestService)modelItem).getWadlUrl());
					CompareStrings(((RestService)modelItem).getWadlVersion());	
				}
				
				else if ( modelItem instanceof WsdlRequest )
				{
					CompareStrings(((WsdlRequest) modelItem).getConfig().toString());	
				}
				else if ( modelItem instanceof WsdlOperation )
				{
					CompareStrings(((WsdlOperation)modelItem).getBindingOperationName());
					CompareStrings(((WsdlOperation)modelItem).getInputName());
					CompareStrings(((WsdlOperation)modelItem).getOutputName());
					CompareStrings(((WsdlOperation)modelItem).getType());

					MessagePart[] mReqPartList = ((WsdlOperation)modelItem).getDefaultRequestParts() ;

					for (MessagePart mReqPart : mReqPartList) 
					{ 
						CompareStrings(mReqPart.getName());
						CompareStrings(mReqPart.getDescription());
					} 

					MessagePart[] mRespPartList = ((WsdlOperation)modelItem).getDefaultResponseParts()  ;

					for (MessagePart mRespPart : mRespPartList) 
					{ 
						CompareStrings(mRespPart.getName());
						CompareStrings(mRespPart.getDescription());
					} 
				}
				else if ( modelItem instanceof WsdlInterface )
				{
					if ( ((WsdlInterface)modelItem).getBindingName() != null )
					{
						CompareStrings(((WsdlInterface)modelItem).getBindingName().getLocalPart());
						CompareStrings(((WsdlInterface)modelItem).getBindingName().getNamespaceURI());
						CompareStrings(((WsdlInterface)modelItem).getBindingName().getPrefix());	
					}
					CompareStrings(((WsdlInterface)modelItem).getInterfaceType());
					CompareStrings(((WsdlInterface)modelItem).getTechnicalId());
					CompareStrings(((WsdlInterface)modelItem).getType());
					CompareStrings(((WsdlInterface)modelItem).getWsaVersion());	
				}
				
				else if ( modelItem instanceof WsdlProject )
				{
					CompareStrings(((WsdlProject)modelItem).getAfterLoadScript());
					CompareStrings(((WsdlProject)modelItem).getBeforeSaveScript());
					CompareStrings(((WsdlProject)modelItem).getAfterRunScript());
					CompareStrings(((WsdlProject)modelItem).getBeforeRunScript());
					CompareStrings(((WsdlProject)modelItem).getHermesConfig());
					CompareStrings(((WsdlProject)modelItem).getPath());
				}

				if ( soapUIProFlag)
				{
					if ( modelItem instanceof WsdlMockService )
					{
						CompareStrings(((WsdlMockService)modelItem).getStringID());
					}

					else if ( modelItem instanceof WsdlMockOperation )
					{

						CompareStrings(((WsdlMockOperation)modelItem).getScriptHelpUrl());

					}
					else if ( modelItem instanceof RestMockService )
					{
						CompareStrings(((RestMockService)modelItem).getStringID());

					}

					else if ( modelItem instanceof RestMockAction )
					{

						CompareStrings(((RestMockAction)modelItem).getScriptHelpUrl());

					}
					else if ( modelItem instanceof WsdlTestCasePro  )
					{
						CompareStrings(((WsdlTestCasePro) modelItem).getOnReportScript());
						
						if ( ((WsdlTestCasePro)modelItem).getReportParameters() != null )
						CompareStrings(((WsdlTestCasePro) modelItem).getReportParameters().toString());	
					}
					else if ( modelItem instanceof WsdlTestSuitePro )
					{
						CompareStrings(((WsdlTestSuitePro) modelItem).getOnReportScript());
						
						if ( ((WsdlTestSuitePro)modelItem).getReportParameters() != null )
						CompareStrings(((WsdlTestSuitePro) modelItem).getReportParameters().toString());	
					}
					else if ( modelItem instanceof WsdlProjectPro )
					{
						CompareStrings(((WsdlProjectPro)modelItem).getOnReportScript());
						
						if ( ((WsdlProjectPro)modelItem).getEnvironmentNames() != null )
						CompareStrings(((WsdlProjectPro)modelItem).getEnvironmentNames().toString());

						Integer eventNum = 0;
						Integer eventCount = ((WsdlProjectPro)modelItem).getEventHandlerCount() ;
						
						if ( ((WsdlProjectPro)modelItem).getRequirements() != null  && ((WsdlProjectPro)modelItem).getRequirements().getRequirementsList() != null )
						{
						List<WsdlRequirement> reqList = ((WsdlProjectPro)modelItem).getRequirements().getRequirementsList();
						for (WsdlRequirement wsdlReq : reqList) 
						{
							CompareStrings(wsdlReq.getId() );
							CompareStrings(wsdlReq.getName() );
							CompareStrings(wsdlReq.getDescription() );
							CompareStrings(wsdlReq.getStatus() );
							CompareStrings(wsdlReq.getDetails() );

						}
						}
						
						if ( ((WsdlProjectPro)modelItem).getReports() != null )
						{
						List<ReportTemplate> reportList = ((WsdlProjectPro)modelItem).getReports();
						for (ReportTemplate wsdlReq : reportList) 
						{
							CompareStrings(wsdlReq.getName());
							CompareStrings(wsdlReq.getDescription());
							CompareStrings(wsdlReq.getData());
							CompareStrings(wsdlReq.getSubreportsList().toString());

						}
						}
						
						if ( ((WsdlProjectPro)modelItem).getDatabaseConnectionContainer() != null  && ((WsdlProjectPro)modelItem).getDatabaseConnectionContainer().getDatabaseConnectionList() != null )
						{
						List<DatabaseConnection> dbConnectionList = ((WsdlProjectPro)modelItem).getDatabaseConnectionContainer().getDatabaseConnectionList();
						for (DatabaseConnection dbConnection : dbConnectionList) 
						{
							CompareStrings(dbConnection.getName());
							CompareStrings(dbConnection.getDriver());
							CompareStrings(dbConnection.getConnectionString());

						}
						}
						while ( eventNum < eventCount )
						{
							String eventName = ((WsdlProjectPro)modelItem).getEventHandlerAt(eventNum).getName();
							String eventTarget	= ((WsdlProjectPro)modelItem).getEventHandlerAt(eventNum).getTarget();
							String eventType	= ((WsdlProjectPro)modelItem).getEventHandlerAt(eventNum).getType();
							String eventScriptText = ((WsdlProjectPro)modelItem).getEventHandlerAt(eventNum).getScript().getScriptText() ;
							CompareStrings(eventName);
							CompareStrings(eventTarget);
							CompareStrings(eventType);
							CompareStrings(eventScriptText);
							eventNum++;
						}
					}
				}
				//Java reflect to check for methods which exist in more than one one model item, but not all
				Class<?> classHandle = modelItem.getClass();

				for(Method m : classHandle.getMethods())
				{
					if(m.getName().equals("getPropertyNames")) 
					{		        	
						for (String propName : ((TestPropertyHolder)modelItem).getPropertyNames()) 
						{ 
							CompareStrings(propName);
							CompareStrings( ((TestPropertyHolder)modelItem).getPropertyValue(propName));
						} 
					}

					if(m.getName().equals("getResponseHeaders")) 
					{		        	
						CompareStrings((m.invoke(modelItem,null).toString()));
					}

					if(m.getName().equals("getResponseContentAsXml")) 
					{		        	
						CompareStrings(((String)m.invoke(modelItem,null)));
					}

					if(m.getName().equals("getResponseContent")) 
					{		        	
						CompareStrings(((String)m.invoke(modelItem,null)));
					}

					if(m.getName().equals("getAttachments")) 
					{		        	
						for (Attachment attachmentList : ((Attachment [])m.invoke(modelItem,null)) ) 
						{ 
							CompareStrings(attachmentList.getName());
							CompareStrings(attachmentList.getUrl());
						} 
					}

				}

			}

			//Add model item to searchResult list if there is a match
			if ( matchFlag == 1 )
			{
				searchResult.add(modelItem);
			}
	}

	/*
	 * This method does the string match function
	 */
	private void CompareStrings(String stringValue) throws Exception
	{
		try
		{
		if ( stringValue != "" && stringValue != null )
		{
			stringValue = stringValue.toLowerCase().replaceAll("[\n\r]", "");
			if ( stringValue.matches(".*"+token+".*") )
			{
				matchFlag=1;
			}
		}	
		}
		catch(Exception e) { 
			e.printStackTrace();
			dialog.setVisible(false);
			dialog.dispose();
			UISupport.showErrorMessage("Failed during Search Operation. Check Error log file for details.");} // Exception caught

	}


}