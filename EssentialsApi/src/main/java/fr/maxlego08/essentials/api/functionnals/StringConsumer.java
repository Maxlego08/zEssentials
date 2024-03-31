package fr.maxlego08.essentials.api.functionnals;

@FunctionalInterface
public interface StringConsumer<T> {

	String accept(T t);
	
}
