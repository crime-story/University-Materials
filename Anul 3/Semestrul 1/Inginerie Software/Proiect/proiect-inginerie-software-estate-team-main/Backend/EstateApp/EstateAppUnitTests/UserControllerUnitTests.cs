using AutoMapper.Internal;
using EstateApp.API.Controllers;
using EstateApp.Domain.Dtos;
using EstateApp.Domain.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Moq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateAppUnitTests
{
    public class UserControllerUnitTests
    {
        private readonly UserController _controller;
        private readonly LoginUserDto _loginUserDto;
        private readonly User _user;
        private readonly Mock<UserManager<User>> _userManagerMock;
        private readonly Mock<RoleManager<IdentityRole>> _roleManagerMock;

        public UserControllerUnitTests()
        {
            _userManagerMock = new Mock<UserManager<User>>(
                new Mock<IUserStore<User>>().Object,
                new Mock<IOptions<IdentityOptions>>().Object,
                new Mock<IPasswordHasher<User>>().Object,
                new IUserValidator<User>[0],
                new IPasswordValidator<User>[0],
                new Mock<ILookupNormalizer>().Object,
                new Mock<IdentityErrorDescriber>().Object,
                new Mock<IServiceProvider>().Object,
                new Mock<ILogger<UserManager<User>>>().Object
            );
            _roleManagerMock = new Mock<RoleManager<IdentityRole>>(
                new Mock<IRoleStore<IdentityRole>>().Object,
                new IRoleValidator<IdentityRole>[0],
                new Mock<ILookupNormalizer>().Object,
                new Mock<IdentityErrorDescriber>().Object,
                new Mock<ILogger<RoleManager<IdentityRole>>>().Object
            );
            _controller = new UserController(_userManagerMock.Object, _roleManagerMock.Object);
            _user = new User
            {
                UserName = "Testuser"
            };
            _loginUserDto = new LoginUserDto
            {
                UserName = "Testuser",
                Password = "Password123!"
            };
        }

        [Fact]
        public async Task Login_ValidCredentials_ReturnsOkResult()
        {
            _userManagerMock.Setup(x => x.FindByNameAsync(_loginUserDto.UserName))
                .Returns(Task.FromResult(_user));
            _userManagerMock.Setup(x => x.CheckPasswordAsync(_user, _loginUserDto.Password))
                .Returns(Task.FromResult(true));
            _userManagerMock.Setup(x => x.GetRolesAsync(_user))
                .Returns(Task.FromResult<IList<string>>(new[] { "Admin" }));

            var result = await _controller.Login(_loginUserDto);

            Assert.IsType<OkObjectResult>(result);
        }

        [Fact]
        public async Task Login_InvalidCredentials_ReturnsUnauthorizedResult()
        {
            _userManagerMock.Setup(x => x.FindByNameAsync(_loginUserDto.UserName))
                .Returns(Task.FromResult(_user));
            _userManagerMock.Setup(x => x.CheckPasswordAsync(_user, _loginUserDto.Password))
                .Returns(Task.FromResult(false));

            var result = await _controller.Login(_loginUserDto);

            Assert.IsType<UnauthorizedResult>(result);
        }

        [Fact]
        public async Task Register_ValidInput_ReturnsOkResult()
        {
            _userManagerMock.Setup(x => x.CreateAsync(It.IsAny<User>(), It.IsAny<string>()))
                .Returns(Task.FromResult(IdentityResult.Success));

            var result = await _controller.Register(new RegisterUserDto { UserName = "Testuser", Password = "Password123!" });

            Assert.IsType<OkObjectResult>(result);
        }


        [Fact]
        public async Task Register_InvalidInput_ReturnsBadRequestResult()
        {
            _userManagerMock.Setup(x => x.CreateAsync(It.IsAny<User>(), It.IsAny<string>()))
                .Returns(Task.FromResult(IdentityResult.Failed()));

            var result = await _controller.Register(new RegisterUserDto { UserName = "", Password = "" });

            Assert.IsType<BadRequestObjectResult>(result);
        }

        [Fact]
        public async Task Register_UserExists_ReturnsConflictResult()
        {
            _userManagerMock.Setup(x => x.FindByNameAsync("Testuser"))
                .Returns(Task.FromResult(new User()));

            var result = await _controller.Register(new RegisterUserDto { UserName = "Testuser", Password = "Password123!" });

            Assert.IsType<ConflictObjectResult>(result);
        }
    }
}
