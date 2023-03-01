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
    public class UpdateEstateCommandHandler : IRequestHandler<UpdateEstateCommand, Estate>
    {
        private readonly IUnitOfWork _unitOfWork;
        public UpdateEstateCommandHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<Estate> Handle(UpdateEstateCommand request, CancellationToken cancellationToken)
        {
            var user = await _unitOfWork.UserDetailsRepository.GetUserDetailsByUsername(request.Username);
            if(user == null)
            {
                return null;
            }
            var estate = await _unitOfWork.EstateRepository.GetByIdAsync(request.EstateID);
            if(estate == null)
            {
                return null;
            }
            estate.Surface = request.Surface;
            estate.Status = request.Status;
            estate.Description = request.Description;
            estate.Price = request.Price;
            estate.NumberOfRooms = request.NumberOfRooms;
            estate.Sector = request.Sector;
            estate.Address = request.Address;
            estate.Neighborhood = request.Neighborhood;
            await _unitOfWork.EstateRepository.Update(estate);
            await _unitOfWork.Save();
            return estate;
        }
    }
}
