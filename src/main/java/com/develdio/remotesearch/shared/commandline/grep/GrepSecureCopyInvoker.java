package com.develdio.remotesearch.shared.commandline.grep;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.develdio.remotesearch.shared.commandline.Parameter;
import com.develdio.remotesearch.shared.commandline.Parameter.PreparedParameter;
import com.develdio.remotesearch.shared.commandline.scp.ScpCommandLine;

public class GrepSecureCopyInvoker
{
	public void secureCopyInvoke()
	{
		/*
		StringBuffer sb = new StringBuffer( "" );
        String s = "(\\s*.*):(\\d+:)";
        Pattern p = Pattern.compile( s );
        Matcher m = p.matcher( response );
        while( m.find() )
                if ( ! sb.toString().contains( m.group( 1 ) ) )
                        sb.append( m.group( 1 ) + "," );
        String r = sb.toString().replace("\n", "").replace("\r", "");
        if ( r.charAt( r.length() - 1) == ',' )
        	r = r.substring( 0, r.length() - 1 );

        PreparedParameter parameter =
        		new Parameter().new PreparedParameter();
        new File( client.getIpFromEndpoint() ).mkdirs();

        parameter.setDest( client.getIpFromEndpoint() );

        parameter.setSource( r );
        parameter.setUserName( client.getUsername() );
        parameter.setPassword( client.getPassword() );
        parameter.setServerName( client.getIpFromEndpoint() );
        ScpCommandLine scp = new ScpCommandLine( parameter );
        scp.execute();*/
	}
}
