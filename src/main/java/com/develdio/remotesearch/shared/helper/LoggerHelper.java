package com.develdio.remotesearch.shared.helper;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * This class encapsulates the logger of the system.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public final class LoggerHelper
{
	private static Logger logger = null;
	private boolean systemDebugActive = false;

	private LoggerHelper( Class<?> clazz )
	{
		String debug = System.getProperty( "debug" );
		if ( null != debug )
		if ( "true".equalsIgnoreCase( debug ) )
			systemDebugActive = true;

		LoggerHelper.logger = LogManager.getLogger( clazz );
	}

	public static LoggerHelper thisClass( final Class<?> clazz )
	{
		return new LoggerHelper( clazz );
	}

	public void info( final String message )
	{
		if ( logger.isInfoEnabled() )
			logger.info( message );
	}

	public void info( final Object message )
	{
		if ( logger.isInfoEnabled() )
			logger.info( message );
	}

	public void debug( final String message )
	{
		if ( logger.isDebugEnabled() && systemDebugActive )
			logger.debug( message );
	}

	public void warn( final String message )
	{
		logger.warn( message );
	}

	public void error( final String message )
	{
		logger.error( message );
	}

	public void error( final String message, final Throwable t )
	{
		logger.error( message, t );
	}

	public void error( final Object message )
	{
		logger.error( message );
	}

	public void error( final Object message, final Throwable t )
	{
		logger.error( message, t );
	}
}
