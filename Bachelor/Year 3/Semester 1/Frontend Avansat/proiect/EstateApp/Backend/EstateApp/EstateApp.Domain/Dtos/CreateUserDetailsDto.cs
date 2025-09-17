using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Domain.Dtos
{
    public class CreateUserDetailsDto
    {
        public string FullName { get; set; }
        public string Username { get; set; }
        public int Age { get; set; }
        public string Residence { get; set; }
        public string ProfileURL { get; set; }
        public string Description { get; set; }
    }
}
