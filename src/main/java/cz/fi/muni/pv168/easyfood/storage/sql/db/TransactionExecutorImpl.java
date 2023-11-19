package cz.fi.muni.pv168.easyfood.storage.sql.db;

import java.util.Objects;
import java.util.function.Supplier;

public class TransactionExecutorImpl implements TransactionExecutor {

    private final Supplier<Transaction> transactions;

    public TransactionExecutorImpl(Supplier<Transaction> transactions) {
        this.transactions = Objects.requireNonNull(transactions);
    }

    @Override
    public void executeInTransaction(Runnable operation) {
        try (var transaction = transactions.get()) {
            operation.run();
            transaction.commit();
        }
    }
}
