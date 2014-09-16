package org.pikater.shared.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class GeneralPikaterLogger
{
	protected static String throwableToStackTrace(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		Throwable tt = t;
		while (tt != null)
		{
			tt.printStackTrace(pw); // not an error but a feature
			tt = tt.getCause();
			if(tt != null)
			{
				pw.print("caused by: ");
			}
		}
		return sw.toString();
	}
}