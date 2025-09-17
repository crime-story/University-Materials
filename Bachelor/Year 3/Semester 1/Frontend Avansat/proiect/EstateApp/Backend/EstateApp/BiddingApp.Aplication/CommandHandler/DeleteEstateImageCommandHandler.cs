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
    public class DeleteEstateImageCommandHandler : IRequestHandler<DeleteEstateImageCommand, EstateImage>
    {
        private readonly IUnitOfWork _unitOfWork;
        public DeleteEstateImageCommandHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<EstateImage> Handle(DeleteEstateImageCommand request, CancellationToken cancellationToken)
        {
            var estateImage = await _unitOfWork.EstateImageRepository.GetByIdAsync(request.EstateImageID);
            if(estateImage == null)
            {
                return null;
            }
            _unitOfWork.EstateImageRepository.Delete(estateImage);
            await _unitOfWork.Save();
            return estateImage;
        }
    }
}
