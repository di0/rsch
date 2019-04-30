package com.develdio.remotesearch.exception;

/**
 * This class encapsules errors from SCP command.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.1
 */
public class ScpCommandException extends CommandException
{
	private static final long serialVersionUID =
			-4917380540391523341L;

	public ScpCommandException()
	{
		super();
	}

	public ScpCommandException( String message )
	{
		super( message );
	}

	public ScpCommandException ( String message,
			Throwable throwable )
	{
		super( message, throwable );
	}
}
