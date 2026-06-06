using System.ComponentModel.DataAnnotations;

namespace expense_tracker.Entities
{
    public enum TransactionType
    {
        EXPENSE,
        INCOME
    }

    public class Transaction
    {
        public Guid Id { get; set; }

        [Required]
        [MaxLength(100)]
        public string Name { get; set; } = string.Empty;
        
        public string? Description { get; set; }
        
        [Required]
        [Range(0.01, double.MaxValue)]
        public double Amount { get; set; }

        [Required]
        public TransactionType Type { get; set; }

        [Required]
        public DateTime Date { get; set; } = DateTime.UtcNow;

        [Required]
        public string Category { get; set; } = "General";

        public string? PaymentMethod { get; set; }
    }
}
