using EstateApp.Domain.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EstateApp.Infrastructure.Configuartions
{
    internal class EstateImageConfiguration : IEntityTypeConfiguration<EstateImage>
    {
        public void Configure(EntityTypeBuilder<EstateImage> builder)
        {
            builder.HasOne(image => image.Estate)
                .WithMany(estate => estate.Images)
                .HasForeignKey(image => image.EstateID)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
