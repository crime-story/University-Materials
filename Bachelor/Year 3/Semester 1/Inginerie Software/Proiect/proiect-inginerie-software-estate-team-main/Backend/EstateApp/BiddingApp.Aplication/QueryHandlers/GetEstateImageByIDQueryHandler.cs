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
    public class GetEstateImageByIDQueryHandler : IRequestHandler<GetEstateImageByIDQuery, EstateImage>
    {
        private readonly IUnitOfWork _unitOfWork;
        public GetEstateImageByIDQueryHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<EstateImage> Handle(GetEstateImageByIDQuery request, CancellationToken cancellationToken)
        {
            return await _unitOfWork.EstateImageRepository.GetByIdAsync(request.EstateImageID);
        }
    }
}
