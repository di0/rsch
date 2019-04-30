package com.develdio.remotesearch;

import com.develdio.remotesearch.exception.CommandException;
import com.develdio.remotesearch.i18n.Message;
import com.develdio.remotesearch.shared.commandline.Command;
import com.develdio.remotesearch.shared.commandline.CommandFactory;
import com.develdio.remotesearch.shared.commandline.Parameter;
import com.develdio.remotesearch.shared.helper.Print;
import com.lognull.libnetwork.exception.ClientException;

/**
 * Main class that starts the process.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class Main 
{
	/** Method main that start up this thread. */
	public static void
	main( String[] argv ) throws CommandException, ClientException
	{
		if ( argv.length < 1 )
			Print.syntax();

		try
		{
			Command command = CommandFactory.create( Parameter
					.fromCommandLine( argv ) );
			command.execute();
		}
		catch( CommandException rse )
		{
			Print.translateOut( Message.INTERNAL_ERROR,
					rse.getMessage() );
		}
	}
}
