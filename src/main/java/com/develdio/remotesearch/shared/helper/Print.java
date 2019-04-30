package com.develdio.remotesearch.shared.helper;

import org.fusesource.jansi.AnsiConsole;

import com.develdio.remotesearch.i18n.Message;
import com.develdio.remotesearch.i18n.MessageTranslate;

/**
 * This class wrappers and abstract all patterns used by
 * streams of output and input.
 * 
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class Print
{	
	public static boolean colorOn = false;

	private static final String RESET	= "\u001B[0m";
	private static final String BLACK	= "\u001B[30m";
	private static final String RED		= "\u001B[31m";
	private static final String RED_B	= "\u001B[1;41;33m";
	private static final String GREEN	= "\u001B[32m";
	private static final String GREEN_B	= "\u001B[7;32;40m";
	private static final String YELLOW	= "\u001B[33m";
	private static final String BLUE	= "\u001B[34m";
	private static final String PURPLE	= "\u001B[35m";
	private static final String CYAN	= "\u001B[36m";
	private static final String WHITE	= "\u001B[37m";
	private static final String WHITE_B	= "\u001B[1;37m";

	/**
	 * This method is responsible by turn on the output syntax color
	 * highlighting for passed input using a format.
	 *
	 * @param format
	 *            A format for numeric, string, and date/time...
	 * @param o
	 *            the given input that will be surround by color
	 *            highlighting
	 */
	public static void
	formmatedText( String format, Object... o )
	{
		if ( System.getProperty( "os.name" ).toLowerCase().
				startsWith( "win" ) )
			AnsiConsole.out.printf( format, o );
		else
			System.out.printf( format, o );
	}

	/**
	 * This method is responsible by turn on the output syntax
	 * color highlighting to given input.
	 *
	 * @param s
	 *           the given input that will be surround by color
	 *           highlighting
	 */
	public static void text( String s )
	{
		if ( System.getProperty( "os.name" ).toLowerCase()
				.startsWith( "win" ) )
			AnsiConsole.out.println( s );
		else
			System.out.println( s );
	}

	/**
	 * This method translates the message using some format
	 * stored in 'm' parameter, obtained of arguments given
	 * by 'o' parameter and surround it with syntax color
	 * highlighting desired according with given parameter
	 * 'c'.
	 *
	 * @param c
	 *            the color that input will be colored
	 *            highlighting
	 * @param m
	 *            the message with format that will be
	 *            translated
	 * @param o
	 *            the arguments that will passed to
	 *            message
	 * @return    the message formatted string with
	 *            surround by color highlighting
	 * @since 1.1
	 */
	public static String
	translateOut( String c, Message m, Object ...o )
	{
		if ( System.getProperty( "os.name" ).
				toLowerCase().
				startsWith( "win" ) )
		{
			return AnsiConsole.out.printf(
				c + m.toString() + RESET, o ).
				toString();
		}
		else
		{
			return System.out.printf(
				c + MessageTranslate.
				bundle( m ) + RESET, o ).
				toString();
		}
	}

	/**
	 * This method translates the message using some format
	 * stored in 'm' parameter, obtained of arguments given
	 * by 'o' parameter.
	 *
	 * @param m
	 *            the message with format that will be
	 *            translated
	 * @param o
	 *            the arguments that will passed to
	 *            message
	 * @return the message formatted string
	 * @since 1.1
	 */
	public static String
	translateOut( Message m, Object ...o )
	{
		return System.out.printf(
			MessageTranslate.bundle( m ), o ).
			toString();
	}

	/**
	 * This method translates the message using some format
	 * stored in 'm' parameter, obtained of arguments given
	 * by 'o' parameter and soon later exit.
	 *
	 * @param m
	 *            the message with format that will be
	 * @param o
	 *            the arguments that will passed to message
	 * @since 1.1
	 */
	public static void
	translateOutAndExit( Message m, Object ...o )
	{
		translateOut( m, o );
		System.exit( 1 );
	}

	/**
	 * This method translates the message.
	 *
	 * @param m
	 *            the message that will be translated
	 * @return the message translated
	 */
	public static String translateOut( Message m )
	{
		return MessageTranslate.bundle( m );
	}

	/**
	 * This method prints a string without any formatting.
	 *
	 * @param s
	 * 			The string that will be printed without
	 *          formatting.
	 */
	public static void
	textWithoutFormatting( String s )
	{
		System.out.println( s );
	}

	/**
	 * Provides system to log errors messages.
	 *
	 * @param s
	 *            The string output
	 * @param c
	 *            The instances of the class that
	 *            represent who will log the errors
	 *            messages.
	 */
	public static void
	error( String s, Class<?> c )
	{
		LoggerHelper.thisClass( c ).error( s );
	}

	/**
	 * Surrounds the string "s" with the ANSI
	 * color red.
	 *
	 * @param s
	 *          string that will be surround
	 * @return	the string surrounded
	 */
	public static String
	red( String s )
	{
		if ( colorOn )
			return( RED + s + RESET );
		return s;
	}

	/**
	 * Surrounds the string "s" with the ANSI
	 * color red bold.
	 *
	 * @param s
	 *          string that will be surround
	 * @return	the string surrounded
	 */
	public static String redBold( String s )
	{
		if ( colorOn )
			return( RED_B + s + RESET );
		return s;
	}

	/**
	 * Surrounds the string "s" with the ANSI
	 * color black.
	 *
	 * @param s
	 *          string that will be surround
	 * @return	the string surrounded
	 */
	public static String black( String s )
	{
		if ( colorOn )
			return( BLACK + s + RESET );
		return s;
	}

	/**
	 * Surrounds the string "s" with the ANSI
	 * color cyan.
	 *
	 * @param s
	 *          string that will be surround
	 * @return	the string surrounded
	 */
	public static String cyan( String s )
	{
		if ( colorOn )
			return( CYAN + s + RESET );
		return s;
	}

	/**
	 * Surrounds the string "s" with the ANSI
	 * color purple.
	 *
	 * @param s
	 *          string that will be surround
	 * @return	the string surrounded
	 */
	public static String purple( String s )
	{
		return( PURPLE + s + RESET );
	}

	/**
	 * Surrounds the string "s" with the ANSI
	 * color green.
	 *
	 * @param s
	 *          string that will be surround
	 * @return	the string surrounded
	 */
	public static String green( String s )
	{
		if ( colorOn )
			return( GREEN + s + RESET );
		return s;
	}

	/**
	 * Surrounds the string "s" with the ANSI
	 * color green and black.
	 *
	 * @param s
	 *          string that will be surround
	 * @return	the string surrounded
	 */
	public static String greenAndBlack( String s )
	{
		return ( GREEN_B + s + RESET );
	}

	/**
	 * Surrounds the string "s" with the ANSI
	 * color yellow.
	 *
	 * @param s
	 *          string that will be surround
	 * @return	the string surrounded
	 */
	public static String yellow( String s )
	{
		if ( colorOn )
			return( YELLOW + s + RESET );
		return s;
	}

	/**
	 * @return simply the color yellow.
	 * @since 1.1
	 */
	public static String getYellowColor()
	{
		return YELLOW;
	}

	/**
	 * @return simply the color yellow.
	 * @since 1.1
	 */
	public static String getBlueColor()
	{
		return BLUE;
	}

	/**
	 * @return simply the color yellow.
	 * @since 1.1
	 */
	public static String getGreenColor()
	{
		return GREEN;
	}

	/**
	 * This method surrounds a string with yellow
	 * color. This method acts as warning system
	 * message.
	 *
	 * @param s
	 *            string that will be surround
	 * @return the string surrounded
	 * @since 1.1
	 */
	public static String warning( String s )
	{
		return( YELLOW + s + RESET );
	}

	/**
	 * Adds a new line.
	 */
	public static void newLine()
	{
		System.out.println( "" );
	}

	/**
	 * @return returns a new line.
	 */
	public static String getNewLine()
	{
		return "\n";
	}

	/**
	 * Prints s and exit.
	 *
	 * @param s
	 *    the string that will be printed
	 */
	public static void andExit( String s )
	{
		System.out.println( s );
		System.exit( 1 );
	}

	/**
	 * Flushes the stream.
	 */
	public static void flush()
	{
		System.out.flush();
	}

	/**
	 * This method prints sharps symbols. It helps to
	 * separate contents between one or more text.
	 */
	public static void hashTag()
	{
		System.out.println( WHITE + "\n###########################"
				+ "##############################" + RESET );
	}

	/**
	 * This method prints arrobas symbols. It helps to
	 * separate contents between one or more text.
	 *
	 * @since 1.1
	 */
	public static void markLineSeparation()
	{
		System.out.println( "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" );
	}

	/**
	 * This method prints the syntax used by this app.
	 *
	 * @since 1.1
	 */
	public static void syntax()
	{
		Print.translateOutAndExit( Message.APP_SYNTAX,
				WHITE_B, RESET );
	}

	/**
	 * This method prints the help menu used by this app.
	 */
	public static void help()
	{
		Print.text( _help() );
		Print.andExit( "" );
	}

	/* In fact, the help menu. */
	private static String _help()
	{
		return MessageTranslate.helpBundle();
	}
}
