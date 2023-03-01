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
    public class EstateConfiguration : IEntityTypeConfiguration<Estate>
    {
        public void Configure(EntityTypeBuilder<Estate> builder)
        {
            builder.HasOne(estate => estate.Location)
                .WithOne(location => location.Estate)
                .HasForeignKey<Location>(location => location.EstateID)
                .OnDelete(DeleteBehavior.Cascade);
            builder.HasOne(estate => estate.User)
                .WithMany(user => user.Estates)
                .HasForeignKey(estate => estate.Username)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
