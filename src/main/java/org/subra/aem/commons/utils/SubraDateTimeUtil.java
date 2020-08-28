package org.subra.aem.commons.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class SubraDateTimeUtil {

	private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	private SubraDateTimeUtil() {
		throw new IllegalStateException(this.getClass().getSimpleName());
	}

	public static LocalDateTime localDateTime(String date, String pattern, Locale locale) {
		if (StringUtils.isNotBlank(date)) {
			return LocalDateTime.parse(date, createDateTimeFormatter(pattern, locale, true));
		} else {
			return LocalDateTime.now();
		}
	}

	public static LocalDateTime localDateTime(String date, String pattern) {
		return localDateTime(date, pattern, Locale.ENGLISH);
	}

	public static LocalDateTime localDateTime(String date) {
		return localDateTime(date, DEFAULT_DATE_TIME_PATTERN, Locale.ENGLISH);
	}

	public static LocalDateTime localDateTime() {
		return localDateTime(null, DEFAULT_DATE_TIME_PATTERN, Locale.ENGLISH);
	}

	public static LocalDate localDate(String date, String pattern, Locale locale) {
		if (StringUtils.isNotBlank(date)) {
			return LocalDate.parse(date, createDateTimeFormatter(pattern, locale, false));
		} else {
			return LocalDate.now();
		}
	}

	public static LocalDate localDate(String date, String pattern) {
		return localDate(date, pattern, Locale.ENGLISH);
	}

	public static LocalDate localDate(String date) {
		return localDate(date, DEFAULT_DATE_PATTERN, Locale.ENGLISH);
	}

	public static LocalDate localDate() {
		return localDate(null, DEFAULT_DATE_PATTERN, Locale.ENGLISH);
	}

	public static String localDateTimeString(LocalDateTime localDateTime, String pattern, Locale locale) {
		DateTimeFormatter dateTimeFormatter = createDateTimeFormatter(pattern, locale, true);
		if (localDateTime != null) {
			return localDateTime.format(dateTimeFormatter);
		} else {
			return LocalDateTime.now().format(dateTimeFormatter);
		}
	}

	public static String localDateTimeString() {
		return localDateTimeString(LocalDateTime.now(), DEFAULT_DATE_TIME_PATTERN, Locale.ENGLISH);
	}

	public static String localDateTimeString(String pattern) {
		return localDateTimeString(LocalDateTime.now(), pattern, Locale.ENGLISH);
	}

	public static String localDateTimeString(LocalDateTime localDateTime) {
		return localDateTimeString(localDateTime, DEFAULT_DATE_TIME_PATTERN, Locale.ENGLISH);
	}

	public static String localDateTimeString(LocalDateTime localDateTime, String pattern) {
		return localDateTimeString(localDateTime, pattern, Locale.ENGLISH);
	}

	public static String localDateString(LocalDate localDate, String pattern, Locale locale) {
		DateTimeFormatter dateTimeFormatter = createDateTimeFormatter(pattern, locale, false);
		if (localDate != null) {
			return localDate.format(dateTimeFormatter);
		} else {
			return LocalDate.now().format(dateTimeFormatter);
		}
	}

	public static String localDateString(LocalDate localDate, String pattern) {
		return localDateString(localDate, pattern, Locale.ENGLISH);
	}

	public static String localDateString(LocalDate localDate) {
		return localDateString(localDate, DEFAULT_DATE_PATTERN, Locale.ENGLISH);
	}

	public static String localDateString() {
		return localDateString(null, DEFAULT_DATE_PATTERN, Locale.ENGLISH);
	}

	public static String dateToStringWithTime(Date date) {
		return localDateTimeString(dateToLocalDateTime(date));
	}

	public static String dateToString(Date date) {
		return localDateString(dateToLocalDate(date));
	}

	public static LocalDateTime dateToLocalDateTime(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static LocalDate dateToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime localDateTimeAtZone(LocalDateTime ldt, ZoneId zoneId) {
		return ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(zoneId != null ? zoneId : ZoneId.systemDefault())
				.toLocalDateTime();
	}

	public static String localDateTimeStringAtZone(LocalDateTime ldt, ZoneId zoneId) {
		return localDateTimeString(localDateTimeAtZone(ldt, zoneId));
	}

	public static LocalDateTime localDateTimeAtZone(ZoneId zoneId) {
		return localDateTimeAtZone(LocalDateTime.now(), zoneId);
	}

	public static String localDateTimeStringAtZone(ZoneId zoneId) {
		return localDateTimeString(localDateTimeAtZone(zoneId));
	}

	public static ZonedDateTime zonedDateTime(LocalDateTime ldt, ZoneId zoneId) {
		return ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(zoneId != null ? zoneId : ZoneId.systemDefault());
	}

	public static ZonedDateTime zonedDateTime(ZoneId zoneId) {
		return zonedDateTime(LocalDateTime.now(), zoneId);
	}

	public static ZonedDateTime zonedDateTime() {
		return zonedDateTime(ZoneId.systemDefault());
	}

	public static ZonedDateTime zonedDateTimeAtUTC(LocalDateTime ldt) {
		return zonedDateTime(ldt, ZoneIdUtil.UTC.zone());
	}

	public static ZonedDateTime zonedDateTimeAtUTC() {
		return zonedDateTimeAtUTC(LocalDateTime.now());
	}

	public static Calendar nowInCalendar() {
		return toCalendar(localDateTime(), null);
	}

	public static Calendar nowAtUTCInCalendar() {
		return toCalendar(localDateTime(), ZoneIdUtil.UTC.zone());
	}

	public static Calendar toCalendar(LocalDateTime ldt, ZoneId zoneId) {
		return GregorianCalendar.from(zonedDateTime(ldt, zoneId));
	}

	public static ZonedDateTime toZonedDateTime(Calendar calendar) {
		if (calendar == null)
			return ZonedDateTime.now();
		return calendar.toInstant().atZone(ZoneId.systemDefault());
	}

	public static LocalDateTime toLocalDateTime(Calendar calendar) {
		return toZonedDateTime(calendar).toLocalDateTime();
	}

	public static String toLocalDateTimeString(Calendar calendar) {
		return localDateTimeString(toLocalDateTime(calendar));
	}

	public static LocalDateTime localDateTimeAtUTC(LocalDateTime ldt) {
		return ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneIdUtil.UTC.zone()).toLocalDateTime();
	}

	public static LocalDateTime localDateTimeAtUTC() {
		return localDateTimeAtUTC(LocalDateTime.now());
	}

	public static String localDateTimeStringAtUTC(LocalDateTime ldt) {
		return localDateTimeString(localDateTimeAtUTC(ldt));
	}

	public static String localDateTimeStringAtUTC() {
		return localDateTimeString(localDateTimeAtUTC());
	}

	public static int getCurrentYear() {
		return LocalDateTime.now().getYear();
	}

	public static int getCurrentMonth() {
		return LocalDateTime.now().getMonthValue();
	}

	public static int getCurrentDayOfMonth() {
		return LocalDateTime.now().getDayOfMonth();
	}

	public static int getCurrentHour() {
		return LocalDateTime.now().getHour();
	}

	public static int getCurrentMinute() {
		return LocalDateTime.now().getMinute();
	}

	public static DateTimeFormatter createDateTimeFormatter(final String pattern, final Locale locale,
			final boolean isDateTime) {
		final String defaultPattern = isDateTime ? DEFAULT_DATE_TIME_PATTERN : DEFAULT_DATE_PATTERN;
		return DateTimeFormatter.ofPattern(StringUtils.isNotBlank(pattern) ? pattern : defaultPattern,
				StringUtils.isNotBlank(locale.toString()) ? locale : Locale.ENGLISH);
	}

	public enum ZoneIdUtil {
		ACT("Australia/Darwin"), AET("Australia/Sydney"), AGT("America/Argentina/Buenos_Aires"), ART("Africa/Cairo"),
		AST("America/Anchorage"), BET("America/Sao_Paulo"), BST("Asia/Dhaka"), CAT("Africa/Harare"),
		CNT("America/St_Johns"), CST("America/Chicago"), CTT("Asia/Shanghai"), EAT("Africa/Addis_Ababa"),
		ECT("Europe/Paris"), IET("America/Indiana/Indianapolis"), IST("Asia/Kolkata"), JST("Asia/Tokyo"),
		MIT("Pacific/Apia"), NET("Asia/Yerevan"), NST("Pacific/Auckland"), PLT("Asia/Karachi"), PNT("America/Phoenix"),
		PRT("America/Puerto_Rico"), PST("America/Los_Angeles"), SST("Pacific/Guadalcanal"), VST("Asia/Ho_Chi_Minh"),
		EST("-05:00"), MST("-07:00"), HST("-10:00"), UTC("UTC");

		private String value;

		ZoneIdUtil(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}

		public ZoneId zone() {
			return ZoneId.of(value);
		}
	}

}
