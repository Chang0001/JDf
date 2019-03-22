package com.fanwe.p2p.model;

import java.io.Serializable;

import com.fanwe.p2p.constant.Constant.TransferActSearchConditionCid;
import com.fanwe.p2p.constant.Constant.TransferActSearchConditionInterest;
import com.fanwe.p2p.constant.Constant.TransferActSearchConditionLeftTime;
import com.fanwe.p2p.constant.Constant.TransferActSearchConditionLevel;
import com.fanwe.p2p.constant.Constant.TransferActSearchConditionMonths;

public class BondTransferSearchModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String conditionCid = TransferActSearchConditionCid.ALL;
	private String conditionLevel = TransferActSearchConditionLevel.ALL;
	private String conditionInterest = TransferActSearchConditionInterest.ALL;
	private String conditionMonths = TransferActSearchConditionMonths.ALL;
	private String conditionLeftTime = TransferActSearchConditionLeftTime.ALL;

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

	public String getConditionMonths()
	{
		return conditionMonths;
	}

	public void setConditionMonths(String conditionMonths)
	{
		this.conditionMonths = conditionMonths;
	}

	public String getConditionLeftTime()
	{
		return conditionLeftTime;
	}

	public void setConditionLeftTime(String conditionLeftTime)
	{
		this.conditionLeftTime = conditionLeftTime;
	}

}
