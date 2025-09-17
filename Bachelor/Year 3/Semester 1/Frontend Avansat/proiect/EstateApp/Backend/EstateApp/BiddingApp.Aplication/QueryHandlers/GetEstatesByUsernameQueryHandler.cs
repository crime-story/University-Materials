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
    public class GetEstatesByUsernameQueryHandler : IRequestHandler<GetEstatesByUsernameQuery, List<Estate>>
    {
        private readonly IUnitOfWork _unitOfWork;
        public GetEstatesByUsernameQueryHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<List<Estate>> Handle(GetEstatesByUsernameQuery request, CancellationToken cancellationToken)
        {
            return await _unitOfWork.EstateRepository.GetEstatesByUsername(request.Username);
        }
    }
}
