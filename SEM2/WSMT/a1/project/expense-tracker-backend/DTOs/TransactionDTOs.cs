using expense_tracker.Entities;
using System.ComponentModel.DataAnnotations;

namespace expense_tracker.DTOs
{
    public record TransactionRequestDto(
        [Required][StringLength(100)] string Name,
        string? Description,
        [Range(0.01, double.MaxValue)] double Amount,
        TransactionType Type,
        [Required] string Category,
        DateTime Date,
        [Required] string PaymentMethod
    );

    public record TransactionResponseDto(
        Guid Id,
        string Name,
        string? Description,
        double Amount,
        TransactionType Type,
        string Category,
        DateTime Date,
        string PaymentMethod
    );
}
