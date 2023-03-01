resizeMatrix = glm::ortho(-width, width, -height, height); // scalam, "aducem" scena la "patratul standard" [-1,1]x[-1,1]
matrTransl = glm::translate(glm::mat4(1.0f), glm::vec3(i, k, 0.0)); // controleaza translatia de-a lungul lui Ox
matrTransl3 = glm::translate(glm::mat4(1.0f), glm::vec3(0.0, h, 0.0));
matrDepl = glm::translate(glm::mat4(1.0f), glm::vec3(1.0, 1.0, 0.0)); // plaseaza patratul rosu
matrScale2 = glm::scale(glm::mat4(1.0f), glm::vec3(1.0, 1.0, 0.0)); // folosita la desenarea patratului rosu
matrTransl2 = glm::translate(glm::mat4(1.0f), glm::vec3(j, l, 0.0));
matrRot = glm::rotate(glm::mat4(1.0f), angle, glm::vec3(0.0, 0.0, 1.0)); // rotatie folosita la deplasarea patratului rosu