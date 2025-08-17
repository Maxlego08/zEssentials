package fr.maxlego08.essentials.api.functionnals;

import java.util.Collection;

@FunctionalInterface
public interface CollectionConsumer<T> {

	/**
	 * Consumes the given input and returns a collection of strings.
	 * The contents of the collection depend on the implementation.
	 *
	 * @param t the input to consume
	 * @return a collection of strings
	 */
	Collection<String> accept(T t);
	
}
