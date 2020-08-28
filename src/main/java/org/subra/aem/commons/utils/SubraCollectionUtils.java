package org.subra.aem.commons.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author Raghava Joijode
 *
 */
public class SubraCollectionUtils extends CollectionUtils {

	private SubraCollectionUtils() {
		throw new IllegalStateException(this.getClass().getSimpleName());
	}

	/**
	 * @param
	 * @return
	 */
	public static <T> Predicate<T> distinctByKey(final Function<? super T, ?> keyExtractor) {
		final Set<Object> seen = new HashSet<>();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public static <T> Stream<T> getStreamFromArray(T[] array) {
		return Arrays.stream(array);
	}

	public static <T> T[] toArray(List<T> list, T[] init) {
		return list.toArray(init);
	}

	public static <T> List<T> toList(T[] array) {
		return Arrays.asList(array);
	}

}