package com.develdio.remotesearch.exception;

/**
 * This class encapsules errors from remote searches.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.1
 */
public class GrepCommandException extends CommandException
{
	private static final long serialVersionUID = -4917380540391523341L;

	public GrepCommandException()
	{
		super();
	}

	public GrepCommandException( String message )
	{
		super( message );
	}

	public GrepCommandException ( String message, Throwable throwable )
	{
		super( message, throwable );
	}
}
