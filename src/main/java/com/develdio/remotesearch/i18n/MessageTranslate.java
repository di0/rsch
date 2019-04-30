package com.develdio.remotesearch.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class bundles resources that provides ways of translate
 * entries according the current value of the default locale for
 * this instance of the Java Virtual Machine.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.1
 */
public class MessageTranslate
{
	public static String bundle( Message message )
	{
		Locale defaultLocale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle(
				"GrepBundle", defaultLocale );
		return bundle.getString( ""+message );
	}

	public static String helpBundle()
	{
		Locale defaultLocale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle(
				"GrepHelpBundle", defaultLocale );
		return bundle.getString( "help" );
	}
}
