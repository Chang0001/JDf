package com.fanwe.p2p.model;

import java.io.Serializable;

import com.fanwe.p2p.constant.Constant.DealsActSearchConditionCid;
import com.fanwe.p2p.constant.Constant.DealsActSearchConditionDealStatus;
import com.fanwe.p2p.constant.Constant.DealsActSearchConditionInterest;
import com.fanwe.p2p.constant.Constant.DealsActSearchConditionLevel;
import com.fanwe.p2p.constant.Constant.DealsActSearchConditionMonths;
import com.fanwe.p2p.constant.Constant.DealsActSearchConditionLeftTime;

public class DealsSearchModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String conditionCid = DealsActSearchConditionCid.ALL;
	private String conditionLevel = DealsActSearchConditionLevel.ALL;
	private String conditionInterest = DealsActSearchConditionInterest.ALL;
	private String conditionDealStatus = DealsActSearchConditionDealStatus.ALL;

	public String getConditionDealStatus()
	{
		return conditionDealStatus;
	}

	public void setConditionDealStatus(String conditionDealStatus)
	{
		this.conditionDealStatus = conditionDealStatus;
	}

	public String getConditionCid()
	{
		return conditionCid;
	}

	public void setConditionCid(String conditionCid)
	{
		this.conditionCid = conditionCid;
	}

	public String getConditionLevel()
	{
		return conditionLevel;
	}

	public void setConditionLevel(String conditionLevel)
	{
		this.conditionLevel = conditionLevel;
	}

	public String getConditionInterest()
	{
		return conditionInterest;
	}

	public void setConditionInterest(String conditionInterest)
	{
		this.conditionInterest = conditionInterest;
	}

}
