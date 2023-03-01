using EstateApp.Domain.Dtos;
using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.Commands
{
    public class CreateUserDetailsCommand : IRequest<UserDetails>
    {
        public CreateUserDetailsCommand(CreateUserDetailsDto dto)
        {
            FullName = dto.FullName;
            Username = dto.Username;
            Age = dto.Age;
            Residence = dto.Residence;
            ProfileURL = dto.ProfileURL;
            Description = dto.Description;
        }
        public string FullName { get; set; }
        public string Username { get; set; }
        public int Age { get; set; }
        public string Residence { get; set; }
        public string ProfileURL { get; set; }
        public string Description { get; set; }
    }
}
