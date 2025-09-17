using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Domain.Models
{
    public class Estate
    {
        public int EstateID { get; set; }
        public double Surface { get; set; }
        public int Price { get; set; }
        public int NumberOfRooms { get; set; }
        public string Status { get; set; }
        public string Description { get; set; }
        public string Username { get; set; }
        public int Sector { get; set; }
        public string Neighborhood { get; set; }
        public string Address { get; set; }
        public UserDetails User { get; set; }
        public List<EstateImage> Images { get; set; }
        public Location Location { get; set; }
    }
}
