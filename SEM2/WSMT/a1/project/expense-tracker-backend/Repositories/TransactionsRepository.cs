using expense_tracker.Data;
using expense_tracker.Entities;
using Microsoft.EntityFrameworkCore;

namespace expense_tracker.Repositories
{
    public class TransactionsRepository(SqlServerDbContext dbContext) : ITransactionsRepository
    {
        public async Task AddAsync(Transaction transaction)
        {
            await dbContext.Transactions.AddAsync(transaction);
        }

        public Task DeleteAsync(Transaction transaction)
        {
            dbContext.Transactions.Remove(transaction);
            return Task.CompletedTask;
        }

        public async Task<List<Transaction>> GetAllAsync()
        {
            return await dbContext.Transactions
                .AsNoTracking()
                .ToListAsync();
        }

        public async Task<Transaction?> GetByIdAsync(Guid id)
        {
            return await dbContext.Transactions
                .FirstOrDefaultAsync(transaction => transaction.Id == id);
        }

        public async Task<List<Transaction>> GetByTypeAsync(TransactionType type)
        {
            return await dbContext.Transactions
                .Where(transaction => transaction.Type == type)
                .AsNoTracking()
                .ToListAsync();
        }

        public async Task<double> GetSumByTypeAsync(TransactionType type)
        {
            return await dbContext.Transactions
                .Where(transaction => transaction.Type == type)
                .SumAsync(transaction => transaction.Amount);
        }

        public async Task<Dictionary<string, List<Transaction>>> GetTransactionsGroupedByCategoryAsync()
        {
            return await dbContext.Transactions
                .AsNoTracking()
                .GroupBy(transaction => transaction.Category)
                .ToDictionaryAsync(group => group.Key, group => group.ToList());
        }

        public async Task<bool> SaveChangesAsync()
        {
            return await dbContext.SaveChangesAsync() > 0;
        }

        public Task UpdateAsync(Transaction transaction)
        {
            dbContext.Transactions.Update(transaction);
            return Task.CompletedTask;
        }
    }
}
