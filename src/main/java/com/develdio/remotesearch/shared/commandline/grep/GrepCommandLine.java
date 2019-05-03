package com.develdio.remotesearch.shared.commandline.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.develdio.remotesearch.data.Data;
import com.develdio.remotesearch.data.DataContainer;
import com.develdio.remotesearch.data.DataContainerDisplay;
import com.develdio.remotesearch.exception.CommandException;
import com.develdio.remotesearch.i18n.Message;
import com.develdio.remotesearch.shared.commandline.Command;
import com.develdio.remotesearch.shared.commandline.CommandLineBase;
import com.develdio.remotesearch.shared.commandline.Parameter;
import com.develdio.remotesearch.shared.commandline.Parameter.PreparedParameter;
import com.develdio.remotesearch.shared.commandline.scp.ScpCommandLine;
import com.develdio.remotesearch.shared.endpoint.CommandResponse;
import com.develdio.remotesearch.shared.helper.Print;
import com.lognull.libnetwork.ClientWrapper;
import com.lognull.libnetwork.exception.ClientException;

/**
 * Class that executes remotely the grep command.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public final class GrepCommandLine extends CommandLineBase
		implements Command, Serializable
{
	private static final long serialVersionUID = -3998791784404884316L;

	/**
	 * Constructor default with prepared command to be used posteriorly.
	 *
	 * @param parameter
	 *        prepared parameters from command line
	 *
	 * @throws CommandException
	 *        if an error occurr when create the command
	 */
	public GrepCommandLine( final PreparedParameter parameter )
			throws CommandException
	{
		super( parameter );
	}

	/**
	 * Executes clients according their respectives endpoints.
	 *
	 * @throws CommandException
	 *        if an error occurred with execution of command
	 */
	public void
	execute() throws CommandException
	{
		final ExecutorService executor =
				Executors.newFixedThreadPool( 10 );

		final Map<ClientWrapper, Command> linkedClientCommand =
				createlinksClientWithCommand();
		for ( final Map.Entry<ClientWrapper, Command> clientMap
				: linkedClientCommand.entrySet() )
		{
			executor.submit( new Runnable() {
				public void run() {
					try
					{
						ConsumerAsync consumerAsync =
							new ConsumerAsync(
								clientMap.getKey() );
						clientMap.getKey().registerCallBack(
							consumerAsync );
						clientMap.getKey().connect();
						clientMap.getValue().execute();
					}
					catch( ClientException | CommandException e )
					{
						Print.translateOut(
							Message.ERROR_CONNECT_CLIENT,
							clientMap.getKey().
							getIpFromEndpoint(),
							e.getMessage() );
						try {
							clientMap.getKey().close();
						} catch( ClientException ignored ) {}
					}
				}
			} );
		}

		executor.shutdown();
	}

	/** Creates links of clients and their respectives commands. */
	private Map<ClientWrapper, Command> createlinksClientWithCommand()
			throws CommandException
	{
		try {
			return linksClientsWithTheirRespectivesCommands();
		} catch( CommandException e ) {
			throw new CommandException( e.getMessage() );
		}
	}

	/* 
	 * This non-static nested class acts as an message consumer
	 * asynchronous. It processes such responses, preparing it
	 * to be correctly formatted according with respectively
	 * configured parameters.
	 */
	private class ConsumerAsync implements CommandResponse
	{
		private DataContainer dataContainer = new DataContainer();
		private ClientWrapper client = null;

		ConsumerAsync( ClientWrapper client )
		{
			this.client = client;
		}

		/**
		 * Finalizes the communication with endpoint. */
		public void finish()
		{
			if ( client != null )
			{
				try {
					client.close();
				} catch ( ClientException e ) {
					Print.translateOut(
						Message.
						ERROR_TO_CLOSE_CONNECTION,
						client.
						getIpFromEndpoint() );
				}
			}
		}

		/**
		 * Shows messages received from endpoint, after was
		 * processed in the response. The output can be in
		 * normal text plain or text plain json notation. */
		public void display()
		{
			DataContainerDisplay dataDisplay =
					new DataContainerDisplay( dataContainer );
			if ( isJsonOutput() )
				dataDisplay.jsonFormat();
			else
				dataDisplay.textFormat();
		}

		/**
		 * The response that will be processed here, comes
		 * from endpoint. */
		@Override
		public void onResponse( final String response )
		{
			// It closes the connection after response
			// successfully received.
			finish();

			// An invalid response.
			if ( response == null || "".equals( response ) )
				return;

			if ( loadGrep() == null )
			{
				Print.translateOut(
					Message.GREP_COMMAND_IS_NULL );
				return;
			}

			// If color option is turn on, we use color
			// highlighting to surround the matched
			// keyword searched.
			if ( loadGrep().isColorOn() )
				Print.colorOn = true;

			// We desired search the keyword and pull the file
			// where the keyword was encountered.
			if ( isGrepWithDownload() )
			{
				StringBuffer sb = new StringBuffer();

				BufferedReader reader =
						new BufferedReader(
								new StringReader( response ) );

				String line = "";
				try {
					while ( ( line = reader.readLine() ) != null )
					{
						String[] splited = line.split( ":" );
						if ( splited.length > 1 ) {
							line = splited[ 0 ];
							if ( ! sb.toString().contains( line ) )
								sb.append( line + "," );
						}
					}
				}
				catch( Exception e ) {
					Print.translateOut( Message.INTERNAL_ERROR,
							e.getMessage() );
				}
				finally
				{
					try {
						reader.close();
					} catch( IOException ignored ) {}
				}
				String replacedSource = sb.toString()
						.replace( "\n", "" ).replace( "\r", "" );
				if ( replacedSource.charAt( replacedSource
						.length() - 1) == ',' )
				{
					replacedSource = replacedSource.substring(
							0, replacedSource.length() - 1 );
				}

				new File( client.getIpFromEndpoint() ).mkdirs();

				PreparedParameter parameter =
					new Parameter().new PreparedParameter();
				parameter.setDest( client.getIpFromEndpoint() );
				parameter.setSource( replacedSource );
				parameter.setUserName( client.getUsername() );
				parameter.setPassword( client.getPassword() );
				parameter.setServerName( client.getIpFromEndpoint() );
				ScpCommandLine scp = new ScpCommandLine( parameter );
				scp.execute();
			}

			else
			{
				Data data = new Data();
				data.addOrigin( client.getIpFromEndpoint() );
				if ( isJsonOutput() )
					data.text( response );
				else
					data.text( matchesResponse( response ) );

				dataContainer.add( data );
				display();
			}
		}

		/* Matches response with keyword searched. */
		private String matchesResponse( String response )
		{
			StringBuffer buffer = new StringBuffer();

			// Matches keyword searched.
			// String patternFrom = "(:\\d+:)\\s?("+loadGrep().
			//		getPattern()+")";

			String patternFrom = "(:\\d+:).*?[\\s\\S].*?("+loadGrep().
					getPattern()+")";

			// Is the keyword searched case insensitive? 
			Pattern patternTo;
			if ( loadGrep().getParameter().contains( "i" ) )
				patternTo = Pattern.compile( patternFrom,
						Pattern.CASE_INSENSITIVE );
			else
				patternTo = Pattern.compile( patternFrom );

			Matcher m = patternTo.matcher( response );
			while ( m.find() )
			{
				// Replace line number with the pattern ->line
				if ( m.group( 1 ) != null )
				{
					response = response.replaceAll( m.group( 1 ),
					Print.green( "->" + Print.translateOut(
						Message.LINE )
						+ " "
						+ m.group( 1 ).replaceAll( ":",
							"" ) + " " ) );
				}
				/*
				 * This ensures that, only once, will be done the
				 * replacement of the matched keyword. For example:
				 * If, hypothetically we want replace the word 'Foo'
				 * and, the string stream contains five words 'Foo',
				 * we will do the same replacement five times. To
				 * avoid this, we're only going to do it if the
				 * target word aren't into the buffer control.
				 *
				 * Therefore, after of each iteration, we will add
				 * the keyword inside the buffer for that this not
				 * need be processed again. If you have encountered
				 * a solution that treat this performatically, let
				 * me know.
				 */
				if ( ! buffer.toString().contains( m.group( 2 ) ) )
				{
					// We want highlighting the keyword matched.
					response = response.replaceAll( m.group( 2 ),
							Print.redBold( m.group( 2 ) ) );

					// Stores the matched keyword to not processed
					// it in the next iteration.
					buffer.append( m.group( 2 ) + "\n" );
				}
			}

			return response;
		}
	}
}
