/*
Aplicarea modelului de iluminare in cazul unui cub
- In acest cod sursa toate varfurile au aceeasi culoare.
- Sunt patru posibilitati, intrucat sunt testate variante pentru:
	(i) implementarea modelului de iluminare 
	(a. in shader-ul de fragment, b. in shader-ul de varfuri)
	In acest scop sunt scrise shader-e diferite – 10_02f*, respectiv 10_02v*)
	(ii) modul de alegere a normalelor 
	(a. la nivelul fetelor sau b. la nivelul varfurilor, prin mediere). 
	Implementarea pentru alegerea normalelor este legata doar de programul principal.
- Din programul principal sunt transferate in shader-ul de varfuri 
toate informatiile geometrice 
(coordonate, normale, pozitia observatorului, pozitia sursei de lumina).
- Din shader-ul de varfuri in sunt transferate shaderul de fragment informatii diferite, 
in functie de shader-ul in care este implementat modelul de iluminare 
(de exemplu, daca modelul de iluminare este implementat in shader-ul de varfuri,
in shader-ul de fragment este transferata culoarea varfului, a
ceasta poate fi apoi randata ca atare).
- Folosirea meniurilor glutMenu.
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

using namespace std;

// identificatori 
GLuint
	VaoId,
	VboId,
	ColorBufferId,
	ProgramIdv,
	ProgramIdf,
	viewLocation,
	projLocation,
	codColLocation,
	depthLocation,
	rendermode,
	l1, l2,
	codCol;
GLint objectColorLoc, lightColorLoc, lightPosLoc, viewPosLoc;

// variabile pentru matricea de vizualizare
float Obsx = 0.0, Obsy = -600.0, Obsz = 0.f;
float Refx = 0.0f, Refy = 1000.0f, Refz = 0.0f;
float Vx = 0.0, Vy = 0.0, Vz = 1.0;

// variabile pentru matricea de proiectie
float width = 800, height = 600, znear = 0.1, fov = 45;

// matrice utilizate
glm::mat4 view, projection;

enum {
	Il_Frag, Il_Frag_Av, Il_Vert, Il_Vert_Av
};
void menu(int selection)
{
	rendermode = selection;
	glutPostRedisplay();
}
void processNormalKeys(unsigned char key, int x, int y)
{
	switch (key) {
	case 'l':
		Vx -= 0.1;
		break;
	case 'r':
		Vx += 0.1;
		break;
	case '+':
		Obsy += 10;
		break;
	case '-':
		Obsy -= 10;
		break;
	}
	if (key == 27)
		exit(0);
}
void processSpecialKeys(int key, int xx, int yy)
{
	switch (key) {
	case GLUT_KEY_LEFT:
		Obsx -= 20;
		break;
	case GLUT_KEY_RIGHT:
		Obsx += 20;
		break;
	case GLUT_KEY_UP:
		Obsz += 20;
		break;
	case GLUT_KEY_DOWN:
		Obsz -= 20;
		break;
	}
}
void CreateVBO(void)
{
	// varfurile 
	GLfloat Vertices[] =
	{
		// inspre Oz'
		  -50.f, -50.f, -50.f,  0.0f,  0.0f, -1.0f,
		   50.f, -50.f, -50.f,  0.0f,  0.0f, -1.0f,
		   50.f,  50.f, -50.f,  0.0f,  0.0f, -1.0f,
		   50.f,  50.f, -50.f,  0.0f,  0.0f, -1.0f,
		  -50.f,  50.f, -50.f,  0.0f,  0.0f, -1.0f,
		  -50.f, -50.f, -50.f,  0.0f,  0.0f, -1.0f,

		  // inspre Oz
		  -50.f, -50.f,  50.f,  0.0f,  0.0f,  1.0f,
		   50.f, -50.f,  50.f,  0.0f,  0.0f,  1.0f,
		   50.f,  50.f,  50.f,  0.0f,  0.0f,  1.0f,
		   50.f,  50.f,  50.f,  0.0f,  0.0f,  1.0f,
		  -50.f,  50.f,  50.f,  0.0f,  0.0f,  1.0f,
		  -50.f, -50.f,  50.f,  0.0f,  0.0f,  1.0f,

		  // inspre Ox'
		  -50.f,  50.f,  50.f, -1.0f,  0.0f,  0.0f,
		  -50.f,  50.f, -50.f, -1.0f,  0.0f,  0.0f,
		  -50.f, -50.f, -50.f, -1.0f,  0.0f,  0.0f,
		  -50.f, -50.f, -50.f, -1.0f,  0.0f,  0.0f,
		  -50.f, -50.f,  50.f, -1.0f,  0.0f,  0.0f,
		  -50.f,  50.f,  50.f, -1.0f,  0.0f,  0.0f,

		  // inspre Ox
		   50.f,  50.f,  50.f,  1.0f,  0.0f,  0.0f,
		   50.f,  50.f, -50.f,  1.0f,  0.0f,  0.0f,
		   50.f, -50.f, -50.f,  1.0f,  0.0f,  0.0f,
		   50.f, -50.f, -50.f,  1.0f,  0.0f,  0.0f,
		   50.f, -50.f,  50.f,  1.0f,  0.0f,  0.0f,
		   50.f,  50.f,  50.f,  1.0f,  0.0f,  0.0f,

		   // inspre Oy'
		  -50.f, -50.f, -50.f,  0.0f, -1.0f,  0.0f,
		   50.f, -50.f, -50.f,  0.0f, -1.0f,  0.0f,
		   50.f, -50.f,  50.f,  0.0f, -1.0f,  0.0f,
		   50.f, -50.f,  50.f,  0.0f, -1.0f,  0.0f,
		  -50.f, -50.f,  50.f,  0.0f, -1.0f,  0.0f,
		  -50.f, -50.f, -50.f,  0.0f, -1.0f,  0.0f,

		  // inspre Oy
		  -50.f,  50.f, -50.f,  0.0f,  1.0f,  0.0f,
		   50.f,  50.f, -50.f,  0.0f,  1.0f,  0.0f,
		   50.f,  50.f,  50.f,  0.0f,  1.0f,  0.0f,
		   50.f,  50.f,  50.f,  0.0f,  1.0f,  0.0f,
		  -50.f,  50.f,  50.f,  0.0f,  1.0f,  0.0f,
		  -50.f,  50.f, -50.f,  0.0f,  1.0f,  0.0f,

		  // Fiecare varf cu normala lui

		  // inspre Oz'
			-50.f, -50.f, -50.f,  -1.0f,  -1.0f, -1.0f,
			 50.f, -50.f, -50.f,  1.0f,  -1.0f, -1.0f,
			 50.f,  50.f, -50.f,  1.0f,  1.0f, -1.0f,
			 50.f,  50.f, -50.f,  1.0f,  1.0f, -1.0f,
			-50.f,  50.f, -50.f,  -1.0f,  1.0f, -1.0f,
			-50.f, -50.f, -50.f,  -1.0f,  -1.0f, -1.0f,

			// inspre Oz
			-50.f, -50.f,  50.f,  -1.0f,  -1.0f,  1.0f,
			 50.f, -50.f,  50.f,  1.0f,  -1.0f,  1.0f,
			 50.f,  50.f,  50.f,  1.0f,  1.0f,  1.0f,
			 50.f,  50.f,  50.f,  1.0f,  1.0f,  1.0f,
			-50.f,  50.f,  50.f,  -1.0f,  1.0f,  1.0f,
			-50.f, -50.f,  50.f,  -1.0f,  -1.0f,  1.0f,

			// inspre Ox'
			-50.f,  50.f,  50.f, -1.0f,  1.0f,  1.0f,
			-50.f,  50.f, -50.f, -1.0f,  1.0f,  -1.0f,
			-50.f, -50.f, -50.f, -1.0f,  -1.0f,  -1.0f,
			-50.f, -50.f, -50.f, -1.0f,  -1.0f,  -1.0f,
			-50.f, -50.f,  50.f, -1.0f,  -1.0f,  1.0f,
			-50.f,  50.f,  50.f, -1.0f,  1.0f,  1.0f,

			// inspre Ox
			 50.f,  50.f,  50.f,  1.0f,  1.0f,  1.0f,
			 50.f,  50.f, -50.f,  1.0f,  1.0f,  -1.0f,
			 50.f, -50.f, -50.f,  1.0f,  -1.0f,  -1.0f,
			 50.f, -50.f, -50.f,  1.0f,  -1.0f,  -1.0f,
			 50.f, -50.f,  50.f,  1.0f,  -1.0f,  1.0f,
			 50.f,  50.f,  50.f,  1.0f,  1.0f,  1.0f,

			 // inspre Oy'
			-50.f, -50.f, -50.f,  -1.0f, -1.0f,  -1.0f,
			 50.f, -50.f, -50.f,  1.0f, -1.0f,  -1.0f,
			 50.f, -50.f,  50.f,  1.0f, -1.0f,  1.0f,
			 50.f, -50.f,  50.f,  1.0f, -1.0f,  1.0f,
			-50.f, -50.f,  50.f,  -1.0f, -1.0f,  1.0f,
			-50.f, -50.f, -50.f,  -1.0f, -1.0f,  -1.0f,

			// inspre Oy
			-50.f,  50.f, -50.f,  -1.0f,  1.0f,  -1.0f,
			 50.f,  50.f, -50.f,  1.0f,  1.0f,  -1.0f,
			 50.f,  50.f,  50.f,  1.0f,  1.0f,  1.0f,
			 50.f,  50.f,  50.f,  1.0f,  1.0f,  1.0f,
			-50.f,  50.f,  50.f,  -1.0f,  1.0f,  1.0f,
			-50.f,  50.f, -50.f,  -1.0f,  1.0f,  -1.0f
	};

	glGenVertexArrays(1, &VaoId);
	glGenBuffers(1, &VboId);
	glBindVertexArray(VaoId);
	glBindBuffer(GL_ARRAY_BUFFER, VboId);
	glBufferData(GL_ARRAY_BUFFER, sizeof(Vertices), Vertices, GL_STATIC_DRAW);

	glEnableVertexAttribArray(0); // atributul 0 = pozitie
	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(GLfloat), (GLvoid*)0);
	glEnableVertexAttribArray(1); // atributul 1 = normale
	glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(GLfloat), (GLvoid*)(3 * sizeof(GLfloat)));
}
void DestroyVBO(void)
{
	glDisableVertexAttribArray(1);
	glDisableVertexAttribArray(0);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glDeleteBuffers(1, &VboId);
	glBindVertexArray(0);
	glDeleteVertexArrays(1, &VaoId);
}
void CreateShadersVertex(void)
{
	ProgramIdv = LoadShaders("10_02v_Shader.vert", "10_02v_Shader.frag");
	glUseProgram(ProgramIdv);
}
void CreateShadersFragment(void)
{
	ProgramIdf = LoadShaders("10_02f_Shader.vert", "10_02f_Shader.frag");
	glUseProgram(ProgramIdf);
}
void DestroyShaders(void)
{
	glDeleteProgram(ProgramIdv);
	glDeleteProgram(ProgramIdf);
}
void Initialize(void)
{
	glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // culoarea de fond a ecranului
	CreateVBO();
	CreateShadersFragment();
	objectColorLoc = glGetUniformLocation(ProgramIdf, "objectColor");
	lightColorLoc = glGetUniformLocation(ProgramIdf, "lightColor");
	lightPosLoc = glGetUniformLocation(ProgramIdf, "lightPos");
	viewPosLoc = glGetUniformLocation(ProgramIdf, "viewPos");
	viewLocation = glGetUniformLocation(ProgramIdf, "view");
	projLocation = glGetUniformLocation(ProgramIdf, "projection");
	CreateShadersVertex();
	objectColorLoc = glGetUniformLocation(ProgramIdv, "objectColor");
	lightColorLoc = glGetUniformLocation(ProgramIdv, "lightColor");
	lightPosLoc = glGetUniformLocation(ProgramIdv, "lightPos");
	viewPosLoc = glGetUniformLocation(ProgramIdv, "viewPos");
	viewLocation = glGetUniformLocation(ProgramIdv, "view");
	projLocation = glGetUniformLocation(ProgramIdv, "projection");
}
void RenderFunction(void)
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);

	// vizualizare + proiectie
	glm::vec3 Obs = glm::vec3(Obsx, Obsy, Obsz);
	glm::vec3 PctRef = glm::vec3(Refx, Refy, Refz);
	glm::vec3 Vert = glm::vec3(Vx, Vy, Vz);
	view = glm::lookAt(Obs, PctRef, Vert);
	glUniformMatrix4fv(viewLocation, 1, GL_FALSE, &view[0][0]);
	projection = glm::infinitePerspective(fov, GLfloat(width) / GLfloat(height), znear);
	glUniformMatrix4fv(projLocation, 1, GL_FALSE, &projection[0][0]);

	// variabile uniforme pentru iluminare
	glUniform3f(objectColorLoc, 1.0f, 0.5f, 0.4f);
	glUniform3f(lightColorLoc, 1.0f, 1.0f, 1.0f);
	glUniform3f(lightPosLoc, 400.f, -400.f, 400.f);
	glUniform3f(viewPosLoc, Obsx, Obsy, Obsz);
	switch (rendermode)
	{
	// modelul de iluminare implementat in shader-ul de fragment
	case Il_Frag: 
		glUseProgram(ProgramIdf);
		l1 = 0; l2 = 36; // normale pentru fete, sunt folosite varfurile 0-35
		break;
	case Il_Frag_Av: // normale pentru varfuri (mediere), sunt folosite varfurile 36-71
		glUseProgram(ProgramIdf);
		l1 = 36; l2 = 36;
		break;

	// modelul de iluminare implementat in shader-ul de varfuri
	case Il_Vert:
		glUseProgram(ProgramIdv);
		l1 = 0; l2 = 36; // normale pentru fete, sunt folosite varfurile 0-35
		break;
	case Il_Vert_Av:
		glUseProgram(ProgramIdv);
		l1 = 36; l2 = 36; // normale pentru varfuri (mediere), sunt folosite varfurile 36-71
		break;
	};

	// desenare
	glDrawArrays(GL_TRIANGLES, l1, l2);

	glutSwapBuffers();
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
	glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE);
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(1200, 900);
	glutCreateWindow("Variante pentru implementarea modelului de iluminare in shadere");
	glewInit();
	Initialize();
	glutIdleFunc(RenderFunction);
	glutDisplayFunc(RenderFunction);
	glutKeyboardFunc(processNormalKeys);
	glutSpecialFunc(processSpecialKeys);
	glutCreateMenu(menu);
	glutAddMenuEntry("Fragment", Il_Frag);
	glutAddMenuEntry("Fragment+Mediere_Normale", Il_Frag_Av);
	glutAddMenuEntry("Varfuri", Il_Vert);
	glutAddMenuEntry("Varfuri+Mediere_Normale", Il_Vert_Av);
	glutAttachMenu(GLUT_RIGHT_BUTTON);
	glutCloseFunc(Cleanup);
	glutMainLoop();

}

