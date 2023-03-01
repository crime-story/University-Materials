using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.Commands
{
    public class CreateEstateImageCommand : IRequest<EstateImage>
    {
        public string URL { get; set; }
        public int EstateID { get; set; }
    }
}
