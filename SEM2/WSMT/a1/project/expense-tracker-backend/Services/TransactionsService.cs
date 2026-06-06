using expense_tracker.Entities;
using expense_tracker.Repositories;

namespace expense_tracker.Services
{
    public class TransactionsService(ITransactionsRepository transactionsRepository) : ITransactionsService
    {
        public async Task<bool> CreateTransactionAsync(Transaction transaction)
        {
            await transactionsRepository.AddAsync(transaction);
            return await transactionsRepository.SaveChangesAsync();
        }

        public async Task<bool> DeleteTransactionAsync(Guid id)
        {
            var existing = await transactionsRepository.GetByIdAsync(id);
            if (existing == null)
            {
                return false;
            }

            await transactionsRepository.DeleteAsync(existing);
            return await transactionsRepository.SaveChangesAsync();
        }

        public async Task<List<Transaction>> GetAllTransactionsAsync()
        {
            return await transactionsRepository.GetAllAsync();
        }

        public async Task<double> GetTotalBalanceAsync()
        {
            var income = await transactionsRepository.GetSumByTypeAsync(TransactionType.INCOME);
            var expense = await transactionsRepository.GetSumByTypeAsync(TransactionType.EXPENSE);

            return income - expense;
        }

        public async Task<Transaction?> GetTransactionByIdAsync(Guid id)
        {
            return await transactionsRepository.GetByIdAsync(id);
        }

        public async Task<List<Transaction>> GetTransactionByType(TransactionType type)
        {
            return await transactionsRepository.GetByTypeAsync(type);
        }

        public async Task<Dictionary<string, List<Transaction>>> GetTransactionsGroupedByCategoryAsync()
        {
            return await transactionsRepository.GetTransactionsGroupedByCategoryAsync();
        }

        public async Task<bool> UpdateTransactionAsync(Transaction transaction)
        {
            var existing = await transactionsRepository.GetByIdAsync(transaction.Id);
            if (existing == null)
            {
                return false;
            }

            existing.Name = transaction.Name;
            existing.Description = transaction.Description;
            existing.Type = transaction.Type;
            existing.Amount = transaction.Amount;
            existing.Date = transaction.Date;
            existing.Category = transaction.Category;

            await transactionsRepository.UpdateAsync(existing);
            return await transactionsRepository.SaveChangesAsync();
        }
    }
}
