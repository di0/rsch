package com.develdio.remotesearch.shared.commandline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.develdio.remotesearch.exception.CommandException;
import com.develdio.remotesearch.exception.ScpCommandException;
import com.develdio.remotesearch.i18n.Message;
import com.develdio.remotesearch.shared.Property;
import com.develdio.remotesearch.shared.commandline.Parameter.PreparedParameter;
import com.develdio.remotesearch.shared.endpoint.EndpointFactory;
import com.develdio.remotesearch.shared.helper.Print;
import com.develdio.remotesearch.shared.helper.UtilityWrapper.Grep;
import com.develdio.remotesearch.shared.helper.UtilityWrapper.Scp;
import com.lognull.libnetwork.ClientWrapper;
import com.lognull.libnetwork.consumer.ClientFactory;
import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.producer.EndpointInfo;
import com.lognull.libnetwork.utility.extra.ScpNotify;

/**
 * This class builds a base that expose operations that do tasks with
 * the parameters gets from command line.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public abstract class CommandLineBase
{
	final private PreparedParameter fromCommandLine;

	/**
	 * Builds with prepared parameters.
	 *
	 * @param fromCommandLine
	 *            values coming from command line.
	 * @throws CommandException
	 *             throws if there is some errors when try read values
	 *             from command line.
	 */
	public CommandLineBase( final PreparedParameter fromCommandLine )
			throws CommandException
	{
		if ( fromCommandLine == null )
		{
			throw new CommandException( Print.translateOut(
					Message.CANNOT_NULL,
					"fromCommandLine" ) );
		}

		this.fromCommandLine = fromCommandLine;
	}

	/** @return source directory */
	protected String getSource()
	{
		return fromCommandLine.getSource();
	}

	/** @return directory destination */
	protected String getDestination()
	{
		return fromCommandLine.getDest();
	}

	/** @return server name from endpoint */
	protected String getServerName()
	{
		return fromCommandLine.getServerName();
	}

	/** @return server name from endpoint */
	protected String getUserName()
	{
		return fromCommandLine.getUserName();
	}

	/** @return server name from endpoint */
	protected String getPassword()
	{
		return fromCommandLine.getPassword();
	}

	/** @return entity which represents the Grep */
	protected Grep loadGrep()
	{
		return fromCommandLine.loadsGrep();
	}

	/** @return entity which represents the SCP */
	protected Scp loadScp()
	{
		return fromCommandLine.loadsScp();
	}

	/** @return server name from endpoint */
	protected boolean isGrepWithDownload()
	{
		return fromCommandLine.isGrepWithDownload();
	}

	/** @return true if is json, false otherwise */
	protected boolean isJsonOutput()
	{
		return fromCommandLine.isJsonOuput();
	}

	/** @return file configuration name */
	protected String getFilename()
	{
		return fromCommandLine.getFileName();
	}

	/**
	 * @return true if has multiples endpoints to search
	 * into, false otherwise
	 */
	protected boolean hasMultiplesEndpoints()
	{
		return fromCommandLine.hasMultiplesEndpoints();
	}

	/** @return map that represent the file properties */
	protected Map<String, List<Property>>
	getPropertiesFile()
	{
		return fromCommandLine.getPropertiesFile();
	}

	/**
	 * This method links one or more clients with your respective command.
	 *
	 * @return the map with Clients and their respective related command.
	 * @throws CommandException
	 *         if an error have occurred when one or more clients were
	 *         wrong created in attempt of to link with your command
	 * @since 1.1
	 */
	protected Map<ClientWrapper, Command>
	linksClientsWithTheirRespectivesCommands() throws CommandException
	{
		Map<ClientWrapper, Command> linksClientCommand =
				new HashMap<>();

		if ( fromCommandLine.isGrepCommand() )
		{
			if ( fromCommandLine.hasMultiplesEndpoints() )
			{
				for ( Map.Entry<String, List<Property>> entry :
					fromCommandLine.getPropertiesFile().entrySet() )
				{
					if ( ( fromCommandLine.hasProfile() ) &&
							( ! isActiveProfile( entry.getKey() ) ) )
						continue;

					for ( Property property : entry.getValue() )
					{
						final Grep grep = new Grep();

						for ( String directory : property.getDirectories() )
							grep.addDirectory( directory );

						grep.addPattern( fromCommandLine.loadsGrep().
								getPattern() );
						grep.addParameter( fromCommandLine.loadsGrep().
								getParameter() );

						/*
						fromCommandLine.setUserName( property.getUser() );
						fromCommandLine.setPassword( property.getPassword() );
						fromCommandLine.setServerName( property.getHostname() );
						*/

						final EndpointInfo endpoint = EndpointFactory.create(
								property.getHostname(), property.getUser(),
								property.getPassword() );
						final ClientWrapper client =
								_tryCreateClientToEndpoint( endpoint );
						linKsGrepCommand( linksClientCommand, client, grep );
					}
				}
			}
			else
			{
				final EndpointInfo endpoint = EndpointFactory.
						create( fromCommandLine );
				final ClientWrapper client =
						_tryCreateClientToEndpoint( endpoint );
				linKsGrepCommand( linksClientCommand, client,
						fromCommandLine.loadsGrep() );
			}
		}

		return linksClientCommand;
	}

	/**
	 * Creates the SCP command and register the given
	 * notify that will receive the messages that
	 * arrives.
	 *
	 * @param notify
	 *            who is interested receive the
	 *            response
	 * @return the SCP command prepared.
	 * @throws ScpCommandException
	 *             if is not possible to create the SCP
	 *             command
	 * @since 1.1
	 */
	protected com.lognull.libnetwork.utility.extra.Scp
	prepareScpCommandWithNotify( final ScpNotify notify )
			throws ScpCommandException
	{
		try {
			EndpointInfo endpoint = EndpointFactory.
					create( fromCommandLine );

			return ( new com.lognull.libnetwork.utility
					.extra.Scp( endpoint, notify ) );
		}
		catch( ClientException ce ) {
			throw new ScpCommandException(
					ce.getMessage() );
		}
	}

	/** Links specific grep command with a Client. */
	private void
	linKsGrepCommand(
			Map<ClientWrapper, Command> linksClientCommand,
			final ClientWrapper client, final Grep grep )
	{
		linksClientCommand.put( client, new Command() {
			public void execute() throws CommandException {
				try {
					if ( grep.isVerbose() )
						client.send( grep + "" );
					else
						client.send( grep +
							" 2> /dev/null" );
				} catch( ClientException ce ) {
					throw new CommandException(
						Print.translateOut
						(
							Message.
							COULD_NOT_SEND_MESSAGE,

							client.
							getIpFromEndpoint(),

							ce.
							getMessage()
						)
					);
				}
			}
		} );
	}

	/**
	 * Creates relationship between given endpoint with a
	 * Client. */
	private ClientWrapper
	_tryCreateClientToEndpoint( final EndpointInfo endpoint )
			throws CommandException
	{
		try {
			return new ClientWrapper( ClientFactory.
					createByEndpoint( endpoint ) );
		} catch ( ClientException ce ) {
			throw new CommandException( ce.getMessage() );
		}
	}

	/** Checks if given profile is active/available. */
	private boolean isActiveProfile( final String profile )
	{
		int i = 0;
		while( i < fromCommandLine.getProfiles().length ) {
			if ( profile.equals( fromCommandLine.
					getProfiles()[ i ] ) )
				return true;
			i++;
		}

		return false;
	}
}
