package com.fanwe.p2p.server;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.p2p.model.FeeStageModel;

public class SDFeeCalculater
{
	private List<FeeStageModel> mListFeeStageModel = new ArrayList<FeeStageModel>();

	public void addFeeStageModel(FeeStageModel feeStageModel)
	{
		if (feeStageModel != null)
		{
			boolean isContain = false;
			for (FeeStageModel model : mListFeeStageModel)
			{
				if (model.equals(feeStageModel))
				{
					isContain = true;
					break;
				}
			}
			if (!isContain)
			{
				mListFeeStageModel.add(feeStageModel);
			}
		}
	}

	public Double getFee(float number)
	{
		FeeStageModel matchModel = getMatchModel(number);
		if (matchModel != null)
		{
			return matchModel.getFee();
		} else
		{
			return null;
		}
	}

	public String getFeeFormat(float number)
	{
		FeeStageModel matchModel = getMatchModel(number);
		if (matchModel != null)
		{
			return matchModel.getFeeFormat();
		} else
		{
			return null;
		}
	}

	private FeeStageModel getMatchModel(float number)
	{
		if (number > 0)
		{
			for (FeeStageModel model : mListFeeStageModel)
			{
				if (model != null)
				{
					if (number >= model.getMin() && number <= model.getMax())
					{
						return model;
					}
				}
			}
			if (mListFeeStageModel.size() > 0)
			{
				return mListFeeStageModel.get(mListFeeStageModel.size() - 1);
			} else
			{
				return null;
			}
		} else
		{
			return null;
		}
	}

}
