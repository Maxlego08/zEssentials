package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.modules.Loadable;

import java.math.BigDecimal;

public record NumberMultiplicationFormat(String format, BigDecimal multiplication) implements Loadable {

}