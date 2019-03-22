package com.fanwe.p2p.model.act;

public class BaseActModel
{
	protected String act = null;
	protected String act_2 = null;
	protected int response_code = -999;
	protected String show_err = null;
	protected int user_login_status = 0;
	
	

	public int getUser_login_status()
	{
		return user_login_status;
	}

	public void setUser_login_status(int user_login_status)
	{
		this.user_login_status = user_login_status;
	}

	public String getShow_err()
	{
		return show_err;
	}

	public void setShow_err(String show_err)
	{
		this.show_err = show_err;
	}

	public int getResponse_code()
	{
		return response_code;
	}

	public void setResponse_code(int response_code)
	{
		this.response_code = response_code;
	}

	public String getAct()
	{
		return act;
	}

	public void setAct(String act)
	{
		this.act = act;
	}

	public String getAct_2()
	{
		return act_2;
	}

	public void setAct_2(String act_2)
	{
		this.act_2 = act_2;
	}

}
