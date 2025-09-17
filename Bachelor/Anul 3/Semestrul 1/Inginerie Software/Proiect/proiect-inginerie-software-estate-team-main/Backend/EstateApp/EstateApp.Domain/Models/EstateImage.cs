using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Domain.Models
{
    public class EstateImage
    {
        public int EstateImageID { get; set; }
        public string URL { get; set; }
        public int EstateID { get; set; }
        public Estate Estate { get; set; }
    }
}
