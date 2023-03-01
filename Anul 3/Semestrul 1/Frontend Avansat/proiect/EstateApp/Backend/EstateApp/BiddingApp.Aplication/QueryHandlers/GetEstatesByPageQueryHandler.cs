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
    public class GetEstatesByPageQueryHandler : IRequestHandler<GetEstatesByPageQuery, List<Estate>>
    {
        private readonly IUnitOfWork _unitOfWork;
        public GetEstatesByPageQueryHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }
        public async Task<List<Estate>> Handle(GetEstatesByPageQuery request, CancellationToken cancellationToken)
        {
            return await _unitOfWork.EstateRepository.GetEstatesByPageNumber(request.PageNumber, request.PageSize);
        }
    }
}
