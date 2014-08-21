package com.au.ft.plugins;

import com.eviware.soapui.plugins.PluginAdapter;
import com.eviware.soapui.plugins.PluginConfiguration;

@PluginConfiguration(groupId = "com.au.ft.plugins", name = "Search Plugin", version = "1.0",
        autoDetect = true, description = "Provides ability to search for any text within soapUI projects",
        infoUrl = "" )
public class PluginConfig extends PluginAdapter {
}
