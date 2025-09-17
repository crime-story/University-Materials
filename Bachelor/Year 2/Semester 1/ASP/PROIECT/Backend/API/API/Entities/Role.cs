using Microsoft.AspNetCore.Identity;

namespace API.Entities
{
    public class Role : IdentityRole
    {
        public ICollection<UserRole> UserRoles { get; set; }
    }
}
