using EstateApp.Aplication.Abstract;
using EstateApp.Aplication.Queries;
using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.QueryHandlers
{
    class GetEstatesQueryHandler : IRequestHandler<GetEstatesQuery, List<Estate>>
    {
        private readonly IUnitOfWork _unitOfWork;
        public GetEstatesQueryHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }
        public async Task<List<Estate>> Handle(GetEstatesQuery request, CancellationToken cancellationToken)
        {
            return await _unitOfWork.EstateRepository.GetAll();
        }
    }
}
