package com.fanwe.p2p.model;

public class FeeStageModel
{
	private double max = 0;
	private double min = 0;

	private double fee = 0;

	private String feeFormat = null;

	public String getFeeFormat()
	{
		return feeFormat;
	}

	public void setFeeFormat(String feeFormat)
	{
		this.feeFormat = feeFormat;
	}

	public double getMax()
	{
		return max;
	}

	public void setMax(double max)
	{
		this.max = max;
	}

	public double getMin()
	{
		return min;
	}

	public void setMin(double min)
	{
		this.min = min;
	}

	public double getFee()
	{
		return fee;
	}

	public void setFee(double fee)
	{
		this.fee = fee;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o != null && o instanceof FeeStageModel)
		{
			FeeStageModel model = (FeeStageModel) o;
			if (model.getFee() == this.fee && model.getMax() == this.max && model.getMin() == this.min)
			{
				return true;
			} else
			{
				return false;
			}
		}
		return super.equals(o);
	}

}
