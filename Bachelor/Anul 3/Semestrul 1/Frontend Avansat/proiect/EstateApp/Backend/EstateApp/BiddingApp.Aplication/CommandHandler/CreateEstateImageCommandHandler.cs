using EstateApp.Aplication.Abstract;
using EstateApp.Aplication.Commands;
using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Aplication.CommandHandler
{
    class CreateEstateImageCommandHandler : IRequestHandler<CreateEstateImageCommand, EstateImage>
    {
        private readonly IUnitOfWork _unitOfWork;
        public CreateEstateImageCommandHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<EstateImage> Handle(CreateEstateImageCommand request, CancellationToken cancellationToken)
        {
            var estate = await _unitOfWork.EstateRepository.GetByIdAsync(request.EstateID);
            if(estate == null)
            {
                return null;
            }
            var image = new EstateImage();
            image.EstateID = request.EstateID;
            image.URL = request.URL;
            await _unitOfWork.EstateImageRepository.Create(image);
            await _unitOfWork.Save();
            return image;
        }
    }
}
