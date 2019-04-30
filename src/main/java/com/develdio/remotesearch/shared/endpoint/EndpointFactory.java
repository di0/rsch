package com.develdio.remotesearch.shared.endpoint;

import com.develdio.remotesearch.shared.commandline.Parameter.PreparedParameter;
import com.lognull.libnetwork.producer.EndpointInfo;
import com.lognull.libnetwork.protocol.Protocol;

/**
 * This class factories informations which it relates with some endpoint.
 * The client side will use soon later, this informations.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class EndpointFactory
{
	/** We want this constructor private. We use factory method. */
	private EndpointFactory() {}

	/**
	 * Creates informations about the endpoint used by a specific
	 * client.
	 *
	 * @param fromCommandLine
	 *            data where are parameters to create the new
	 *            endpoint
	 * @return The endpoint builds with the information given
	 *         through command line parameters
	 * @since 1.0
	 */
	public static EndpointInfo
	create( final PreparedParameter fromCommandLine )
	{
		return ( _create(
				fromCommandLine.getServerName(),
				fromCommandLine.getUserName(),
				fromCommandLine.getPassword() ) );
	}

	/**
	 * Creates informations about the endpoint used by a specific client.
	 *
	 * @param ip
	 *            Address used by endpoint
	 * @param user
	 *            The credential user to login in the endpoint
	 * @param password
	 *            The credential password to login in the endpoint
	 *
	 * @return The endpoint builds with the information given through the ip,
	 *         user and password
	 * @since 1.1
	 */
	public static EndpointInfo
	create( String ip, String user, String password )
	{
		return _create( ip, user, password );
	}

	private static EndpointInfo
	_create( String ip, String user, String password )
	{
		EndpointInfo endpoint = new EndpointInfo();
		endpoint.setIp( ip );
		endpoint.setPort( 22 );
		endpoint.setProtocol( Protocol.SSH );

		EndpointInfo.Credential credential =
				endpoint.new Credential();
		credential.setUser( user );
		credential.setPassword( password );
		endpoint.setCredential( credential );

		return endpoint;
	}
}
