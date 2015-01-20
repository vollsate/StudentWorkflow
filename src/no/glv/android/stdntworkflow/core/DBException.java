package no.glv.android.stdntworkflow.core;

public class DBException extends RuntimeException {

    /** DBException.java */
    private static final long serialVersionUID = -5331569245421077680L;

    public DBException() {
	// TODO Auto-generated constructor stub
    }

    public DBException( String detailMessage ) {
	super( detailMessage );
	// TODO Auto-generated constructor stub
    }

    public DBException( Throwable throwable ) {
	super( throwable );
	// TODO Auto-generated constructor stub
    }

    public DBException( String detailMessage, Throwable throwable ) {
	super( detailMessage, throwable );
	// TODO Auto-generated constructor stub
    }

}
