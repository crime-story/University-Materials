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
    public class GetEstatesImagesQueryHandler : IRequestHandler<GetEstatesImagesQuery, List<EstateImage>>
    {
        private readonly IUnitOfWork _unitOfWork;
        public GetEstatesImagesQueryHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<List<EstateImage>> Handle(GetEstatesImagesQuery request, CancellationToken cancellationToken)
        {
            return await _unitOfWork.EstateImageRepository.GetAll();
        }
    }
}
