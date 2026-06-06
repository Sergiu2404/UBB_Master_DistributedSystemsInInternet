using expense_tracker.DTOs;
using expense_tracker.Entities;
using expense_tracker.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace expense_tracker.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TransactionsController(ITransactionsService transactionsService) : ControllerBase
    {
        [HttpGet]
        public async Task<ActionResult<List<Transaction>>> GetAll()
        {
            var transactions = await transactionsService.GetAllTransactionsAsync();
            return Ok(transactions);
        }

        [HttpGet("{id:guid}")]
        public async Task<ActionResult<Transaction>> GetTransactionById(Guid id)
        {
            var transaction = await transactionsService.GetTransactionByIdAsync(id);
            if (transaction == null)
            {
                return NotFound();
            }
            return Ok(transaction);
        }

        [HttpGet("balance")]
        public async Task<ActionResult<double>> GetBalance()
        {
            return Ok(await transactionsService.GetTotalBalanceAsync());
        }

        [HttpGet("type/{type}")]
        public async Task<ActionResult<List<Transaction>>> GetByType(TransactionType type)
        {
            return Ok(await transactionsService.GetTransactionByType(type));
        }

        [HttpGet("grouped")]
        public async Task<ActionResult<Dictionary<string, List<Transaction>>>> GetGroupedByCategory()
        {
            return Ok(await transactionsService.GetTransactionsGroupedByCategoryAsync());
        }

        [HttpPost]
        public async Task<ActionResult<TransactionResponseDto>> Create(TransactionRequestDto transactionDto)
        {
            if (transactionDto == null)
            {
                return BadRequest("some data is required");
            }

            var transaction = new Transaction
            {
                Id = Guid.NewGuid(),
                Name = transactionDto.Name,
                Description = transactionDto.Description,
                Amount = transactionDto.Amount,
                Type = transactionDto.Type,
                Category = transactionDto.Category,
                Date = transactionDto.Date,
                PaymentMethod = transactionDto.PaymentMethod
            };

            var success = await transactionsService.CreateTransactionAsync(transaction);
            if (!success)
            {
                return BadRequest("Transaction couldnt be created");
            }

            var response = new TransactionResponseDto(
                transaction.Id,
                transaction.Name,
                transaction.Description,
                transaction.Amount,
                transaction.Type,
                transaction.Category,
                transaction.Date,
                transaction.PaymentMethod
            );

            return CreatedAtAction(nameof(GetTransactionById), new { id = response.Id }, response);
        }

        [HttpPut("{id:guid}")]
        public async Task<ActionResult> Update(Guid id, TransactionRequestDto transactionDto)
        {
            if (transactionDto == null)
            {
                return BadRequest("some data is required");
            }

            var transaction = new Transaction
            {
                Id = id,
                Name = transactionDto.Name,
                Description = transactionDto.Description,
                Amount = transactionDto.Amount,
                Type = transactionDto.Type,
                Category = transactionDto.Category,
                Date = transactionDto.Date,
            };

            var success = await transactionsService.UpdateTransactionAsync(transaction);
            if (!success)
            {
                return NotFound();
            }
            return NoContent();
        }

        [HttpDelete("{id:guid}")]
        public async Task<ActionResult> Delete(Guid id)
        {
            var success = await transactionsService.DeleteTransactionAsync(id);
            if (!success)
            {
                return NotFound();
            }

            return NoContent();
        }
    }
}
