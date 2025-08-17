package fr.maxlego08.essentials.api.functionnals;
@FunctionalInterface
public interface ReturnBiConsumer<T, G, C> {

	/**
	 * Applies this function to the given arguments.
	 * @param t The first function argument
	 * @param g The second function argument
	 * @return A computed value
	 */
	C accept(T t, G g);
	
}