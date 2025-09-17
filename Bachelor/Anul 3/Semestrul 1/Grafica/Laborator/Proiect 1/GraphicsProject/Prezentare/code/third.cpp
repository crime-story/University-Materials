	// Matricea pentru dreptunghiul rosu 
	myMatrix = resizeMatrix * matrTransl2 * matrDepl * matrScale2 * matrRot;
	// Culoarea
	codCol = 1;
	// Transmitere variabile uniforme
	glUniformMatrix4fv(myMatrixLocation, 1, GL_FALSE, &myMatrix[0][0]);
	glUniform1i(codColLocation, codCol);