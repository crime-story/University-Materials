// Shader-ul de fragment / Fragment shader  
#version 330
 
in vec3 FragPos;  
in vec3 Normal; 
in vec3 inLightPos;
in vec3 inViewPos;
in vec3 dir;
in vec3 ex_Color; 
 
out vec4 out_Color;
 
uniform vec3 lightColor;
uniform int codCol; 
 
void main(void)
  {
    vec3 fogColor = vec3(0.8, 0.5, 0.5);

    float fogFactor = 0.002;
    float z = distance(inViewPos, FragPos);
    float f = exp(-fogFactor * z);

    if (codCol==0) // pentru codCol==0 este aplicata iluminarea
    {
  	// Ambient
    float ambientStrength = 0.2f;
    vec3 ambient = ambientStrength * lightColor;
  	
    // Diffuse 
    vec3 normala = normalize(Normal);
    vec3 lightDir = normalize(inLightPos - FragPos);
    //vec3 lightDir = normalize(dir); // cazul unei surse directionale
    float diff = max(dot(normala, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;
    
    // Specular
    float specularStrength = 0.5f;
    vec3 viewDir = normalize(inViewPos - FragPos);//vector catre observator normalizat (V)
    vec3 reflectDir = reflect(lightDir, normala); // reflexia razei de lumina (R)
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 1);
    vec3 specular = specularStrength * spec * lightColor;  
    vec3 emission=vec3(0.0, 0.0, 0.0);

    vec3 result = emission+(ambient + diffuse + specular) * ex_Color;

    vec3 fogResult = mix(fogColor, result, f);

	out_Color = vec4(fogResult, 1.0f);
    }

    if (codCol==1) // pentru codCol==1 este desenata umbra
    {
        vec3 black = vec3 (0.0, 0.0, 0.0);
        vec3 fogResult = mix(fogColor, black, f);
		out_Color = vec4 (fogResult, 1.0);
     }
  }
