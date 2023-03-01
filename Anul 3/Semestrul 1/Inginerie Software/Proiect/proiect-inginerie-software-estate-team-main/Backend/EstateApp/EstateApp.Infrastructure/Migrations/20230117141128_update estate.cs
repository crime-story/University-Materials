using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace EstateApp.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class updateestate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "Address",
                table: "Estates",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");

            migrationBuilder.AddColumn<string>(
                name: "Neighborhood",
                table: "Estates",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");

            migrationBuilder.AddColumn<int>(
                name: "Sector",
                table: "Estates",
                type: "int",
                nullable: false,
                defaultValue: 0);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Address",
                table: "Estates");

            migrationBuilder.DropColumn(
                name: "Neighborhood",
                table: "Estates");

            migrationBuilder.DropColumn(
                name: "Sector",
                table: "Estates");
        }
    }
}
