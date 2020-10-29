package com.nic.service.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

	private static final Logger log = LoggerFactory.getLogger(Utils.class);

	/**
	 * Return base URL (value is dynamic when on different environment - depend
	 * mainly on input parameters)
	 * 
	 * @param domainName
	 * @param subDirectory
	 */
	public static String returnBaseUrl(HttpServletRequest request, String domainName, String subDirectory) {
		StringBuilder url = new StringBuilder();
		try {
			url.append(request.getScheme()).append("://");

			if (StringUtils.isNotBlank(domainName))
				url.append(domainName);
			else {
				log.warn("Please specify the valid domain name! For now, making use of this host's IP address");
				url.append(InetAddress.getLocalHost().getHostAddress());
			}

		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}

		int port = request.getServerPort();
		if ((request.getScheme().equals("http") && port != 80)
				|| (request.getScheme().equals("https") && port != 443)) {
			url.append(':').append(port);
		}

		if (StringUtils.isNotBlank(subDirectory))
			url.append("/").append(subDirectory);

		return url.toString();
	}

}
