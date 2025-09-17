#include <glad/glad.h>
#include <glad/glad.h>
#include <GLFW/glfw3.h>

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

#include "shader.h"
#include "stb_image.h"

using namespace std;

void framebuffer_size_callback(GLFWwindow* window, int width, int height);
void processInput(GLFWwindow* window);

unsigned int loadTexture(const char* path);

const unsigned int WIDTH = 1280;
const unsigned int HEIGHT = 720;

// camera
glm::vec3 cameraPosition;
glm::quat cameraOrientation = glm::quat(glm::lookAt(glm::vec3(), glm::vec3(0, 0, 1), glm::vec3(0, 1, 0)));

float delta = 0.0f;
float lastFrame = 0.0f;

float matrUmbra[4][4];

glm::vec3 lumina;

int main()
{
    // glfw: initialize and configure
    // ------------------------------
    glfwInit();
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    // glfw window creation
    // --------------------
    //glfwWindowHint(GLFW_SAMPLES, 16);
    GLFWwindow* window = glfwCreateWindow(WIDTH, HEIGHT, "ProiectGrafica2", NULL, NULL);
    if (window == NULL)
    {
        std::cout << "Failed to create GLFW window" << std::endl;
        glfwTerminate();
        return -1;
    }
    glfwMakeContextCurrent(window);

    // glad: load all OpenGL function pointers
    // ---------------------------------------
    if (!gladLoadGLLoader((GLADloadproc)glfwGetProcAddress))
    {
        std::cout << "Failed to initialize GLAD" << std::endl;
        return -1;
    }
    
    glm::mat4 projection = glm::infinitePerspective(glm::radians(50.f), (float)WIDTH / (float)HEIGHT, 0.1f);

    Shader sh_perete("perete.vert", "perete.frag");
    Shader sh_cilindru("cilindru.vert", "cilindru.frag");

#pragma region cilindru
#define N 30
#define BASE_OFFSET (N * 3 * 2)
#define BASE_OFFSET2 (BASE_OFFSET * 2)
    glm::vec3 vertices[(N * 3 * 2 + N * 6) * 2];
    float deltaAngle = glm::radians(360.f) / N;

    for (int i = 0; i < N; i++) {
        float currentDelta = (i)*deltaAngle;
        float nextDelta = (i + 1) * deltaAngle;

        vertices[6 * i + 0] = glm::vec3(glm::cos(currentDelta), -1.f, glm::sin(currentDelta));
        vertices[6 * i + 1] = glm::vec3(0, -1.f, 0);
        vertices[6 * i + 2] = glm::vec3(glm::cos(nextDelta), -1.f, glm::sin(nextDelta));
        vertices[6 * i + 3] = glm::vec3(0, -1.f, 0);
        vertices[6 * i + 4] = glm::vec3(0, -1.f, 0);
        vertices[6 * i + 5] = glm::vec3(0, -1.f, 0);

        vertices[BASE_OFFSET + 6 * i + 0] = glm::vec3(glm::cos(currentDelta), 1.f, glm::sin(currentDelta));
        vertices[BASE_OFFSET + 6 * i + 1] = glm::vec3(0, 1.f, 0);
        vertices[BASE_OFFSET + 6 * i + 2] = glm::vec3(glm::cos(nextDelta), 1.f, glm::sin(nextDelta));
        vertices[BASE_OFFSET + 6 * i + 3] = glm::vec3(0, 1.f, 0);
        vertices[BASE_OFFSET + 6 * i + 4] = glm::vec3(0, 1.f, 0);
        vertices[BASE_OFFSET + 6 * i + 5] = glm::vec3(0, 1.f, 0);

        vertices[BASE_OFFSET2 + 12 * i + 0] = glm::vec3(glm::cos(currentDelta), -1.f, glm::sin(currentDelta));
        vertices[BASE_OFFSET2 + 12 * i + 1] = glm::vec3(glm::cos(currentDelta), 0, glm::sin(currentDelta));
        vertices[BASE_OFFSET2 + 12 * i + 2] = glm::vec3(glm::cos(nextDelta), -1.f, glm::sin(nextDelta));
        vertices[BASE_OFFSET2 + 12 * i + 3] = glm::vec3(glm::cos(nextDelta), 0, glm::sin(nextDelta));
        vertices[BASE_OFFSET2 + 12 * i + 4] = glm::vec3(glm::cos(currentDelta), 1.f, glm::sin(currentDelta));
        vertices[BASE_OFFSET2 + 12 * i + 5] = glm::vec3(glm::cos(currentDelta), 0, glm::sin(currentDelta));

        vertices[BASE_OFFSET2 + 12 * i + 6] = glm::vec3(glm::cos(currentDelta), 1.f, glm::sin(currentDelta));
        vertices[BASE_OFFSET2 + 12 * i + 7] = glm::vec3(glm::cos(currentDelta), 0, glm::sin(currentDelta));
        vertices[BASE_OFFSET2 + 12 * i + 8] = glm::vec3(glm::cos(nextDelta), 1.f, glm::sin(nextDelta));
        vertices[BASE_OFFSET2 + 12 * i + 9] = glm::vec3(glm::cos(nextDelta), 0, glm::sin(nextDelta));
        vertices[BASE_OFFSET2 + 12 * i + 10] = glm::vec3(glm::cos(nextDelta), -1.f, glm::sin(nextDelta));
        vertices[BASE_OFFSET2 + 12 * i + 11] = glm::vec3(glm::cos(nextDelta), 0, glm::sin(nextDelta));
    }

    unsigned int vbo_cilindru, vao_cilindru;
    glGenBuffers(1, &vbo_cilindru);
    glGenVertexArrays(1, &vao_cilindru);
    glBindVertexArray(vao_cilindru);
    glBindBuffer(GL_ARRAY_BUFFER, vbo_cilindru);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(float), (GLvoid*)(3 * sizeof(GLfloat)));
    glEnableVertexAttribArray(1);
