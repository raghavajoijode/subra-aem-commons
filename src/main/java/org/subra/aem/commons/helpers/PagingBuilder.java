package org.subra.aem.commons.helpers;

import java.util.LinkedList;
import java.util.List;

import org.subra.aem.commons.beans.Paging;

public final class PagingBuilder {

	public static final String DEF_ELLIPSES = "...";
	public static final int DEF_SHOWN_NUMS = 5;

	private static final String RECORD_RANGE_FORM = "%s - %s";
	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static final int TWO = 2;

	private PagingBuilder() {
		throw new IllegalStateException(this.getClass().getSimpleName());
	}

	public static Paging build(final int page, final int size, final int records) {
		return build(page, size, records, DEF_SHOWN_NUMS, DEF_ELLIPSES);
	}

	public static Paging build(final int page, final int size, final int records, final int shown,
			final String ellipses) {
		int pages = Math.max(totalPages(records, size), ONE);
		Paging paging = new Paging();
		paging.setPage(page);
		paging.setSize(size);
		paging.setPages(pages);
		paging.setTotalRecords(records);
		paging.setFirstPage(page == ONE);
		paging.setLastPage(lastPage(page, pages, records));
		paging.setRangeStart(rangeStart(page, size));
		paging.setRangeEnd(rangeEnd(page, size, records));
		paging.setRecordRange(recordRange(page, size, records));
		paging.setList(buildList(page, pages, shown, ellipses));
		return paging;
	}

	private static int totalPages(final int records, final int size) {
		return records / size + (records % size == ZERO ? ZERO : ONE);
	}

	private static int rangeStart(final int page, final int size) {
		return (page - ONE) * size + ONE;
	}

	private static int rangeEnd(final int page, final int size, final int records) {
		return Math.min(page * size, records);
	}

	private static String recordRange(final int page, final int size, final int records) {
		return records == 0 ? String.valueOf(ZERO)
				: String.format(RECORD_RANGE_FORM, rangeStart(page, size), rangeEnd(page, size, records));
	}

	private static List<String> buildList(final int page, final int pages, final int shown, final String ellipses) {
		List<String> pagingList = new LinkedList<>();
		addStr(pagingList, ONE);
		if (pages <= ONE) {
			return pagingList;
		}
		int standardEdgeSize = Math.floorDiv(shown, TWO);
		int adjustedLeadingSize = adjustEdgeSize(pages - page, standardEdgeSize) - ONE;
		int adjustedTrailingSize = adjustEdgeSize(page - ONE, standardEdgeSize) - ONE;
		int startDigit = Math.max(page - adjustedLeadingSize, TWO);
		int endDigit = Math.min(page + adjustedTrailingSize, pages - ONE);
		if (pages > shown && TWO < startDigit) {
			addStr(pagingList, ellipses);
		}
		for (int curPage = startDigit; curPage <= endDigit; curPage++) {
			addStr(pagingList, curPage);
		}
		if (pages > endDigit + ONE) {
			addStr(pagingList, ellipses);
		}
		addStr(pagingList, pages);
		return pagingList;
	}

	private static void addStr(final List<String> list, final Object value) {
		list.add(String.valueOf(value));
	}

	private static int adjustEdgeSize(final int distanceFromEdge, final int edgeSize) {
		return distanceFromEdge < edgeSize ? edgeSize + (edgeSize - distanceFromEdge) : edgeSize;
	}

	private static boolean lastPage(final int page, final int pages, final int records) {
		return page == pages || records == 0;
	}

}
