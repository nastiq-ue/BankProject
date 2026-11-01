void main() {
    BankAccount anna = new BankAccount("Анна", new Balance(50000, Currency.RUB), 30000);
    BankAccount ivan = new BankAccount("Иван", new Balance(10000, Currency.RUB), 20000);
    BankAccount oleg = new BankAccount("Олег", new Balance(2000, Currency.USD), 10000);

    IO.println("Начальные балансы:");
    IO.println(anna);
    IO.println(ivan);
    IO.println(oleg);

    anna.deposit(5000);
    anna.withdraw(2000);
    anna.transfer_to(ivan, 10000);
    anna.transfer_to(oleg, 9600);

    IO.println("\nПосле операций:");
    IO.println(anna);
    IO.println(ivan);
    IO.println(oleg);

    Balance balance = new Balance(10000, Currency.RUB);
    Balance new_balance = balance.add(5000);
    IO.println("\nПроверка неизменяемости Balance:");
    IO.println("Старый: " + balance);
    IO.println("Новый: " + new_balance);

    IO.println("\nПроверка ошибки:");
    try {
        anna.withdraw(40000);
    } catch (IllegalArgumentException e) {
        IO.println(e.getMessage());
    }

    ivan.block();
    try {
        ivan.withdraw(100);
    } catch (IllegalStateException e) {
        IO.println(e.getMessage());
    }
    ivan.unblock();

    anna.show_history();
    ivan.show_history();
    oleg.show_history();
}
