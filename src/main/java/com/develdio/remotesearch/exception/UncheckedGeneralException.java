package com.develdio.remotesearch.exception;

/**
 * This class encapsules errors from remote searches.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.1
 */
public class UncheckedGeneralException extends Exception
{
	private static final long serialVersionUID = -4917380540391523341L;

	public UncheckedGeneralException()
	{
		super();
	}

	public UncheckedGeneralException( String message )
	{
		super( message );
	}

	public UncheckedGeneralException ( String message,
			Throwable throwable )
	{
		super( message, throwable );
	}
}
