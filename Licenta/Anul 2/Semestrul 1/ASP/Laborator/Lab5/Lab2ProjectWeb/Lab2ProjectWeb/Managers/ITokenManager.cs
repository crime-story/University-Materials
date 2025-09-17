using Lab2ProjectWeb.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Managers
{
    public interface ITokenManager
    {
        Task<string> CreateToken(User user);
    }
}
