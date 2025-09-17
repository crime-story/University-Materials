using Microsoft.EntityFrameworkCore.Migrations;

namespace Lab2ProjectWeb.Migrations
{
    public partial class AddedManyToManyTable : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Concursuri",
                columns: table => new
                {
                    Id = table.Column<string>(type: "nvarchar(450)", nullable: false),
                    Name = table.Column<string>(type: "nvarchar(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Concursuri", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "AuthorConcursuri",
                columns: table => new
                {
                    AuthorId = table.Column<string>(type: "nvarchar(450)", nullable: false),
                    ConcursId = table.Column<string>(type: "nvarchar(450)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AuthorConcursuri", x => new { x.AuthorId, x.ConcursId });
                    table.ForeignKey(
                        name: "FK_AuthorConcursuri_Authors_AuthorId",
                        column: x => x.AuthorId,
                        principalTable: "Authors",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_AuthorConcursuri_Concursuri_ConcursId",
                        column: x => x.ConcursId,
                        principalTable: "Concursuri",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_AuthorConcursuri_ConcursId",
                table: "AuthorConcursuri",
                column: "ConcursId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "AuthorConcursuri");

            migrationBuilder.DropTable(
                name: "Concursuri");
        }
    }
}
