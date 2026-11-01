private static int passed = 0;
private static int failed = 0;

void main() {
    Balance balance = new Balance(10000, Currency.RUB);

    test("Сложение", "10000 + 5000", 15000, balance.add(5000).get_money());
    test("Вычитание", "10000 - 3000", 7000, balance.subtract(3000).get_money());
    test("Старый баланс не изменился", "После добавленяи и вычитания", 10000, balance.get_money());
    test("Рубли в доллары", "9600 копеек RUB в USD", 100,
            new Balance(9600, Currency.RUB).convertTo(Currency.USD).get_money());
    test("Доллары в рубли", "100 центов USD в RUB", 9600,
            new Balance(100, Currency.USD).convertTo(Currency.RUB).get_money());
    test("Округление при обмене", "48 копеек RUB в USD", 1,
            new Balance(48, Currency.RUB).convertTo(Currency.USD).get_money());

    String error = "нет ошибки";
    try {
        new Balance(-100, Currency.RUB);
    } catch (IllegalArgumentException e) {
        error = e.getMessage();
    }
    test("Отрицательный баланс", "money=-100", "Сумма не может быть отрицательной", error);

    error = "нет ошибки";
    try {
        balance.subtract(20000);
    } catch (IllegalArgumentException e) {
        error = e.getMessage();
    }
    test("Недостаточно денег", "10000 - 20000", "Недостаточно денег", error);


    BankAccount anna = new BankAccount("Анна", new Balance(20000, Currency.RUB), 15000);
    BankAccount ivan = new BankAccount("Иван", new Balance(5000, Currency.RUB), 10000);

    anna.deposit(5000);
    test("Пополнение", "20000 + 5000", 25000, anna.get_balance().get_money());
    anna.withdraw(3000);
    test("Снятие", "25000 - 3000", 22000, anna.get_balance().get_money());
    anna.transfer_to(ivan, 10000);
    test("Отправка перевода", "Анна: 22000 - 10000", 12000, anna.get_balance().get_money());
    test("Получение перевода", "Иван: 5000 + 10000", 15000, ivan.get_balance().get_money());

    error = "нет ошибки";
    try {
        anna.transfer_to(ivan, 16000);
    } catch (IllegalArgumentException e) {
        error = e.getMessage();
    }
    test("Превышение лимита", "limit=15000, перевод=16000", "Превышен лимит", error);
    test("Баланс после ошибки", "Анна после неудачного перевода", 12000, anna.get_balance().get_money());

    anna.block();
    test("Блокировка", "anna.block()", 1, anna.is_blocked() ? 1 : 0);
    error = "нет ошибки";
    try {
        anna.withdraw(100);
    } catch (IllegalStateException e) {
        error = e.getMessage();
    }
    test("Снятие с заблокированного счёта", "withdraw(100)", "Счёт заблокирован", error);
    anna.unblock();
    test("Разблокировка", "anna.unblock()", 0, anna.is_blocked() ? 1 : 0);


    test("История Анны", "Пополнение, снятие, перевод, блокировка, разблокировка",
            5, anna.get_history().size());
    test("История Ивана", "Один входящий перевод", 1, ivan.get_history().size());
    anna.get_history().clear();
    test("История не удаляется извне", "Очищаем полученную копию", 5, anna.get_history().size());


    BankAccount rub = new BankAccount("Рубли", new Balance(9600, Currency.RUB), 10000);
    BankAccount usd = new BankAccount("Доллары", new Balance(200, Currency.USD), 10000);
    rub.transfer_to(usd, 9600);
    test("Рубли после перевода", "9600 - 9600 RUB копеек", 0, rub.get_balance().get_money());
    test("Доллары после перевода", "200 + 100 USD центов", 300, usd.get_balance().get_money());


    IO.println("Всего тестов: " + (passed + failed));
    IO.println("Успешно: " + passed);
    IO.println("Ошибок: " + failed);
    if (failed > 0) {
        throw new AssertionError("Некоторые тесты не прошли ");
    }
}

private static void test(String name, String data, long expected, long actual) {
    IO.println("\n" + name);
    IO.println("Дано: " + data);
    IO.println("Ожидаем: " + expected);
    IO.println("Получили: " + actual);
    if (expected == actual) {
        IO.println("ПАСС");
        passed++;
    } else {
        IO.println("ФЕЙЛ");
        failed++;
    }
}

private static void test(String name, String data, String expected, String actual) {
    IO.println("\n" + name);
    IO.println("Дано: " + data);
    IO.println("Ожидаем: " + expected);
    IO.println("Получили: " + actual);
    if (expected.equals(actual)) {
        IO.println("ПАСС");
        passed++;
    } else {
        IO.println("ФЕЙЛ");
        failed++;
    }
}
