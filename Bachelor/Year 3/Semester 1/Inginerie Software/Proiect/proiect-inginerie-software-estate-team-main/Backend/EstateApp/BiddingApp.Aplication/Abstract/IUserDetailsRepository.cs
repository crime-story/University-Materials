using EstateApp.Domain.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.Abstract
{
    public interface IUserDetailsRepository : IGenericRepository<UserDetails>
    {
        Task<UserDetails> GetUserDetailsByUsername(string username);
    }
}
