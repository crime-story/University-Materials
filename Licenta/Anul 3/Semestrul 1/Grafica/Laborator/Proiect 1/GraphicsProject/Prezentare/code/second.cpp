	// Matricea pentru dreptunghiul rosu 
	myMatrix = resizeMatrix * matrTransl * matrDepl * matrScale2;
	// Culoarea
	codCol = 2;
	// Transmitere variabile uniforme
	glUniformMatrix4fv(myMatrixLocation, 1, GL_FALSE, &myMatrix[0][0]);
	glUniform1i(codColLocation, codCol);