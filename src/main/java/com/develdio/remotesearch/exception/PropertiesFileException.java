package com.develdio.remotesearch.exception;

/**
 * This class encapsules errors from properties files.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.1
 */
public class PropertiesFileException extends Exception
{
	private static final long serialVersionUID = -4917380540391523341L;

	public PropertiesFileException()
	{
		super();
	}

	public PropertiesFileException( String message )
	{
		super( message );
	}

	public PropertiesFileException ( String message, Throwable throwable )
	{
		super( message, throwable );
	}
}
