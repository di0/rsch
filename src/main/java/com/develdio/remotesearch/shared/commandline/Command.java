package com.develdio.remotesearch.shared.commandline;

import com.develdio.remotesearch.exception.CommandException;

/**
 * This interface encapsules the behavior of a command
 * that is executed by some participant.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public interface Command 
{
	/**
	 * Executes any command.
	 *
	 * @throws CommandException
	 *             if something to occur when try
	 *             execute command
	 */
	public void execute() throws CommandException;
}
