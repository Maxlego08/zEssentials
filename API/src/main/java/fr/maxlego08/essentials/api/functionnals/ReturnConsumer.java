package fr.maxlego08.essentials.api.functionnals;
@FunctionalInterface
public interface ReturnConsumer<T, G> {

	/**
	 * Accepts a value of type T and returns a value of type G.
	 *
	 * @param t the value to be accepted
	 * @return the result of accepting the given value
	 */
	G accept(T t);
	
}