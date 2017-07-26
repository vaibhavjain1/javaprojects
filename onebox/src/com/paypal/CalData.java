package com.paypal;

class CalData {

	String CAL_MINUTE;
	String TXN_TIME;
	String MACH_ID;
	String DATA_CTR;
	String CAL_STATUS;
	String STATE;
	String REASON;
	String FC_REASON;
	String FC_TYPE;
	String AVAL_PLAN;
	String DEF_FM;
	String PLANS_FM;
	String DESCRIPTION;
	String CORR_ID;
	String PIPELINE_NAME;
	String SUBPIPELINE_NAME;
	String PROD;
	String PROD_FMLY;
	
	public void setCAL_MINUTE(String cAL_MINUTE) {
		CAL_MINUTE = cAL_MINUTE;
	}

	public void setTXN_TIME(String tXN_TIME) {
		TXN_TIME = tXN_TIME;
	}

	public void setMACH_ID(String mACH_ID) {
		MACH_ID = mACH_ID;
	}

	public void setDATA_CTR(String dATA_CTR) {
		DATA_CTR = dATA_CTR;
	}

	public void setCAL_STATUS(String cAL_STATUS) {
		CAL_STATUS = cAL_STATUS;
	}

	public void setSTATE(String sTATE) {
		STATE = sTATE;
	}

	public void setREASON(String rEASON) {
		REASON = rEASON;
	}

	public void setFC_REASON(String fC_REASON) {
		FC_REASON = fC_REASON;
	}

	public void setFC_TYPE(String fC_TYPE) {
		FC_TYPE = fC_TYPE;
	}

	public void setAVAL_PLAN(String aVAL_PLAN) {
		AVAL_PLAN = aVAL_PLAN;
	}

	public void setDEF_FM(String dEF_FM) {
		DEF_FM = dEF_FM;
	}

	public void setPLANS_FM(String pLANS_FM) {
		PLANS_FM = pLANS_FM;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public void setCORR_ID(String cORR_ID) {
		CORR_ID = cORR_ID;
	}

	public void setPIPELINE_NAME(String pIPELINE_NAME) {
		PIPELINE_NAME = pIPELINE_NAME;
	}

	public void setSUBPIPELINE_NAME(String sUBPIPELINE_NAME) {
		SUBPIPELINE_NAME = sUBPIPELINE_NAME;
	}

	public void setPROD(String pROD) {
		PROD = pROD;
	}

	public void setPROD_FMLY(String pROD_FMLY) {
		PROD_FMLY = pROD_FMLY;
	}

	@Override
	public String toString() {
		return "CalLogData [CAL_MINUTE=" + CAL_MINUTE + ", TXN_TIME=" + TXN_TIME + ", MACH_ID=" + MACH_ID
				+ ", DATA_CTR=" + DATA_CTR + ", CAL_STATUS=" + CAL_STATUS + ", STATE=" + STATE + ", REASON=" + REASON
				+ ", FC_REASON=" + FC_REASON + ", FC_TYPE=" + FC_TYPE + ", AVAL_PLAN=" + AVAL_PLAN + ", DEF_FM="
				+ DEF_FM + ", PLANS_FM=" + PLANS_FM + ", DESCRIPTION=" + DESCRIPTION + ", CORR_ID=" + CORR_ID
				+ ", PIPELINE_NAME=" + PIPELINE_NAME + ", SUBPIPELINE_NAME=" + SUBPIPELINE_NAME + ", PROD=" + PROD
				+ ", PROD_FMLY=" + PROD_FMLY + "]";
	}
	
} 