#pragma endregion

#pragma region perete
    unsigned int textura_perete_diffuse = loadTexture("diffuse.jpg");
    unsigned int textura_perete_normal = loadTexture("normal.jpg");
    unsigned int textura_perete_specular = loadTexture("specular.jpg");

    float dim_textura = 5.f;
    GLfloat v[] = {
        // pozitie              normale             tex_coords                                  tangenta
        -0.5f, 0.5f, 0.0f,     0, 0, 1,          0.0f, 1.0f * dim_textura,                      1, 0, 0,
        -0.5f, -0.5f, 0.0f,    0, 0, 1,          0.0f, 0.0f,                                    1, 0, 0,
        0.5f, 0.5f, 0.0f,      0, 0, 1,          1.0f * dim_textura, 1.0f * dim_textura,        1, 0, 0,

        -0.5f, -0.5f, 0.0f,    0, 0, 1,          0.0f, 0.0f,                                    1, 0, 0,
        0.5f, -0.5f, 0.0f,     0, 0, 1,          1.0f * dim_textura, 0.0f,                      1, 0, 0,
        0.5f, 0.5f, 0.0f,      0, 0, 1,          1.0f * dim_textura, 1.0f * dim_textura,        1, 0, 0,
    };

    unsigned int vbo_perete, vao_perete;
    glGenBuffers(1, &vbo_perete);
    glGenVertexArrays(1, &vao_perete);
    glBindVertexArray(vao_perete);
    glBindBuffer(GL_ARRAY_BUFFER, vbo_perete);
    glBufferData(GL_ARRAY_BUFFER, sizeof(v), &v, GL_STATIC_DRAW);

    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 11 * sizeof(GLfloat), (void*)0);
    glEnableVertexAttribArray(1);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 11 * sizeof(GLfloat), (void*)(3 * sizeof(GLfloat)));
    glEnableVertexAttribArray(2);
    glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, 11 * sizeof(GLfloat), (void*)(6 * sizeof(GLfloat)));
    glEnableVertexAttribArray(3);
    glVertexAttribPointer(3, 3, GL_FLOAT, GL_FALSE, 11 * sizeof(GLfloat), (void*)(8 * sizeof(GLfloat)));

    // texturi perete
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, textura_perete_diffuse);
    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, textura_perete_normal);
    glActiveTexture(GL_TEXTURE2);
    glBindTexture(GL_TEXTURE_2D, textura_perete_specular);
    
    sh_perete.use();
    sh_perete.setInt("tex_diffuse", 0);
    sh_perete.setInt("tex_normal", 1);
    sh_perete.setInt("tex_specular", 2);
#pragma endregion
    
#pragma region path
    float startZ = -50.f;
    const int nPoints = 100;
    const float deltaZ = 100.f / nPoints;
    glm::vec3 points[nPoints];
    float angle = 0.f;
    const float pi = 3.141592653589f;
    for (int i = 0; i < nPoints; i++) {
        angle += pi / 16.f;
        points[i] = glm::vec3(glm::cos(angle) * 4.f, glm::sin(angle) * 4.f, startZ += deltaZ);
    }
