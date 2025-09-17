using EstateApp.Aplication.Abstract;
using EstateApp.Aplication.Commands;
using EstateApp.Domain.Models;
using MediatR;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace EstateApp.Aplication.CommandHandler
{
    public class UpdateUserDetailsCommandHandler : IRequestHandler<UpdateUserDetailsCommand, UserDetails>
    {
        private readonly IUnitOfWork _unitOfWork;

        public UpdateUserDetailsCommandHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<UserDetails> Handle(UpdateUserDetailsCommand request, CancellationToken cancellationToken)
        {
            var user = await _unitOfWork.UserDetailsRepository.GetUserDetailsByUsername(request.Username);
            if(user == null)
            {
                return null;
            }
            user.FullName = request.FullName;
            user.Residence = request.Residence;
            user.Age = request.Age;
            user.Description = request.Description;
            user.ProfileURL = request.ProfileURL;
            await _unitOfWork.UserDetailsRepository.Update(user);
            await _unitOfWork.Save();
            return user;
        }
    }
}