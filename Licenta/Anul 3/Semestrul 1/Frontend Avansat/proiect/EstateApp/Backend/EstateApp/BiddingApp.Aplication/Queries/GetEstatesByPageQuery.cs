using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.Queries
{
    public class GetEstatesByPageQuery : IRequest<List<Estate>>
    {
        public int PageNumber { get; set; }
        public int PageSize { get; set; }
    }
}
