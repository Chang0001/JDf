package com.fanwe.p2p.model;

import com.fanwe.p2p.ProjectDetailWebviewActivity;

public class RuntimeConfigModel
{

	private boolean isMainActivityStarted = false;
	private ProjectDetailWebviewActivity projectDetailWebviewActivity = null;

	public ProjectDetailWebviewActivity getProjectDetailWebviewActivity()
	{
		return projectDetailWebviewActivity;
	}

	public void setProjectDetailWebviewActivity(ProjectDetailWebviewActivity projectDetailWebviewActivity)
	{
		this.projectDetailWebviewActivity = projectDetailWebviewActivity;
	}

	public boolean isMainActivityStarted()
	{
		return isMainActivityStarted;
	}

	public void setMainActivityStarted(boolean isMainActivityStarted)
	{
		this.isMainActivityStarted = isMainActivityStarted;
	}
	
	
	
}
