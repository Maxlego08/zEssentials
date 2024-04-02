package fr.maxlego08.essentials.api.functionnals;
@FunctionalInterface
public interface ReturnConsumer<T, G> {

	G accept(T t);
	
}