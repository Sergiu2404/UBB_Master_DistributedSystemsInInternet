using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace auth_example.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Authorize]
    public class AdminController : ControllerBase
    {
        [HttpGet("dashboard")]
        [Authorize(Roles = "Admin")]
        public IActionResult Dashboard()
        {
            return Ok($"admin dashboard: you have admin privileges, user {User.Identity?.Name}");
        }

        [HttpGet("me")]
        public IActionResult Me()
        {
            var username = User.Identity?.Name;
            var role = User.IsInRole("Admin") ? "Admin" : "Basic";
            return Ok(new { username, role });
        }
    }
}
