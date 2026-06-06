using expense_tracker.Entities;

namespace expense_tracker.Services
{
    public interface ITransactionsService
    {
        Task<double> GetTotalBalanceAsync();
        Task<bool> CreateTransactionAsync(Transaction transaction);
        Task<bool> DeleteTransactionAsync(Guid id);
        Task<bool> UpdateTransactionAsync(Transaction transaction);
        Task<Transaction?> GetTransactionByIdAsync(Guid id);
        Task<List<Transaction>> GetAllTransactionsAsync();
        Task<List<Transaction>> GetTransactionByType(TransactionType type);
        Task<Dictionary<string, List<Transaction>>> GetTransactionsGroupedByCategoryAsync();
    }
}
