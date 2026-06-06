using expense_tracker.Entities;
using Microsoft.EntityFrameworkCore;

namespace expense_tracker.Data
{
    public class SqlServerDbContext(DbContextOptions<SqlServerDbContext> options) : DbContext(options)
    {
        public DbSet<Transaction> Transactions { get; set; }
    }
}
