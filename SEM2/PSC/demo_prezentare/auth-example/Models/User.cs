using System.Data;

namespace auth_example.Models
{
    public enum Role
    {
        Basic,
        Admin
    }
    public class User
    {
        public int Id { get; set; }
        public string Username { get; set; } = string.Empty;
        public string PasswordHash { get; set; } = string.Empty;
        public Role Role { get; set; } = Role.Basic;
    }
}
