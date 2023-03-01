/* DESCRIERE: DREPTUNGHI CU SATELIT -- varianta cu OpenGL "nou"
	- utilizeaza diverse transformari si compunerea acestora folosind biblioteca glm
	- functii pentru utilizarea mouse-ului
*/

#include <windows.h>  // biblioteci care urmeaza sa fie incluse
#include <stdlib.h> // necesare pentru citirea shader-elor
#include <stdio.h>
#include <math.h>
#include <iostream>
#include <GL/glew.h> // glew apare inainte de freeglut
#include <GL/freeglut.h> // nu trebuie uitat freeglut.h
#include "loadShaders.h"

// Din biblioteca glm
#include "glm/glm.hpp"  
#include "glm/gtc/matrix_transform.hpp"
#include "glm/gtx/transform.hpp"
#include "glm/gtc/type_ptr.hpp"

using namespace std;

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
float PI = 3.141592, angle = 0;
float tx = 0; float ty = 0;
float width = 500, height = 500;
float i = 0.0, j = 0.0, h = 0.0, k = 0.0, l = 0.0,alpha4 = 0.0, alpha2 = 0.0, alpha3 = 0.0, step2 = 0.5, step3 = 1.5, alpha = 0.0, step = 0.1, beta = 0.003, ok = 0;
glm::mat4
myMatrix, resizeMatrix, matrTransl, matrTransl2, matrTransl3, matrScale1, matrScale2, matrRot, matrDepl;

void displayMatrix()
{
	for (int ii = 0; ii < 4; ii++)
	{
		for (int jj = 0; jj < 4; jj++)
			cout << myMatrix[ii][jj] << "  ";
		cout << endl;
	};
	cout << "\n";
};

void miscad(void)
{
	if (i > -1 && i <= 400 && j <= 750)
	{
		i = i + alpha;
		alpha = +step;
	}
	if (j > -1.0 && j <= 150) {
		j = j + alpha2;
		alpha2 = +step2;
	}
	
	if (j >= 150 && j <= 250 && angle <= 0.5) {
		angle += beta;
		l = l + alpha3;
		alpha3 = +step3;
	}
	
	if (l >= 200 && l <= 370 && angle >= 0.5) {
		j = j + alpha2;
		alpha2 = +step2;
		l = l + alpha3;
		alpha3 = +step2;
	}
	if (l >= 370 && angle <= 0.6 && angle >= 0) {
		angle -= beta;
		j = j + alpha2;
		alpha2 = +step3;
	}
	if (l >= 370 && j <= 850 && angle <= 0) {
		j = j + alpha2;
		alpha2 = +step3;
	}
	if (j >= 750 && h >= -230) {
		h = h - alpha4;
		alpha4 = +step3;
	}
	
	glutPostRedisplay();
}




void mouse(int button, int state, int x, int y)
{
	switch (button) {
	case GLUT_LEFT_BUTTON:
		if (state == GLUT_DOWN) {
			alpha = -step;
			alpha2 = -step2;
		}
		glutIdleFunc(miscad);
		break;
	case GLUT_RIGHT_BUTTON:
		if (state == GLUT_DOWN) {
			alpha = step;
			alpha2 = step2;
		}
		glutIdleFunc(miscad);
		break;
	default:
		break;
	}
}

