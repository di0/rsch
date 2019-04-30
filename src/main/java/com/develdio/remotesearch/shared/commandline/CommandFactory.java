package com.develdio.remotesearch.shared.commandline;

import com.develdio.remotesearch.exception.CommandException;
import com.develdio.remotesearch.shared.commandline.Parameter.PreparedParameter;
import com.develdio.remotesearch.shared.commandline.grep.GrepCommandLine;
import com.develdio.remotesearch.shared.commandline.scp.ScpCommandLine;

/**
 * This class is a factory pattern that creates a specific command.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public final class CommandFactory
{
	/**
	 * Creates the command according given parameters.
	 *
	 * @param fromCommandLine
	 *            stores the data that given the command desired
	 *
	 * @return the Command abstract created
	 * @throws CommandException
	 *             if by some reason, the command cannot be created
	 */
	public static Command
	create( final PreparedParameter fromCommandLine )
			throws CommandException
	{
		// The desired is the use of secure copy command.
		if ( fromCommandLine.isScpCommand() )
		{
			return new ScpCommandLine( fromCommandLine );
		}
		else // Otherwise, start look for remote search.
		{
			return new GrepCommandLine( fromCommandLine );
		}
	}
}
