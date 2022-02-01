using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Entities;

namespace proiectASP.Managers
{
    public interface ITokenManager
    {
        Task<string> GenerateToken(User user);
    }
}