void CreateVBO(void)
{
	// varfurile 
	GLfloat Vertices[] = {
		// varfuri pentru axe
		-500.0f, 500.0f, 0.0f, 1.0f,
		500.0f, 500.0f, 0.0f, 1.0f,
		500.0f, -500.0f, 0.0f, 1.0f,
		-500.0f, -500.0f, 0.0f, 1.0f,

		-500.0f, 350.0f, 0.0f, 1.0f,
		500.0f, 350.0f, 0.0f, 1.0f,
		500.0f, -350.0f, 0.0f, 1.0f,
		-500.0f, -350.0f, 0.0f, 1.0f,

		-490.0f, 20.0f, 0.0f, 1.0f,
		-390.0f, 20.0f, 0.0f, 1.0f,
		-390.0f, -20.0f, 0.0f, 1.0f,
		-490.0f, -20.0f, 0.0f, 1.0f,

		-350.0f, 20.0f, 0.0f, 1.0f,
		-250.0f, 20.0f, 0.0f, 1.0f,
		-250.0f, -20.0f, 0.0f, 1.0f,
		-350.0f, -20.0f, 0.0f, 1.0f,

		-210.0f, 20.0f, 0.0f, 1.0f,
		-110.0f, 20.0f, 0.0f, 1.0f,
		-110.0f, -20.0f, 0.0f, 1.0f,
		-210.0f, -20.0f, 0.0f, 1.0f,

		-70.0f, 20.0f, 0.0f, 1.0f,
		30.0f, 20.0f, 0.0f, 1.0f,
		30.0f, -20.0f, 0.0f, 1.0f,
		-70.0f, -20.0f, 0.0f, 1.0f,

		70.0f, 20.0f, 0.0f, 1.0f,
		170.0f, 20.0f, 0.0f, 1.0f,
		170.0f, -20.0f, 0.0f, 1.0f,
		70.0f, -20.0f, 0.0f, 1.0f,

		210.0f, 20.0f, 0.0f, 1.0f,
		310.0f, 20.0f, 0.0f, 1.0f,
		310.0f, -20.0f, 0.0f, 1.0f,
		210.0f, -20.0f, 0.0f, 1.0f,

		350.0f, 20.0f, 0.0f, 1.0f,
		450.0f, 20.0f, 0.0f, 1.0f,
		450.0f, -20.0f, 0.0f, 1.0f,
		350.0f, -20.0f, 0.0f, 1.0f,

		490.0f, 20.0f, 0.0f, 1.0f,
		500.0f, 20.0f, 0.0f, 1.0f,
		500.0f, -20.0f, 0.0f, 1.0f,
		490.0f, -20.0f, 0.0f, 1.0f,

		-450.0f, 250.0f, 0.0f, 1.0f,
		-300.0f, 250.0f, 0.0f, 1.0f,
		-300.0f, 120.0f, 0.0f, 1.0f,
		-450.0f, 120.0f, 0.0f, 1.0f,

		-450.0f, -250.0f, 0.0f, 1.0f,
		-300.0f, -250.0f, 0.0f, 1.0f,
		-300.0f, -120.0f, 0.0f, 1.0f,
		-450.0f, -120.0f, 0.0f, 1.0f,

		// - WRITING TEXT: "FINISH" - //
		
		// WORD "F"
		475.0f, 320.0f, 0.0f, 1.0f,
		375.0f, 320.0f, 0.0f, 1.0f,
		375.0f, 290.0f, 0.0f, 1.0f,
		475.0f, 290.0f, 0.0f, 1.0f,

		475.0f, 290.0f, 0.0f, 1.0f,
		475.0f, 240.0f, 0.0f, 1.0f,
		455.0f, 240.0f, 0.0f, 1.0f,
		455.0f, 290.0f, 0.0f, 1.0f,

		435.0f, 290.0f, 0.0f, 1.0f,
		435.0f, 240.0f, 0.0f, 1.0f,
		415.0f, 240.0f, 0.0f, 1.0f,
		415.0f, 290.0f, 0.0f, 1.0f,
		
		// WORD "I"
		475.0f, 210.0f, 0.0f, 1.0f,
		375.0f, 210.0f, 0.0f, 1.0f,
		375.0f, 180.0f, 0.0f, 1.0f,
		475.0f, 180.0f, 0.0f, 1.0f,
		
		//WORD "N"
		475.0f, 150.0f, 0.0f, 1.0f,
		375.0f, 150.0f, 0.0f, 1.0f,
		375.0f, 120.0f, 0.0f, 1.0f,
		475.0f, 120.0f, 0.0f, 1.0f,

		475.0f, 70.0f, 0.0f, 1.0f,
		375.0f, 70.0f, 0.0f, 1.0f,
		375.0f, 40.0f, 0.0f, 1.0f,
		475.0f, 40.0f, 0.0f, 1.0f,

		475.0f, 120.0f, 0.0f, 1.0f,
		445.0f, 120.0f, 0.0f, 1.0f,
		375.0f, 70.0f, 0.0f, 1.0f,
		405.0f, 70.0f, 0.0f, 1.0f,

		// WORD "I"
		475.0f, -40.0f, 0.0f, 1.0f,
		375.0f, -40.0f, 0.0f, 1.0f,
		375.0f, -70.0f, 0.0f, 1.0f,
		475.0f, -70.0f, 0.0f, 1.0f,

		// WORD "S"
		475.0f, -100.0f, 0.0f, 1.0f,
		455.0f, -100.0f, 0.0f, 1.0f,
		455.0f, -170.0f, 0.0f, 1.0f,
		475.0f, -170.0f, 0.0f, 1.0f,

		475.0f, -100.0f, 0.0f, 1.0f,
		415.0f, -100.0f, 0.0f, 1.0f,
		415.0f, -130.0f, 0.0f, 1.0f,
		475.0f, -130.0f, 0.0f, 1.0f,

		415.0f, -100.0f, 0.0f, 1.0f,
		435.0f, -100.0f, 0.0f, 1.0f,
		435.0f, -170.0f, 0.0f, 1.0f,
		415.0f, -170.0f, 0.0f, 1.0f,

		415.0f, -170.0f, 0.0f, 1.0f,
		415.0f, -140.0f, 0.0f, 1.0f,
		375.0f, -140.0f, 0.0f, 1.0f,
		375.0f, -170.0f, 0.0f, 1.0f,

		375.0f, -100.0f, 0.0f, 1.0f,
		395.0f, -100.0f, 0.0f, 1.0f,
		395.0f, -170.0f, 0.0f, 1.0f,
		375.0f, -170.0f, 0.0f, 1.0f,

		// WORD "H"
		475.0f, -200.0f, 0.0f, 1.0f,
		375.0f, -200.0f, 0.0f, 1.0f,
		375.0f, -230.0f, 0.0f, 1.0f,
		475.0f, -230.0f, 0.0f, 1.0f,

		475.0f, -280.0f, 0.0f, 1.0f,
		375.0f, -280.0f, 0.0f, 1.0f,
		375.0f, -310.0f, 0.0f, 1.0f,
		475.0f, -310.0f, 0.0f, 1.0f,

		435.0f, -310.0f, 0.0f, 1.0f,
		415.0f, -310.0f, 0.0f, 1.0f,
		415.0f, -200.0f, 0.0f, 1.0f,
		435.0f, -200.0f, 0.0f, 1.0f,

		// - WRITING TEXT: "BLUE WIN" - //

		// WORD "B"
		-300.0f, 700.0f, 0.0f, 1.0f,
		-300.0f, 600.0f, 0.0f, 1.0f,
		-280.0f, 600.f, 0.0f, 1.0f,
		-280.0f, 700.0f, 0.0f, 1.0f,

		-250.0f, 700.0f, 0.0f, 1.0f,
		-250.0f, 600.0f, 0.0f, 1.0f,
		-230.0f, 600.f, 0.0f, 1.0f,
		-230.0f, 700.0f, 0.0f, 1.0f,

		-300.0f, 700.0f, 0.0f, 1.0f,
		-300.0f, 680.0f, 0.0f, 1.0f,
		-230.0f, 680.f, 0.0f, 1.0f,
		-230.0f, 700.0f, 0.0f, 1.0f,

		-300.0f, 660.0f, 0.0f, 1.0f,
		-300.0f, 640.0f, 0.0f, 1.0f,
		-230.0f, 640.f, 0.0f, 1.0f,
		-230.0f, 660.0f, 0.0f, 1.0f,

		-300.0f, 620.0f, 0.0f, 1.0f,
		-300.0f, 600.0f, 0.0f, 1.0f,
		-230.0f, 600.f, 0.0f, 1.0f,
		-230.0f, 620.0f, 0.0f, 1.0f,

		// WORD "L"
		-200.0f, 700.0f, 0.0f, 1.0f,
		-200.0f, 600.0f, 0.0f, 1.0f,
		-180.0f, 600.f, 0.0f, 1.0f,
		-180.0f, 700.0f, 0.0f, 1.0f,

		-200.0f, 620.0f, 0.0f, 1.0f,
		-200.0f, 600.0f, 0.0f, 1.0f,
		-150.0f, 600.f, 0.0f, 1.0f,
		-150.0f, 620.0f, 0.0f, 1.0f,

		// WORD "U"
		-120.0f, 700.0f, 0.0f, 1.0f,
		-120.0f, 600.0f, 0.0f, 1.0f,
		-100.0f, 600.f, 0.0f, 1.0f,
		-100.0f, 700.0f, 0.0f, 1.0f,

		-70.0f, 700.0f, 0.0f, 1.0f,
		-70.0f, 600.0f, 0.0f, 1.0f,
		-50.0f, 600.f, 0.0f, 1.0f,
		-50.0f, 700.0f, 0.0f, 1.0f,

		-120.0f, 620.0f, 0.0f, 1.0f,
		-50.0f, 620.0f, 0.0f, 1.0f,
		-50.0f, 600.0f, 0.0f, 1.0f,
		-120.0f, 600.0f, 0.0f, 1.0f,

		// WORD "E"
		-20.0f, 700.0f, 0.0f, 1.0f,
		-20.0f, 600.0f, 0.0f, 1.0f,
		0.0f, 600.f, 0.0f, 1.0f,
		0.0f, 700.0f, 0.0f, 1.0f,

		-20.0f, 620.0f, 0.0f, 1.0f,
		30.0f, 620.0f, 0.0f, 1.0f,
		30.0f, 600.0f, 0.0f, 1.0f,
		-20.0f, 600.0f, 0.0f, 1.0f,

		-20.0f, 660.0f, 0.0f, 1.0f,
		30.0f, 660.0f, 0.0f, 1.0f,
		30.0f, 640.0f, 0.0f, 1.0f,
		-20.0f, 640.0f, 0.0f, 1.0f,

		-20.0f, 700.0f, 0.0f, 1.0f,
		30.0f, 700.0f, 0.0f, 1.0f,
		30.0f, 680.0f, 0.0f, 1.0f,
		-20.0f, 680.0f, 0.0f, 1.0f,

		// WORD "W"
		100.0f, 700.0f, 0.0f, 1.0f,
		120.0f, 700.0f, 0.0f, 1.0f,
		140.0f, 600.0f, 0.0f, 1.0f,
		120.0f, 600.0f, 0.0f, 1.0f,

		140.0f, 600.0f, 0.0f, 1.0f,
		120.0f, 600.0f, 0.0f, 1.0f,
		140.0f, 670.0f, 0.0f, 1.0f,
		160.0f, 670.0f, 0.0f, 1.0f,

		140.0f, 670.0f, 0.0f, 1.0f,
		160.0f, 670.0f, 0.0f, 1.0f,
		180.0f, 600.0f, 0.0f, 1.0f,
		160.0f, 600.0f, 0.0f, 1.0f,

		180.0f, 600.0f, 0.0f, 1.0f,
		160.0f, 600.0f, 0.0f, 1.0f,
		180.0f, 700.0f, 0.0f, 1.0f,
		200.0f, 700.0f, 0.0f, 1.0f,

		// WORD "I"
		220.0f, 700.0f, 0.0f, 1.0f,
		240.0f, 700.0f, 0.0f, 1.0f,
		240.0f, 600.0f, 0.0f, 1.0f,
		220.0f, 600.0f, 0.0f, 1.0f,

		// WORD "N"
		260.0f, 700.0f, 0.0f, 1.0f,
		280.0f, 700.0f, 0.0f, 1.0f,
		280.0f, 600.0f, 0.0f, 1.0f,
		260.0f, 600.0f, 0.0f, 1.0f,

		310.0f, 700.0f, 0.0f, 1.0f,
		330.0f, 700.0f, 0.0f, 1.0f,
		330.0f, 600.0f, 0.0f, 1.0f,
		310.0f, 600.0f, 0.0f, 1.0f,

		280.0f, 700.0f, 0.0f, 1.0f,
		280.0f, 670.0f, 0.0f, 1.0f,
		310.0f, 600.0f, 0.0f, 1.0f,
		310.0f, 630.0f, 0.0f, 1.0f,

		// RED CAR
		-450.0f, 120.0f, 0.0f, 1.0f,
		-320.0f, 120.0f, 0.0f, 1.0f,
		-320.0f, 150.0f, 0.0f, 1.0f,
		-450.0f, 150.0f, 0.0f, 1.0f,
		-450.0f, 120.0f, 0.0f, 1.0f,

		-450.0f, 150.0f, 0.0f, 1.0f,
		-320.0f, 150.0f, 0.0f, 1.0f,
		-350.0f, 220.0f, 0.0f, 1.0f,
		-450.0f, 220.0f, 0.0f, 1.0f,
		-450.0f, 150.0f, 0.0f, 1.0f,

		-320.0f, 120.0f, 0.0f, 1.0f,
		-320.0f, 150.0f, 0.0f, 1.0f,
		-280.0f, 220.0f, 0.0f, 1.0f,
		-280.0f, 190.0f, 0.0f, 1.0f,
		-320.0f, 120.0f, 0.0f, 1.0f,

		-350.0f, 220.0f, 0.0f, 1.0f,
		-320.0f, 150.0f, 0.0f, 1.0f,
		-280.0f, 220.0f, 0.0f, 1.0f,
		-310.0f, 290.0f, 0.0f, 1.0f,
		-350.0f, 220.0f, 0.0f, 1.0f,

		-310.0f, 290.0f, 0.0f, 1.0f,
		-350.0f, 220.0f, 0.0f, 1.0f,
		-450.0f, 220.0f, 0.0f, 1.0f,
		-410.0f, 290.0f, 0.0f, 1.0f,
		-310.0f, 290.0f, 0.0f, 1.0f,

		-310.0f, 290.0f, 0.0f, 1.0f,
		-350.0f, 220.0f, 0.0f, 1.0f,
		-450.0f, 220.0f, 0.0f, 1.0f,
		-410.0f, 290.0f, 0.0f, 1.0f,
		-310.0f, 290.0f, 0.0f, 1.0f,

		-430.0f, 230.0f, 0.0f, 1.0f,
		-400.0f, 280.0f, 0.0f, 1.0f,
		-350.0f, 280.0f, 0.0f, 1.0f,
		-380.0f, 230.0f, 0.0f, 1.0f,
		-430.0f, 230.0f, 0.0f, 1.0f,

		-320.0f, 170.0f, 0.0f, 1.0f,
		-340.0f, 220.0f, 0.0f, 1.0f,
		-310.0f, 270.0f, 0.0f, 1.0f,
		-290.0f, 220.0f, 0.0f, 1.0f,
		-320.0f, 170.0f, 0.0f, 1.0f,

		-380.0f, 210.0f, 0.0f, 1.0f,
		-380.0f, 160.0f, 0.0f, 1.0f,
		-340.0f, 160.0f, 0.0f, 1.0f,
		-360.0f, 210.0f, 0.0f, 1.0f,
		-380.0f, 210.0f, 0.0f, 1.0f,

		-400.0f, 160.0f, 0.0f, 1.0f,
		-430.0f, 160.0f, 0.0f, 1.0f,
		-430.0f, 210.0f, 0.0f, 1.0f,
		-400.0f, 210.0f, 0.0f, 1.0f,
		-400.0f, 160.0f, 0.0f, 1.0f,

		// BLUE CAR
		-450.0f, -250.0f, 0.0f, 1.0f,
		-320.0f, -250.0f, 0.0f, 1.0f,
		-320.0f, -220.0f, 0.0f, 1.0f,
		-450.0f, -220.0f, 0.0f, 1.0f,
		-450.0f, -250.0f, 0.0f, 1.0f,

		-450.0f, -220.0f, 0.0f, 1.0f,
		-320.0f, -220.0f, 0.0f, 1.0f,
		-350.0f, -150.0f, 0.0f, 1.0f,
		-450.0f, -150.0f, 0.0f, 1.0f,
		-450.0f, -220.0f, 0.0f, 1.0f,

		-320.0f, -250.0f, 0.0f, 1.0f,
		-320.0f, -220.0f, 0.0f, 1.0f,
		-280.0f, -150.0f, 0.0f, 1.0f,
		-280.0f, -180.0f, 0.0f, 1.0f,
		-320.0f, -250.0f, 0.0f, 1.0f,

		-350.0f, -150.0f, 0.0f, 1.0f,
		-320.0f, -220.0f, 0.0f, 1.0f,
		-280.0f, -150.0f, 0.0f, 1.0f,
		-310.0f, -80.0f, 0.0f, 1.0f,
		-350.0f, -150.0f, 0.0f, 1.0f,

		-310.0f, -80.0f, 0.0f, 1.0f,
		-350.0f, -150.0f, 0.0f, 1.0f,
		-450.0f, -150.0f, 0.0f, 1.0f,
		-410.0f, -80.0f, 0.0f, 1.0f,
		-310.0f, -80.0f, 0.0f, 1.0f,

		-430.0f, -140.0f, 0.0f, 1.0f,
		-400.0f, -90.0f, 0.0f, 1.0f,
		-350.0f, -90.0f, 0.0f, 1.0f,
		-380.0f, -140.0f, 0.0f, 1.0f,
		-430.0f, -140.0f, 0.0f, 1.0f,

		-320.0f, -200.0f, 0.0f, 1.0f,
		-340.0f, -150.0f, 0.0f, 1.0f,
		-310.0f, -100.0f, 0.0f, 1.0f,
		-290.0f, -150.0f, 0.0f, 1.0f,
		-320.0f, -200.0f, 0.0f, 1.0f,

		-380.0f, -160.0f, 0.0f, 1.0f,
		-380.0f, -210.0f, 0.0f, 1.0f,
		-340.0f, -210.0f, 0.0f, 1.0f,
		-360.0f, -160.0f, 0.0f, 1.0f,
		-380.0f, -210.0f, 0.0f, 1.0f,

		-400.0f, -210.0f, 0.0f, 1.0f,
		-430.0f, -210.0f, 0.0f, 1.0f,
		-430.0f, -160.0f, 0.0f, 1.0f,
		-400.0f, -160.0f, 0.0f, 1.0f,
		-400.0f, -210.0f, 0.0f, 1.0f,
	};

	// culorile varfurilor din colturi
	GLfloat Colors[] = {
	  0.0f, 1.0f, 0.0f, 1.0f,
	  0.0f, 1.0f, 0.0f, 1.0f,
	  0.0f, 1.0f, 0.0f, 1.0f,
	  0.0f, 1.0f, 0.0f, 1.0f,

	  0.5f, 0.5f, 0.5f, 1.0f,
	  0.5f, 0.5f, 0.5f, 1.0f,
	  0.5f, 0.5f, 0.5f, 1.0f,
	  0.5f, 0.5f, 0.5f, 1.0f,

	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,

	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,

	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,

	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,

	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,

	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,

	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,

	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,
	  1.0f, 1.0f, 1.0f, 1.0f,

	  1.0f, 0.0f, 0.0f, 1.0f,
	  1.0f, 0.0f, 0.0f, 1.0f,
	  1.0f, 0.0f, 0.0f, 1.0f,
	  1.0f, 0.0f, 0.0f, 1.0f,

	  0.0f, 0.0f, 1.0f, 1.0f,
	  0.0f, 0.0f, 1.0f, 1.0f,
	  0.0f, 0.0f, 1.0f, 1.0f,
	  0.0f, 0.0f, 1.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,

	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
	  1.0f, 1.0f, 0.0f, 1.0f,
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
	ProgramId = LoadShaders("03_02_Shader.vert", "03_02_Shader.frag");
	glUseProgram(ProgramId);
}
void DestroyShaders(void)
{
	glDeleteProgram(ProgramId);
}

void Initialize(void)
{
	glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // culoarea de fond a ecranului
	CreateVBO();
	CreateShaders();
	codColLocation = glGetUniformLocation(ProgramId, "codCuloare");
	myMatrixLocation = glGetUniformLocation(ProgramId, "myMatrix");
}
void RenderFunction(void)
{
	glClear(GL_COLOR_BUFFER_BIT);

	// TO DO: schimbati transformarile (de exemplu deplasarea are loc pe axa Oy sau pe o alta dreapta)
	resizeMatrix = glm::ortho(-width, width, -height, height); // scalam, "aducem" scena la "patratul standard" [-1,1]x[-1,1]
	matrTransl = glm::translate(glm::mat4(1.0f), glm::vec3(i, k, 0.0)); // controleaza translatia de-a lungul lui Ox
	matrTransl3 = glm::translate(glm::mat4(1.0f), glm::vec3(0.0, h, 0.0));
	matrDepl = glm::translate(glm::mat4(1.0f), glm::vec3(1.0, 1.0, 0.0)); // plaseaza patratul rosu
	matrScale2 = glm::scale(glm::mat4(1.0f), glm::vec3(1.0, 1.0, 0.0)); // folosita la desenarea patratului rosu
	matrTransl2 = glm::translate(glm::mat4(1.0f), glm::vec3(j, l, 0.0));
	matrRot = glm::rotate(glm::mat4(1.0f), angle, glm::vec3(0.0, 0.0, 1.0)); // rotatie folosita la deplasarea patratului rosu

	// Matricea de redimensionare (pentru elementele "fixe") 
	myMatrix = resizeMatrix;
	// Culoarea
	codCol = 0;
	// Transmitere variabile uniforme
	glUniformMatrix4fv(myMatrixLocation, 1, GL_FALSE, &myMatrix[0][0]);
	glUniform1i(codColLocation, codCol);
	
	glDrawArrays(GL_POLYGON, 0, 4);
	glDrawArrays(GL_POLYGON, 4, 4);
	glDrawArrays(GL_POLYGON, 8, 4);
	glDrawArrays(GL_POLYGON, 12, 4);
	glDrawArrays(GL_POLYGON, 16, 4);
	glDrawArrays(GL_POLYGON, 20, 4);
	glDrawArrays(GL_POLYGON, 24, 4);
	glDrawArrays(GL_POLYGON, 28, 4);
	glDrawArrays(GL_POLYGON, 32, 4);
	glDrawArrays(GL_POLYGON, 36, 4);
	glDrawArrays(GL_POLYGON, 48, 4);
	glDrawArrays(GL_POLYGON, 52, 4);
	glDrawArrays(GL_POLYGON, 56, 4);
	glDrawArrays(GL_POLYGON, 60, 4);
	glDrawArrays(GL_POLYGON, 64, 4);
	glDrawArrays(GL_POLYGON, 68, 4);
	glDrawArrays(GL_POLYGON, 72, 4);
	glDrawArrays(GL_POLYGON, 76, 4);
	glDrawArrays(GL_POLYGON, 80, 4);
	glDrawArrays(GL_POLYGON, 84, 4);
	glDrawArrays(GL_POLYGON, 88, 4);
	glDrawArrays(GL_POLYGON, 92, 4);
	glDrawArrays(GL_POLYGON, 96, 4);
	glDrawArrays(GL_POLYGON, 100, 4);
	glDrawArrays(GL_POLYGON, 104, 4);
	glDrawArrays(GL_POLYGON, 108, 4);

	// Matricea pentru dreptunghiul rosu 
	myMatrix = resizeMatrix * matrTransl * matrDepl * matrScale2;
	// Culoarea
	codCol = 2;
	// Transmitere variabile uniforme
	glUniformMatrix4fv(myMatrixLocation, 1, GL_FALSE, &myMatrix[0][0]);
	glUniform1i(codColLocation, codCol);
	// Apelare DrawArrays
	glDrawArrays(GL_POLYGON, 200, 4);
	glDrawArrays(GL_POLYGON, 205, 4);
	glDrawArrays(GL_POLYGON, 210, 4);
	glDrawArrays(GL_POLYGON, 215, 4);
	glDrawArrays(GL_POLYGON, 220, 4);
	glDrawArrays(GL_POLYGON, 225, 4);
	glUniform1i(codColLocation, 4);
	glDrawArrays(GL_POLYGON, 230, 4);
	glDrawArrays(GL_POLYGON, 235, 4);
	glDrawArrays(GL_POLYGON, 240, 4);
	glDrawArrays(GL_POLYGON, 245, 4);
	glUniform1i(codColLocation, 3);
	glDrawArrays(GL_LINES, 202, 2);
	glDrawArrays(GL_LINES, 200, 2);
	glDrawArrays(GL_LINES, 203, 2);
	glDrawArrays(GL_LINES, 201, 2);
	glDrawArrays(GL_LINES, 205, 2);
	glDrawArrays(GL_LINES, 206, 2);
	glDrawArrays(GL_LINES, 207, 2);
	glDrawArrays(GL_LINES, 208, 2);
	glDrawArrays(GL_LINES, 210, 2);
	glDrawArrays(GL_LINES, 211, 2);
	glDrawArrays(GL_LINES, 212, 2);
	glDrawArrays(GL_LINES, 213, 2);
	glDrawArrays(GL_LINES, 215, 2);
	glDrawArrays(GL_LINES, 216, 2);
	glDrawArrays(GL_LINES, 217, 2);
	glDrawArrays(GL_LINES, 218, 2);
	glDrawArrays(GL_LINES, 220, 2);
	glDrawArrays(GL_LINES, 221, 2);
	glDrawArrays(GL_LINES, 222, 2);
	glDrawArrays(GL_LINES, 223, 2);
	glDrawArrays(GL_LINES, 224, 2);
	glDrawArrays(GL_LINES, 225, 2);
	glDrawArrays(GL_LINES, 226, 2);
	glDrawArrays(GL_LINES, 230, 2);
	glDrawArrays(GL_LINES, 231, 2);
	glDrawArrays(GL_LINES, 232, 2);
	glDrawArrays(GL_LINES, 233, 2);
	glDrawArrays(GL_LINES, 235, 2);
	glDrawArrays(GL_LINES, 236, 2);
	glDrawArrays(GL_LINES, 237, 2);
	glDrawArrays(GL_LINES, 238, 2);
	glDrawArrays(GL_LINES, 240, 2);
	glDrawArrays(GL_LINES, 241, 2);
	glDrawArrays(GL_LINES, 242, 2);
	glDrawArrays(GL_LINES, 243, 2);
	glDrawArrays(GL_LINES, 245, 2);
	glDrawArrays(GL_LINES, 246, 2);
	glDrawArrays(GL_LINES, 247, 2);
	glDrawArrays(GL_LINES, 248, 2);
	
	// Matricea pentru dreptunghiul rosu 
	myMatrix = resizeMatrix * matrTransl2 * matrDepl * matrScale2 * matrRot;
	// Culoarea
	codCol = 1;
	// Transmitere variabile uniforme
	glUniformMatrix4fv(myMatrixLocation, 1, GL_FALSE, &myMatrix[0][0]);
	glUniform1i(codColLocation, codCol);
	// Apelare DrawArrays
	glDrawArrays(GL_POLYGON, 250, 4);
	glDrawArrays(GL_POLYGON, 255, 4);
	glDrawArrays(GL_POLYGON, 260, 4);
	glDrawArrays(GL_POLYGON, 265, 4);
	glDrawArrays(GL_POLYGON, 270, 4);
	glUniform1i(codColLocation, 3);
	glDrawArrays(GL_LINES, 250, 2);
	glDrawArrays(GL_LINES, 251, 2);
	glDrawArrays(GL_LINES, 252, 2);
	glDrawArrays(GL_LINES, 253, 2);
	glDrawArrays(GL_LINES, 255, 2);
	glDrawArrays(GL_LINES, 256, 2);
	glDrawArrays(GL_LINES, 257, 2);
	glDrawArrays(GL_LINES, 258, 2);
	glDrawArrays(GL_LINES, 261, 2);
	glDrawArrays(GL_LINES, 262, 2);
	glDrawArrays(GL_LINES, 263, 2);
	glDrawArrays(GL_LINES, 265, 2);
	glDrawArrays(GL_LINES, 266, 2);
	glDrawArrays(GL_LINES, 267, 2);
	glDrawArrays(GL_LINES, 268, 2);
	glDrawArrays(GL_LINES, 270, 2);
	glDrawArrays(GL_LINES, 271, 2);
	glDrawArrays(GL_LINES, 272, 2);
	glDrawArrays(GL_LINES, 273, 2);
	glDrawArrays(GL_LINES, 275, 2);
	glDrawArrays(GL_LINES, 276, 2);
	glDrawArrays(GL_LINES, 277, 2);
	glDrawArrays(GL_LINES, 278, 2);
	glDrawArrays(GL_LINES, 280, 2);
	glDrawArrays(GL_LINES, 281, 2);
	glDrawArrays(GL_LINES, 282, 2);
	glDrawArrays(GL_LINES, 283, 2);
	glDrawArrays(GL_LINES, 285, 2);
	glDrawArrays(GL_LINES, 286, 2);
	glDrawArrays(GL_LINES, 287, 2);
	glDrawArrays(GL_LINES, 288, 2);
	glDrawArrays(GL_LINES, 290, 2);
	glDrawArrays(GL_LINES, 291, 2);
	glDrawArrays(GL_LINES, 292, 2);
	glDrawArrays(GL_LINES, 293, 2);
	glUniform1i(codColLocation, 4);
	glDrawArrays(GL_POLYGON, 275, 4);
	glDrawArrays(GL_POLYGON, 280, 4);
	glDrawArrays(GL_POLYGON, 285, 4);
	glDrawArrays(GL_POLYGON, 290, 4);

	myMatrix = resizeMatrix * matrTransl3;
	glUniformMatrix4fv(myMatrixLocation, 1, GL_FALSE, &myMatrix[0][0]);
	glUniform1i(codColLocation, codCol);
	glDrawArrays(GL_POLYGON, 112, 4);
	glDrawArrays(GL_POLYGON, 116, 4);
	glDrawArrays(GL_POLYGON, 120, 4);
	glDrawArrays(GL_POLYGON, 124, 4);
	glDrawArrays(GL_POLYGON, 128, 4);
	glDrawArrays(GL_POLYGON, 132, 4);
	glDrawArrays(GL_POLYGON, 136, 4);
	glDrawArrays(GL_POLYGON, 140, 4);
	glDrawArrays(GL_POLYGON, 144, 4);
	glDrawArrays(GL_POLYGON, 148, 4);
	glDrawArrays(GL_POLYGON, 152, 4);
	glDrawArrays(GL_POLYGON, 156, 4);
	glDrawArrays(GL_POLYGON, 160, 4);
	glDrawArrays(GL_POLYGON, 164, 4);
	glDrawArrays(GL_POLYGON, 168, 4);
	glDrawArrays(GL_POLYGON, 172, 4);
	glDrawArrays(GL_POLYGON, 176, 4);
	glDrawArrays(GL_POLYGON, 180, 4);
	glDrawArrays(GL_POLYGON, 184, 4);
	glDrawArrays(GL_POLYGON, 188, 4);
	glDrawArrays(GL_POLYGON, 192, 4);
	glDrawArrays(GL_POLYGON, 196, 4);
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
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(1000, 1000);
	glutCreateWindow("Proiect 1 - Depasire intre 2 dreptunghiuri");
	glewInit();
	Initialize();
	glutDisplayFunc(RenderFunction);
	glutMouseFunc(mouse);
	glutCloseFunc(Cleanup);
	glutMainLoop();
}