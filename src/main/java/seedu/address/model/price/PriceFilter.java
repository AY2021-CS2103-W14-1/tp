package seedu.address.model.price;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Predicate that checks if a price matches the specified filter.
 */
public class PriceFilter implements Predicate<Price> {

    /**
     * Enumeration of the different comparisons.
     */
    enum CompareOp {
        LESS_THAN ((toTest, target) -> toTest.compareTo(target) < 0),
        LESS_THAN_OR_EQUALS ((toTest, target) -> toTest.compareTo(target) <= 0),
        EQUALS ((toTest, target) -> toTest.compareTo(target) == 0),
        GREATER_THAN ((toTest, target) -> toTest.compareTo(target) > 0),
        GREATER_THAN_OR_EQUALS ((toTest, target) -> toTest.compareTo(target) >= 0)
        ;

        private final BiFunction<Price, Price, Boolean> function;

        CompareOp(BiFunction<Price, Price, Boolean> function) {
            this.function = function;
        }

        private BiFunction<Price, Price, Boolean> getBiFunction() {
            return function;
        }
    }

    private final CompareOp compareOp;
    private final Price target;
    private static Map<String, CompareOp> map = Map.of(
            "<", CompareOp.LESS_THAN,
            "<=", CompareOp.LESS_THAN_OR_EQUALS,
            "==", CompareOp.EQUALS,
            ">", CompareOp.GREATER_THAN,
            ">=", CompareOp.GREATER_THAN
    );
    public static final String MESSAGE_CONSTRAINTS = "< / <= / == / > / >= PRICE eg [<= 500]";

    /**
     * Constructs a {@code PriceFilter} object from a String.
     *
     * @param priceFilter The String specified.
     */
    public PriceFilter(String priceFilter) {
        requireNonNull(priceFilter);
        checkArgument(isValidPriceFilter(priceFilter), MESSAGE_CONSTRAINTS);
        if (map.containsKey(priceFilter.substring(0, 2))) {
            this.compareOp = map.get(priceFilter.substring(0, 2));
            this.target = new Price(Double.parseDouble(priceFilter.substring(2)));
        } else if (map.containsKey(priceFilter.substring(0, 1))) {
            this.compareOp = map.get(priceFilter.substring(0, 1));
            this.target = new Price(Double.parseDouble(priceFilter.substring(1)));
        } else {
            assert false: "Invalid price filter scenario already handled";
            this.compareOp = null;
            this.target = null;
        }
    }

    public static boolean isValidPriceFilter(String test) {
        if (test == null || test.equals("")) {
            return false;
        }
        try {
            if (map.containsKey(test.substring(0, 2))) {
                Double.parseDouble(test.substring(2));
                return true;
            } else if (map.containsKey(test.substring(0, 1))) {
                Double.parseDouble(test.substring(1));
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean test(Price price) {
        return compareOp.getBiFunction().apply(price, target);
    }
}