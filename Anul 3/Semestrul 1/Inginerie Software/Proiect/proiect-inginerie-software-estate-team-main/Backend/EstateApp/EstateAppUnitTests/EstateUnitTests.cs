using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using EstateApp.Aplication.Abstract;
using Moq;
using Xunit;
using EstateApp.Domain.Models;
using EstateApp.API.Controllers;
using Microsoft.Extensions.Logging;
using AutoMapper;
using MediatR;
using Microsoft.AspNetCore.Mvc;
using EstateApp.Aplication.Queries;
using System.Net;
using EstateApp.Infrastructure.Data;
using EstateApp.Infrastructure.Repositories;
using Microsoft.EntityFrameworkCore;

namespace EstateAppUnitTests
{
    public class EstateUnitTests
    {
        [Fact]
        public async Task GetUserDetailsByUsername_ReturnsExpectedUser()
        {
            // Arrange
            var expectedUserDetails = new UserDetails
            {
                Username = "Robertto",
                FullName = "John",
                Age = 21,
                Residence = "Pipera",
                Description = "Profesor"
            };

            var mockMediator = new Mock<IMediator>();
            mockMediator.Setup(x => x.Send(It.IsAny<GetUserDetailsByUsernameQuery>(), default(System.Threading.CancellationToken))).ReturnsAsync(expectedUserDetails);

            var mockLogger = new Mock<ILogger<EstateController>>();
            var mockMapper = new Mock<IMapper>();
            var controller = new UserDetailsController(mockMediator.Object, mockLogger.Object, mockMapper.Object);

            // Act
            var result = await controller.GetUserDetailsByUsername("Robertto");

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result);
            var actualUserDetails = Assert.IsAssignableFrom<UserDetails>(okResult.Value);
            Assert.Equal(expectedUserDetails, actualUserDetails);
        }

        [Fact]
        public async Task GetEstatesByUsername_ReturnsExpectedEstates()
        {
            // Arrange
            var expectedEstates = new List<Estate>
                {
                    new Estate {
                        EstateID = 1,
                        Surface = 2d,
                        Price = 1000,
                        NumberOfRooms = 2,
                        Status = "lol",
                        Description = "Frumos",
                        Username = "Robertto",
                        Address = "La Jador 7",
                        Neighborhood = "Ferentari"
                    }
                };

            var mockMediator = new Mock<IMediator>();
            mockMediator.Setup(x => x.Send(It.IsAny<GetEstatesByUsernameQuery>(), default(System.Threading.CancellationToken))).ReturnsAsync(expectedEstates);

            var mockLogger = new Mock<ILogger<EstateController>>();
            var mockMapper = new Mock<IMapper>();
            var controller = new EstateController(mockMediator.Object, mockLogger.Object, mockMapper.Object);

            // Act
            var result = await controller.GetEstatesByUsername("Robertto");

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result);
            var actualEstates = Assert.IsAssignableFrom<IEnumerable<Estate>>(okResult.Value);
            Assert.Equal(expectedEstates, actualEstates);
        }
    }
}