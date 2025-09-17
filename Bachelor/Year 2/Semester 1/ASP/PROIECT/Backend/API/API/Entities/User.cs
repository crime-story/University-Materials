using Microsoft.AspNetCore.Identity;

namespace API.Entities
{
    public class User : IdentityUser
    {
        public ICollection<UserRole> UserRoles { get; set; }
    }
}
