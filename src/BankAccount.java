import java.util.ArrayList;

public class BankAccount {
    private final String name;
    private final long limit;
    private Balance balance;
    private boolean blocked;
    private final ArrayList<String> history = new ArrayList<>();

    public BankAccount(String name, Balance balance, long limit) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не указано");
        }
        if (balance == null) {
            throw new IllegalArgumentException("Баланс не указан");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Неверный лимит");
        }
        this.name = name;
        this.balance = balance;
        this.limit = limit;
    }

    public String get_name() {
        return name;
    }

    public Balance get_balance() {
        return balance;
    }

    public long get_limit() {
        return limit;
    }

    public boolean is_blocked() {
        return blocked;
    }

    public ArrayList<String> get_history() {
        return new ArrayList<>(history);
    }

    public void show_history() {
        System.out.println("\nИстория операций: " + name);
        if (history.isEmpty()) {
            System.out.println("Операций пока нет");
        } else {
            for (String operation : history) {
                System.out.println(operation);
            }
        }
    }

    public void block() {
        blocked = true;
        history.add("Счёт заблокирован");
    }

    public void unblock() {
        blocked = false;
        history.add("Счёт разблокирован");
    }

    public void deposit(long sum) {
        if (blocked) {
            throw new IllegalStateException("Счёт заблокирован");
        }
        balance = balance.add(sum);
        history.add("Пополнение: +" + new Balance(sum, balance.get_currency()));
    }

    public void withdraw(long sum) {
        if (blocked) {
            throw new IllegalStateException("Счёт заблокирован");
        }
        if (sum > limit) {
            throw new IllegalArgumentException("Превышен лимит");
        }
        balance = balance.subtract(sum);
        history.add("Снятие: -" + new Balance(sum, balance.get_currency()));
    }

    public void transfer_to(BankAccount account, long sum) {
        if (account == null) {
            throw new IllegalArgumentException("Счёт не указан");
        }
        if (account == this) {
            throw new IllegalArgumentException("Нельзя переводить самому себе");
        }
        if (blocked || account.blocked) {
            throw new IllegalStateException("Один из счетов заблокирован");
        }
        if (sum <= 0) {
            throw new IllegalArgumentException("Неверная сумма");
        }
        if (sum > limit) {
            throw new IllegalArgumentException("Превышен лимит");
        }

        Balance new_balance = balance.subtract(sum);
        Balance transfer = new Balance(sum, balance.get_currency());
        transfer = transfer.convertTo(account.balance.get_currency());

        if (transfer.get_money() == 0) {
            throw new IllegalArgumentException("Слишком маленькая сумма");
        }

        Balance other_balance = account.balance.add(transfer.get_money());
        balance = new_balance;
        account.balance = other_balance;

        history.add("Перевод для " + account.name + ": -" + new Balance(sum, balance.get_currency()));
        account.history.add("Перевод от " + name + ": +" + transfer);
    }

    @Override
    public String toString() {
        return name + ": " + balance + ", лимит: " + new Balance(limit, balance.get_currency()) + ", заблокирован: " + blocked;
    }
}
