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
    public class CreateUserDetailsCommandHandler : IRequestHandler<CreateUserDetailsCommand, UserDetails>
    {
        private readonly IUnitOfWork _unitOfWork;

        public CreateUserDetailsCommandHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<UserDetails> Handle(CreateUserDetailsCommand request, CancellationToken cancellationToken)
        {
            var client = await _unitOfWork.UserDetailsRepository.GetUserDetailsByUsername(request.Username);
            if(client == null)
            {
                var user = new UserDetails();
                user.FullName = request.FullName;
                user.Username = request.Username;
                user.Age = request.Age;
                user.Residence = request.Residence;
                user.Description = request.Description;
                user.ProfileURL = request.ProfileURL;
                await _unitOfWork.UserDetailsRepository.Create(user);
                await _unitOfWork.Save();

                return user;
            }
            return null;
        }
    }
}
