package fr.maxlego08.essentials.api.functionnals;
@FunctionalInterface
public interface ReturnBiConsumer<T, G, C> {

	C accept(T t, G g);
	
}