public enum Currency {
    RUB("RUB", "₽", 100),
    USD("USD", "$", 9600),
    EUR("EUR", "€", 10300),
    CNY("CNY", "¥", 1320);

    private final String code;
    private final String symbol;
    private final long price_in_rub;

    Currency(String code, String symbol, long price_in_rub) {
        this.code = code;
        this.symbol = symbol;
        this.price_in_rub = price_in_rub;
    }

    public String get_code() {
        return code;
    }

    public String get_symbol() {
        return symbol;
    }

    public long get_price() {
        return price_in_rub;
    }
}
