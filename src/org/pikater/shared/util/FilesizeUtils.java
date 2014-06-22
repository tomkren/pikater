package org.pikater.shared.util;

import java.text.DecimalFormat;

public class FilesizeUtils
{
	//Source: http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
	public static String formatFileSize(long size)
	{
		if(size <= 0) return "0";
		final String[] units = new String[] { "B", "KiB", "MiB", "GiB", "TiB" };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
