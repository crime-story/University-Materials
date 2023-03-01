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
    public class CreateEstateCommandHandler : IRequestHandler<CreateEstateCommand, Estate>
    {
        private readonly IUnitOfWork _unitOfWork;
        public CreateEstateCommandHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<Estate> Handle(CreateEstateCommand request, CancellationToken cancellationToken)
        {
            var user = await _unitOfWork.UserDetailsRepository.GetUserDetailsByUsername(request.Username);
            if(user == null)
            {
                return null;
            }
            var estate = new Estate();
            estate.Username = request.Username;
            estate.Price = request.Price;
            estate.NumberOfRooms = request.NumberOfRooms;
            estate.Description = request.Description;
            estate.Surface = request.Surface;
            estate.Status = request.Status;
            estate.Sector = request.Sector;
            estate.Address = request.Address;
            estate.Neighborhood = request.Neighborhood;
            await _unitOfWork.EstateRepository.Create(estate);
            await _unitOfWork.Save();
            return estate;
        }
    }
}
