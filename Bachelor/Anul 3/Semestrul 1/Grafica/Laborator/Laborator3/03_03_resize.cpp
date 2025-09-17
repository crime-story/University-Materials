/* TRANSFORMARI.
   - pentru a stabili o fereastra de "decupare" intr-o scena 2D putem folosi 
   atat functia glm::ortho, cat si indicarea explicita a transformarilor
   - in exemplu este decupat dreptunghiul delimitat de xmin, xmax, ymin, ymax
*/

#include <windows.h>  // biblioteci care urmeaza sa fie incluse
#include <stdlib.h> // necesare pentru citirea shader-elor
#include <stdio.h>
#include <math.h>
#include <iostream>
#include <GL/glew.h> // glew apare inainte de freeglut
#include <GL/freeglut.h> // nu trebuie uitat freeglut.h

#include "loadShaders.h"

#include "glm/glm.hpp"  
#include "glm/gtc/matrix_transform.hpp"
#include "glm/gtx/transform.hpp"
#include "glm/gtc/type_ptr.hpp"

//////////////////////////////////////

GLfloat
	WIN_WIDTH = 900, WIN_HEIGHT = 600;

float
	xmin = -400, xmax = 500, ymin = -200, ymax = 400;
float
	deltax = xmax - xmin, deltay = ymax - ymin,
	xcenter = (xmin + xmax) * 0.5, ycenter = (ymin + ymax) * 0.5;

GLuint
	VaoId,
	VboId,
	ColorBufferId,
	ProgramId,
	myMatrixLocation,
	matrScaleLocation,
	matrTranslLocation,
	matrRotlLocation,
	codColLocation;
int codCol;

glm::mat4
	myMatrix, resizeMatrix, matrTransl, mScale, mTrans;

void CreateVBO(void)
{
	// varfurile 
	GLfloat Vertices[] = {
		// cele 4 varfuri din colturi 
		-390.0f, -190.0f, 0.0f, 1.0f,
		490.0f,  -190.0f, 0.0f, 1.0f,
		490.0f, 390.0f, 0.0f, 1.0f,
		-390.0f, 390.0f, 0.0f, 1.0f,
		// varfuri pentru axe
		-400.0f, 0.0f, 0.0f, 1.0f,
		500.0f,  0.0f, 0.0f, 1.0f,
		0.0f, -200.0f, 0.0f, 1.0f,
		0.0f, 400.0f, 0.0f, 1.0f,
		// varfuri pentru dreptunghi
		-50.0f,  -50.0f, 0.0f, 1.0f,
		50.0f, -50.0f, 0.0f, 1.0f,
		50.0f,  50.0f, 0.0f, 1.0f,
		-50.0f,  50.0f, 0.0f, 1.0f,
	};

	// culorile varfurilor din colturi
	GLfloat Colors[] = {
	  1.0f, 0.0f, 0.0f, 1.0f,
	  0.0f, 1.0f, 0.0f, 1.0f,
	  0.0f, 0.0f, 1.0f, 1.0f,
	  0.8f, 0.4f, 0.0f, 1.0f,
	};

	// se creeaza un buffer nou
	glGenBuffers(1, &VboId);
	// este setat ca buffer curent
	glBindBuffer(GL_ARRAY_BUFFER, VboId);
	// punctele sunt "copiate" in bufferul curent
	glBufferData(GL_ARRAY_BUFFER, sizeof(Vertices), Vertices, GL_STATIC_DRAW);

	// se creeaza / se leaga un VAO (Vertex Array Object) - util cand se utilizeaza mai multe VBO
	glGenVertexArrays(1, &VaoId);
	glBindVertexArray(VaoId);
	// se activeaza lucrul cu atribute; atributul 0 = pozitie
	glEnableVertexAttribArray(0);
	// 
	glVertexAttribPointer(0, 4, GL_FLOAT, GL_FALSE, 0, 0);

	// un nou buffer, pentru culoare
	glGenBuffers(1, &ColorBufferId);
	glBindBuffer(GL_ARRAY_BUFFER, ColorBufferId);
	glBufferData(GL_ARRAY_BUFFER, sizeof(Colors), Colors, GL_STATIC_DRAW);
	// atributul 1 =  culoare
	glEnableVertexAttribArray(1);
	glVertexAttribPointer(1, 4, GL_FLOAT, GL_FALSE, 0, 0);
}
void DestroyVBO(void)
{
	glDisableVertexAttribArray(1);
	glDisableVertexAttribArray(0);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glDeleteBuffers(1, &ColorBufferId);
	glDeleteBuffers(1, &VboId);
	glBindVertexArray(0);
	glDeleteVertexArrays(1, &VaoId);
}

void CreateShaders(void)
{
	ProgramId = LoadShaders("03_03_Shader.vert", "03_03_Shader.frag");
	glUseProgram(ProgramId);
}


void DestroyShaders(void)
{
	glDeleteProgram(ProgramId);
}

void Initialize(void)
{
	glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // culoarea de fond a ecranului
	CreateVBO();
	CreateShaders();
	codColLocation = glGetUniformLocation(ProgramId, "codCuloare");
	myMatrixLocation = glGetUniformLocation(ProgramId, "myMatrix");

	// Pentru decupare avem de translatat si de scalat
	// Varianta 1 (recomandat). Se aplica functia glm::ortho
	resizeMatrix = glm::ortho(xmin, xmax, ymin, ymax);

	// Varianta 2. Efectuam explicit compunerea transformarilor
	mScale = glm::scale(glm::mat4(1.0f), glm::vec3(2.f / deltax, 2.f / deltay, 1.0));
	mTrans = glm::translate(glm::mat4(1.0f), glm::vec3(-xcenter, -ycenter, 0.0));
	// resizeMatrix = mScale * mTrans;

	// Matricea de translatie pentru dreptunghi
	matrTransl = glm::translate(glm::mat4(1.0f), glm::vec3(100.f, 100.f, 0.0));
}
void RenderFunction(void)
{
	glClear(GL_COLOR_BUFFER_BIT);
	myMatrix = resizeMatrix;
	glUniformMatrix4fv(myMatrixLocation, 1, GL_FALSE, &myMatrix[0][0]);

	// Puncte din colturi
	// Culoarea
	codCol = 0;
	glUniform1i(codColLocation, codCol);
	// Desenare
	glPointSize(15.0);
	glEnable(GL_POINT_SMOOTH);
	glDrawArrays(GL_POINTS, 0, 4);

	// Axe
	// Culoarea
	codCol = 1;
	glUniform1i(codColLocation, codCol);
	// Desenare
	glLineWidth(2.0);
	glDrawArrays(GL_LINES, 4, 4);

	// Dreptunghiul
	// Matricea asociata - dreptunghiul este translatat fata de axe
	myMatrix = resizeMatrix * matrTransl; // ATENTIE LA ORDINE
	glUniformMatrix4fv(myMatrixLocation, 1, GL_FALSE, &myMatrix[0][0]);
	// Culoarea
	codCol = 2;
	glUniform1i(codColLocation, codCol);
	// Desenare
	glDrawArrays(GL_TRIANGLE_FAN, 8, 4);

	glutPostRedisplay();
	glFlush();
}
void Cleanup(void)
{
	DestroyShaders();
	DestroyVBO();
}

int main(int argc, char* argv[])
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(WIN_WIDTH, WIN_HEIGHT);
	glutCreateWindow("Utilizarea glm::ortho");
	glewInit();
	Initialize();
	glutDisplayFunc(RenderFunction);
	glutCloseFunc(Cleanup);
	glutMainLoop();
}

