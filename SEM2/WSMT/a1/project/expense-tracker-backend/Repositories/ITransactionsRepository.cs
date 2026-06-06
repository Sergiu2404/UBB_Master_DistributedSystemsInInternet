using expense_tracker.Entities;

namespace expense_tracker.Repositories
{
    public interface ITransactionsRepository
    {
        Task<List<Transaction>> GetAllAsync();
        Task<Transaction?> GetByIdAsync(Guid id);
        Task<List<Transaction>> GetByTypeAsync(TransactionType type);
        Task AddAsync(Transaction transaction);
        Task DeleteAsync(Transaction transaction);
        Task UpdateAsync(Transaction transaction);
        Task<bool> SaveChangesAsync();
        Task< Dictionary< string, List<Transaction> > > GetTransactionsGroupedByCategoryAsync();
        Task<double> GetSumByTypeAsync(TransactionType type);
    }
}
