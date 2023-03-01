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
    class GetEstateByIDQueryHandler : IRequestHandler<GetEstateByIDQuery, Estate>
    {
        private readonly IUnitOfWork _unitOfWork;
        public GetEstateByIDQueryHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<Estate> Handle(GetEstateByIDQuery request, CancellationToken cancellationToken)
        {
            return await _unitOfWork.EstateRepository.GetEstateWithImagesByID(request.EstateID);
        }
    }
}
