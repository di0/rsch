package com.develdio.remotesearch.shared.commandline.scp;

import com.develdio.remotesearch.exception.CommandException;
import com.develdio.remotesearch.exception.ScpCommandException;
import com.develdio.remotesearch.i18n.Message;
import com.develdio.remotesearch.shared.commandline.Command;
import com.develdio.remotesearch.shared.commandline.CommandLineBase;
import com.develdio.remotesearch.shared.commandline.Parameter.PreparedParameter;
import com.develdio.remotesearch.shared.helper.Print;
import com.develdio.remotesearch.shared.helper.RemoteCopyHandlerStatus;
import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.utility.extra.ScpClient;
import com.lognull.libnetwork.utility.extra.ScpNotify;

/**
 * This class acts as the SCP(Secure Copy) utility from command line.
 * It copies files between hosts on a network through of the Secure
 * Shell(SSH) protocol.  
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @see scp man
 * @see ssh man
 * @see com.lognull.libnetwork.utility.extra
 * @since 1.0
 */
public final class ScpCommandLine extends CommandLineBase implements Command
{
	/** Source of the file that will be transfered. */
	private String src = "";

	/** Destination of the file that was transfered. */
	private String dst = "";

	/** The handling responsible by transfer. */
	private static RemoteCopyHandlerStatus
			remoteCopyHandlerStatus = null;

	/**
	 * Constructs with prepared command to be used posteriorly.
	 *
	 * @param parameter
	 *        prepared parameters from command line
	 *
	 * @throws CommandException
	 *        exception throws if an error occurr when create
	 *        the command
	 */
	public ScpCommandLine( final PreparedParameter parameter )
			throws CommandException
	{
		super( parameter );
	}

	/**
	 * Executes clients with their respectives endpoints.
	 *
	 * @throws CommandException
	 *         if an error occurred with command execution
	 */
	public void execute() throws CommandException
	{
		__tryExecute();
	}

	/* It tries to read options gets through from command
	 * line. */
	private void
	__tryExecute() throws CommandException
	{
		remoteCopyHandlerStatus =
				RemoteCopyHandlerStatus.init();

		dst = getDestination();
		String sourceSplited[] = getSource().split( "," );

		final ScpClient scp = prepareScpClientCommand();
		scp.connect();

		if ( sourceSplited.length >= 1 )
		{
			for ( int i = 0; i < sourceSplited.length; i++  )
			{
				src = sourceSplited[ i ];
				try
				{
					scp.copyFromRemote( src, dst );
					remoteCopyHandlerStatus =
							RemoteCopyHandlerStatus.init();
				}
				catch( ClientException ce )
				{
					throw new CommandException( ce.getMessage() );
				}
				Print.newLine();
			}
		}

		Print.translateOut( Message.DOWNLOAD_COMPLETE );
		scp.close();
	}

	/* Prepares the client that will use the SCP utility. */
	private ScpClient prepareScpClientCommand() throws
			CommandException
	{
		try {
			return prepareScpCommandWithNotify(
					new HandlerScpNotify() );
		} catch( ScpCommandException e ) {
			throw new CommandException( e.getMessage() );
		}
	}

	/**
	 * This nested class handles the notify received by SCP transfer.
	 */
	private static class HandlerScpNotify implements ScpNotify
	{
		/** Represents the total size already transfered. */
		private static int total;

		/**
		 * Callback invoked to set the size of the target
		 * download.
		 */
		@Override
		public void onTargetSize( long targetSize )
		{
			total = (int)targetSize;
		}

		/**
		 * Callback invoked while a transfer is still
		 * remaining.
		 */
		@Override
		public void onRemaining( long remaining )
		{
			remoteCopyHandlerStatus.update(
					(int) remaining, total );
		}
	}
}
