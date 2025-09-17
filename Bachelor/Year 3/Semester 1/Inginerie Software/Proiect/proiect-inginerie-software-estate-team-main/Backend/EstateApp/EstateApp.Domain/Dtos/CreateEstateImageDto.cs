using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Domain.Dtos
{
    public class CreateEstateImageDto
    {
        public string URL { get; set; }
        public int EstateID { get; set; }
    }
}
