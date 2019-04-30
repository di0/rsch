package com.develdio.remotesearch.exception;

/**
 * This class encapsules errors from execute command.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.1
 */
public class CommandException extends RuntimeException
{
	private static final long serialVersionUID = -4917380540391523341L;

	public CommandException()
	{
		super();
	}

	public CommandException( String message )
	{
		super( message );
	}

	public CommandException( String message,
			Throwable throwable )
	{
		super( message, throwable );
	}
}
