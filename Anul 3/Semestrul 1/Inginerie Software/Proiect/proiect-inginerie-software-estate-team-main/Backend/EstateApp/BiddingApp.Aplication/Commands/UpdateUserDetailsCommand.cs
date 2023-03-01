using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace EstateApp.Aplication.Commands
{
    public class UpdateUserDetailsCommand : IRequest<UserDetails>
    {
        public string FullName { get; set; }
        public string Username { get; set; }
        public int Age { get; set; }
        public string Residence { get; set; }
        public string ProfileURL { get; set; }
        public string Description { get; set; }
    }
}