#pragma endregion

    // definim cilindrii
    float h = -4.f;
    pair<glm::vec3, pair<glm::vec3, glm::vec3>> cilindrii[10] = {
        { glm::vec3(225, 193, 110), { glm::vec3(4, h, -40), glm::vec3(0, 0, 90) } },
        { glm::vec3(205, 127, 50), { glm::vec3(3, h, -30), glm::vec3() } },
        { glm::vec3(165, 42, 42), { glm::vec3(4, h, -20), glm::vec3(90, 0, 0) } },
        { glm::vec3(218, 160, 109), { glm::vec3(-2, h, -15), glm::vec3(90, 45, 0) } },
        { glm::vec3(128, 0, 32), { glm::vec3(1, h, -10), glm::vec3() } },
        { glm::vec3(233, 116, 81), { glm::vec3(-4, h, 10), glm::vec3(0, 135, 90) } },
        { glm::vec3(110, 38, 14), { glm::vec3(-2, h, 30), glm::vec3(0, 0, 90) } },
        { glm::vec3(193, 154, 107), { glm::vec3(-1, h, 35), glm::vec3() } },
        { glm::vec3(149, 69, 53), { glm::vec3(2, h, 40), glm::vec3() } },
        { glm::vec3(123, 63, 0), { glm::vec3(0, h, 45), glm::vec3(90, 0, 0) } },
    };

    // planul pe care aplicam umbrele
    glm::vec4 plane_umbra[3] = {
        { 0, 1, 0, 4.99f },
        { 1, 0, 0, 4.99f },
        { -1, 0, 0, 4.99f },
    };

    int punctCurent = 0;
    int nextPunct;
    float progres = 0.f;

    // curatam ecranul cu negru
    float culoare_fundal = .3f;
    glClearColor(culoare_fundal, culoare_fundal, culoare_fundal, 1);
    glEnable(GL_DEPTH_TEST);
    //glEnable(GL_CULL_FACE);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    while (!glfwWindowShouldClose(window))
    {
        float currentFrame = static_cast<float>(glfwGetTime());
        delta = currentFrame - lastFrame;
        lastFrame = currentFrame;
        processInput(window);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // misca camera
        progres += delta * 5.f;
        if (progres >= 1.f) {
            progres -= 1.f;
            punctCurent = min(punctCurent + 1, nPoints - 1);
        }
        nextPunct = min(punctCurent + 1, nPoints - 1);
        glm::vec3 pozCurenta = glm::mix(points[punctCurent], points[nextPunct], progres);
        cameraPosition = pozCurenta;

        lumina = cameraPosition + glm::vec3(0, 0, 10.f);
        // setam matricile de vizualizare si pozitia camerei
        glm::mat4 viewMatrix;
        glm::mat4 rotationMatrix = glm::mat4_cast(cameraOrientation);
        glm::mat4 translationMatrix = glm::translate(glm::mat4(1.0f), -cameraPosition);
        viewMatrix = rotationMatrix * translationMatrix;

        sh_perete.use();
        sh_perete.setMat4("view", viewMatrix);
        sh_perete.setMat4("projection", projection);
        sh_perete.setVec3("camera_pos", cameraPosition);
        sh_perete.setVec3("pozitie_lumina", lumina);

        sh_cilindru.use();
        sh_cilindru.setMat4("view", viewMatrix);
        sh_cilindru.setMat4("projection", projection);
        sh_cilindru.setVec3("camera_pos", cameraPosition);
        sh_cilindru.setVec3("pozitie_lumina", lumina);

#pragma region randeaza pereti
        static float dist = 5.f;
        static pair<glm::vec3, glm::vec3> pereti[] = {
            { glm::vec3(-dist, 0, 0), glm::vec3(0, 90, 0) },
            { glm::vec3(dist, 0, 0), glm::vec3(0, -90, 0) },
            { glm::vec3(0, dist, 0), glm::vec3(90, 90, 0) },
            { glm::vec3(0, -dist, 0), glm::vec3(-90, -90, 0) },
            { glm::vec3(0, 0, -50), glm::vec3(0, 0, 0) },
            { glm::vec3(0, 0, 50), glm::vec3(0, 180, 0) },
        };

        sh_perete.use();
        for (int i = 0; i < 6; i++) {
            glm::mat4 model;
            model = glm::translate(model, pereti[i].first);
            model = glm::rotate(model, glm::radians(pereti[i].second.z), glm::vec3(0,0,1));
            model = glm::rotate(model, glm::radians(pereti[i].second.y), glm::vec3(0,1,0));
            model = glm::rotate(model, glm::radians(pereti[i].second.x), glm::vec3(1,0,0));
            if (i < 4) {
                model = glm::scale(model, glm::vec3(100.f, 10.f, 1.f));
                sh_perete.setFloat("texRatio", 10.f);
            }
            else {
                model = glm::scale(model, glm::vec3(10.f, 10.f, 1.f));
                sh_perete.setFloat("texRatio", 1.f);
            }
            sh_perete.setMat4("model", model);

            glBindVertexArray(vao_perete);
            glDrawArrays(GL_TRIANGLES, 0, 6);
        }
#pragma endregion

#pragma region randeaza cilindrii
        sh_cilindru.use();
        sh_cilindru.setInt("codCol", 0);
        for (int i = 0; i < 10; i++) {
            glm::mat4 model;
            model = glm::translate(model, cilindrii[i].second.first);
            model = glm::rotate(model, glm::radians(cilindrii[i].second.second.z), glm::vec3(0, 0, 1));
            model = glm::rotate(model, glm::radians(cilindrii[i].second.second.y), glm::vec3(0, 1, 0));
            model = glm::rotate(model, glm::radians(cilindrii[i].second.second.x), glm::vec3(1, 0, 0));

            sh_cilindru.setMat4("model", model);
            sh_cilindru.setVec3("uColor", glm::vec3(cilindrii[i].first.x / 255.f, cilindrii[i].first.y / 255.f, cilindrii[i].first.z / 255.f));

            glBindVertexArray(vao_cilindru);
            glDrawArrays(GL_TRIANGLES, 0, N * 12);
        }
#pragma endregion
        
#pragma region umbre
        sh_cilindru.use();
        sh_cilindru.setInt("codCol", 1);
        for (int i = 0; i < 3; i++) {
            glm::mat4 matrUmbra;
            matrUmbra[0][0] = plane_umbra[i].y * lumina.y + plane_umbra[i].z * lumina.z + plane_umbra[i].w;
            matrUmbra[0][1] = -plane_umbra[i].y * lumina.x;
            matrUmbra[0][2] = -plane_umbra[i].z * lumina.x;
            matrUmbra[0][3] = -plane_umbra[i].w * lumina.x;
            matrUmbra[1][0] = -plane_umbra[i].x * lumina.y;
            matrUmbra[1][1] = plane_umbra[i].x * lumina.x + plane_umbra[i].z * lumina.z + plane_umbra[i].w;
            matrUmbra[1][2] = -plane_umbra[i].z * lumina.y;
            matrUmbra[1][3] = -plane_umbra[i].w * lumina.y;
            matrUmbra[2][0] = -plane_umbra[i].x * lumina.z;
            matrUmbra[2][1] = -plane_umbra[i].y * lumina.z;
            matrUmbra[2][2] = plane_umbra[i].x * lumina.x + plane_umbra[i].y * lumina.y + plane_umbra[i].w;
            matrUmbra[2][3] = -plane_umbra[i].w * lumina.z;
            matrUmbra[3][0] = -plane_umbra[i].x;
            matrUmbra[3][1] = -plane_umbra[i].y;
            matrUmbra[3][2] = -plane_umbra[i].z;
            matrUmbra[3][3] = plane_umbra[i].x * lumina.x + plane_umbra[i].y * lumina.y + plane_umbra[i].z * lumina.z;

            matrUmbra = glm::transpose(matrUmbra);
            sh_cilindru.setMat4("matrUmbra", &matrUmbra[0][0]);

            for (int i = 0; i < 10; i++) {
                glm::mat4 model;
                model = glm::translate(model, cilindrii[i].second.first);
                model = glm::rotate(model, glm::radians(cilindrii[i].second.second.z), glm::vec3(0, 0, 1));
                model = glm::rotate(model, glm::radians(cilindrii[i].second.second.y), glm::vec3(0, 1, 0));
                model = glm::rotate(model, glm::radians(cilindrii[i].second.second.x), glm::vec3(1, 0, 0));

                sh_cilindru.setMat4("model", model);
                sh_cilindru.setVec3("uColor", glm::vec3(cilindrii[i].first.x / 255.f, cilindrii[i].first.y / 255.f, cilindrii[i].first.z / 255.f));

                glBindVertexArray(vao_cilindru);
                glDrawArrays(GL_TRIANGLES, 0, N * 12);
            }
        }
        
#pragma endregion
        
        
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    glfwTerminate();
    return 0;
}

void processInput(GLFWwindow* window)
{
    if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
        glfwSetWindowShouldClose(window, true);
}

void framebuffer_size_callback(GLFWwindow* window, int width, int height)
{
    glViewport(0, 0, width, height);
}

// functie standard de incarcat texturi
unsigned int loadTexture(char const* path)
{
    unsigned int textureID;
    glGenTextures(1, &textureID);

    int width, height, nrComponents;
    unsigned char* data = stbi_load(path, &width, &height, &nrComponents, 0);
    if (data)
    {
        GLenum format;
        if (nrComponents == 1)
            format = GL_RED;
        else if (nrComponents == 3)
            format = GL_RGB;
        else if (nrComponents == 4)
            format = GL_RGBA;

        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, format == GL_RGBA ? GL_CLAMP_TO_EDGE : GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, format == GL_RGBA ? GL_CLAMP_TO_EDGE : GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        stbi_image_free(data);
    }
    else
    {
        std::cout << "Texture failed to load at path: " << path << std::endl;
        stbi_image_free(data);
    }

    return textureID;
}