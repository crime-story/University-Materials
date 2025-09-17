using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Domain.Models
{
    public class Location
    {
        public int LocationID { get; set; }
        public int Sector { get; set; }
        public string Neighborhood { get; set; }
        public string Address { get; set; }
        public int EstateID { get; set; }
        public Estate Estate { get; set; }
    }
}
