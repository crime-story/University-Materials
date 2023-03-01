using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Domain.Models
{
    public class UserDetails
    {
        public string FullName { get; set; }
        [Key]
        public string Username { get; set; }
        public int Age { get; set; }
        public string Residence { get; set; }
        public string Description { get; set; }
        public string ProfileURL { get; set; }
        public List<Estate> Estates { get; set; }
    }
}
