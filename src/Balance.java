import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Balance {
    private final long money;
    private final Currency currency;

    public Balance(long money, Currency currency) {
        if (money < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Валюта не указана");
        }
        this.money = money;
        this.currency = currency;
    }

    public long get_money() {
        return money;
    }

    public Currency get_currency() {
        return currency;
    }

    public Balance add(long sum) {
        if (sum <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }
        return new Balance(Math.addExact(money, sum), currency);
    }

    public Balance subtract(long sum) {
        if (sum <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }
        if (sum > money) {
            throw new IllegalArgumentException("Недостаточно денег");
        }
        return new Balance(money - sum, currency);
    }

    public Balance convertTo(Currency new_currency) {
        if (new_currency == null) {
            throw new IllegalArgumentException("Валюта не указана");
        }
        if (currency == new_currency) {
            return new Balance(money, currency);
        }

        BigDecimal sum = BigDecimal.valueOf(money);
        BigDecimal old_price = BigDecimal.valueOf(currency.get_price());
        BigDecimal new_price = BigDecimal.valueOf(new_currency.get_price());
        long result = sum.multiply(old_price).divide(new_price, 0, RoundingMode.HALF_UP).longValueExact();
        return new Balance(result, new_currency);
    }

    @Override
    public String toString() {
        return (money / 100) + "." + String.format("%02d", money % 100) + " " + currency.get_symbol();
    }
}
