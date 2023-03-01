using EstateApp.Aplication.Abstract;
using EstateApp.Domain.Models;
using EstateApp.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Infrastructure.Repositories
{
    public class UserDetailsRepository : GenericRepository<UserDetails>, IUserDetailsRepository
    {
        public UserDetailsRepository(EstateAppContext _context) : base(_context) { }

        public async Task<UserDetails> GetUserDetailsByUsername(string username)
        {
            var toReturn = await _context.UserDetails.Where(x => x.Username == username).FirstOrDefaultAsync();
            return toReturn;
        }
    }
}
