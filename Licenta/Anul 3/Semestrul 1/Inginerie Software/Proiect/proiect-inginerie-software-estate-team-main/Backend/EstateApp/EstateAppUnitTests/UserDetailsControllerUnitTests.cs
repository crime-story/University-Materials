using AutoMapper;
using EstateApp.API.Controllers;
using EstateApp.Aplication.Commands;
using EstateApp.Aplication.Queries;
using EstateApp.Domain.Dtos;
using EstateApp.Domain.Models;
using MediatR;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Moq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateAppUnitTests
{
    public class UserDetailsControllerUnitTests
    {
        private readonly UserDetailsController _controller;
        private readonly Mock<ILogger<EstateController>> _loggerMock;
        private readonly Mock<IMediator> _mediatorMock;
        private readonly Mock<IMapper> _mapperMock;

        public UserDetailsControllerUnitTests()
        {
            _loggerMock = new Mock<ILogger<EstateController>>();
            _mediatorMock = new Mock<IMediator>();
            _mapperMock = new Mock<IMapper>();
            _controller = new UserDetailsController(_mediatorMock.Object, _loggerMock.Object, _mapperMock.Object);
        }

        [Fact]
        public async Task CreateUserDetails_ReturnsOkObjectResult_WhenSuccessful()
        {
            // Arrange
            var dto = new CreateUserDetailsDto
            {
                Username = "test",
                FullName = "Test user",
                Age = 25,
                Description = "Test description",
                ProfileURL = "testurl",
                Residence = "Test residence"
            };

            var command = new CreateUserDetailsCommand(dto);
            _mapperMock.Setup(x => x.Map<CreateUserDetailsCommand>(It.IsAny<CreateUserDetailsDto>()))
                .Returns(command);

            _mediatorMock.Setup(x => x.Send(command, It.IsAny<CancellationToken>()))
                .Returns(Task.FromResult(new UserDetails()));

            // Act
            var result = await _controller.CreateUserDetails(dto, CancellationToken.None);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result);
            Assert.IsAssignableFrom<UserDetails>(okResult.Value);
        }
        
        [Fact]
        public async Task CreateUserDetails_ReturnsNotFound_WhenUserAlreadyExists()
        {
            // Arrange
            var dto = new CreateUserDetailsDto
            {
                Username = "test",
                FullName = "Test user",
                Age = 25,
                Description = "Test description",
                ProfileURL = "testurl",
                Residence = "Test residence"
            };
            var command = new CreateUserDetailsCommand(dto);
            _mapperMock.Setup(x => x.Map<CreateUserDetailsCommand>(It.IsAny<CreateUserDetailsDto>()))
                .Returns(command);

            _mediatorMock.Setup(x => x.Send(It.IsAny<CreateUserDetailsCommand>(), It.IsAny<CancellationToken>()));

            // Act
            var result = await _controller.CreateUserDetails(dto, CancellationToken.None);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result);
            Assert.Equal("Client exists", notFoundResult.Value);
        }

        [Fact]
        public async Task GetUsersDetails_ReturnsOkObjectResult_WhenSuccessful()
        {
            // Arrange
            var mockMediator = new Mock<IMediator>();
            var userDetails = new List<UserDetails>
            {
                new UserDetails { FullName = "John Doe", Age = 30, Description = "Test Description", ProfileURL = "https://example.com/image.jpg", Residence = "Test Residence" },
                new UserDetails { FullName = "Jane Smith", Age = 25, Description = "Test Description", ProfileURL = "https://example.com/image.jpg", Residence = "Test Residence" }
            };
            mockMediator.Setup(x => x.Send(It.IsAny<GetUsersDetailsQuery>(), It.IsAny<CancellationToken>())).ReturnsAsync(userDetails);
            var controller = new UserDetailsController(mockMediator.Object, _loggerMock.Object, _mapperMock.Object);

            // Act
            var result = await controller.GetUsersDetails();

            // Assert
            var okObjectResult = Assert.IsType<OkObjectResult>(result);
            var returnValue = Assert.IsType<List<UserDetails>>(okObjectResult.Value);
            Assert.Equal(userDetails, returnValue);
        }
    }
}
