using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.Commands
{
    public class DeleteEstateCommand : IRequest<Estate>
    {
        public int EstateID { get; set; }
    }
}
