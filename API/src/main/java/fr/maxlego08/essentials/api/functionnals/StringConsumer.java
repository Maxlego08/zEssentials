package fr.maxlego08.essentials.api.functionnals;

@FunctionalInterface
public interface StringConsumer<T> {

    /**
     * Accepts a value of type T and returns a string.
     * The contents of the returned string depend on the implementation.
     *
     * @param t the value to be accepted
     * @return the string resulting from accepting the given value
     */
    String accept(T t);

}
