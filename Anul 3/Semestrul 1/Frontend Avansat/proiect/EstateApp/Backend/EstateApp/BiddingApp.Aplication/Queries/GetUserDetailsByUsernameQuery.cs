using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace EstateApp.Aplication.Queries
{
    public class GetUserDetailsByUsernameQuery : IRequest<UserDetails>
    {
        public string Username { get; set; }
    }
}