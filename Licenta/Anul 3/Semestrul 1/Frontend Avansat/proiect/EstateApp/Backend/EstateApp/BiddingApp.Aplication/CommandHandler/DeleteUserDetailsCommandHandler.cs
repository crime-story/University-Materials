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
    public class DeleteUserDetailsCommandHandler : IRequestHandler<DeleteUserDetailsCommand, UserDetails>
    {
        private readonly IUnitOfWork _unitOfWork;
        public DeleteUserDetailsCommandHandler(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<UserDetails> Handle(DeleteUserDetailsCommand request, CancellationToken cancellationToken)
        {
            var user = await _unitOfWork.UserDetailsRepository.GetUserDetailsByUsername(request.Username);
            if(user == null)
            {
                return null;
            }
            _unitOfWork.UserDetailsRepository.Delete(user);
            await _unitOfWork.Save();
            return user;
        }
    }
}