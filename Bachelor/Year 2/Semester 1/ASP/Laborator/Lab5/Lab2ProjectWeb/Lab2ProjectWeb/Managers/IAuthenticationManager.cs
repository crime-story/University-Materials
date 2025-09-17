using Lab2ProjectWeb.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2ProjectWeb.Managers
{
    public interface IAuthenticationManager
    {
        Task Signup(SignupUserModel signupUserModel);
        Task<TokenModel> Login(LoginUserModel loginUserModel);
    }
}
