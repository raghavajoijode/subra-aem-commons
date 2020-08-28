package org.subra.aem.commons.helpers;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.subra.aem.commons.constants.CookieType;
import org.subra.aem.commons.utils.SubraCollectionUtils;

public class CookieHelper {

	private static final int COOKIE_EXPIRATION = 157680000; // 5 years
	private static final String[] COOKIE_BAD_CHARACTERS = { "\r", "\n", "HTTP/1.1 200 OK" };

	public static String[] getAuthKeys(final HttpServletRequest request) {
		if (request != null && request.getCookies() != null)
			return SubraCollectionUtils.getStreamFromArray(request.getCookies())
					.filter(c -> StringUtils.equals(c.getName(), CookieType.AUTH_KEY.getName())).map(Cookie::getValue)
					.toArray(size -> new String[size]);

		return new String[] {};
	}

	public static String getAuthKey(final HttpServletRequest request) {
		return getCookieValue(request, CookieType.AUTH_KEY);
	}

	public static String getAccountId(final HttpServletRequest request) {
		return getCookieValue(request, CookieType.ACCOUNT_ID);
	}

	public static void setAuthKey(final HttpServletResponse response, final String domain, final String value,
			final boolean createSecure) {
		setCookieOfType(response, CookieType.AUTH_KEY, value, domain, COOKIE_EXPIRATION, createSecure, createSecure);
	}

	public static void setAccountId(final HttpServletResponse response, final String value,
			final boolean createSecure) {
		setCookie(response, CookieType.ACCOUNT_ID.getName(), value, null, COOKIE_EXPIRATION, createSecure, false);
	}

	public static void deleteAuthKey(final HttpServletResponse response, final String domain) {
		deleteCookie(response, CookieType.AUTH_KEY.getName(), domain);
	}

	public static void deleteAccountId(final HttpServletResponse response) {
		deleteCookieOfType(response, CookieType.ACCOUNT_ID);
	}

	private static String getCookieValue(final HttpServletRequest request, final CookieType cookieType) {
		return getCookieValue(request, cookieType.getName());
	}

	public static String getCookieValue(final HttpServletRequest request, final String cookieName) {
		if (request != null && request.getCookies() != null) {
			final Cookie cookie = Arrays.stream(request.getCookies())
					.filter(c -> StringUtils.equals(c.getName(), cookieName)).findFirst().orElse(null);
			return cookie != null ? cookie.getValue() : null;
		}
		return null;
	}

	private static void setCookieOfType(final HttpServletResponse response, final CookieType cookieType,
			final String value, final String domain, final int expiry, final boolean secure, final boolean httpOnly) {
		setCookie(response, cookieType.getName(), value, domain, expiry, secure, httpOnly);
	}

	public static void setCookie(final HttpServletResponse response, final String cookieName, final String value,
			final String domain, final int expiry, final boolean secure, final boolean httpOnly) {
		if (value != null) {

			if (httpOnly) {
				createHttpOnlyCookie(response, cookieName, domain, value, expiry, secure);
			} else {
				final Cookie cookie = new Cookie(cookieName, validateCookieValue(value));
				cookie.setValue(value);
				cookie.setMaxAge(3600);
				if (domain != null) {
					cookie.setDomain(domain);
				}
				cookie.setPath("/");
				cookie.setSecure(secure);
				response.addCookie(cookie);
			}
		}
	}

	private static void deleteCookieOfType(final HttpServletResponse response, final CookieType cookieType) {
		deleteCookie(response, cookieType.getName(), null);
	}

	public static void deleteCookie(final HttpServletResponse response, final String cookieName, final String domain) {
		setCookie(response, cookieName, StringUtils.EMPTY, domain, 0, false, false);
	}

	protected static void createHttpOnlyCookie(final HttpServletResponse response, final String cookieName,
			final String domain, final String value, final int expiry, final boolean secure) {

		final StringBuilder cookieBuilder = new StringBuilder();
		cookieBuilder.append(cookieName);
		cookieBuilder.append("=");
		cookieBuilder.append(value);

		if (expiry >= 0) {
			cookieBuilder.append("; Max-Age=");
			cookieBuilder.append(expiry);
		}

		if (!StringUtils.isEmpty(domain)) {
			cookieBuilder.append("; Domain=");
			cookieBuilder.append(domain);
		}

		cookieBuilder.append("; Path=/");

		if (secure) {
			cookieBuilder.append("; Secure");
		}

		cookieBuilder.append("; HttpOnly");

		response.addHeader("Set-Cookie", validateCookieValue(cookieBuilder.toString()));
	}

	protected static String validateCookieValue(final String value) {
		String newVal = value;
		for (int i = 0; i < COOKIE_BAD_CHARACTERS.length; i++) {
			newVal = newVal.replaceAll(COOKIE_BAD_CHARACTERS[i], StringUtils.EMPTY);
		}
		return newVal;
	}

	private CookieHelper() {
		throw new AssertionError();
	}

}
