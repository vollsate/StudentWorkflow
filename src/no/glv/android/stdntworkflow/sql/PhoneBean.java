package no.glv.android.stdntworkflow.sql;

import no.glv.android.stdntworkflow.intrfc.Phone;

public class PhoneBean implements Phone {
	
	private String id;
	private long number;
	private int type;

	public PhoneBean( String id, int type ) {
		this.id = id;
		this.type = type;
	}

	@Override
	public long getNumber() {
		return number;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public void setNumber( long num ) {
		this.number = num;
	}

	@Override
	public void setID( String id ) {
		this.id = id;
	}

}
