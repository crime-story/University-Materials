 
// Shader-ul de fragment / Fragment shader  
 
 #version 400
 
in vec3 FragPos;  
in vec3 Normal; 
in vec3 inLightPos;
in vec3 inViewPos;
in vec3 ex_Color; 
 
out vec4 out_Color;
 
uniform vec3 lightColor;
uniform int codCol; 
 
void main(void)
  {
  	// Ambient
    float ambientStrength = 0.2f;
    vec3 ambient = ambientStrength * lightColor;
  	
    // Diffuse 
    vec3 normala = normalize(Normal);
     vec3 lightDir = normalize(inLightPos - FragPos);
    // vec3 dir=vec3(0.0,-150.0,200.0); // sursa directionala
    // vec3 lightDir=normalize(dir);
    float diff = max(dot(normala, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;
    
    // Specular
    float specularStrength = 0.5f;
    vec3 viewDir = normalize(inViewPos - FragPos);//vector catre observator normalizat (V)
    vec3 reflectDir = reflect(-lightDir, normala); // reflexia razei de lumina (R)
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 1);
    vec3 specular = specularStrength * spec * lightColor;  
    vec3 emission=vec3(0.0, 0.0, 0.0);
    vec3 result = emission+(ambient + diffuse + specular) * ex_Color;
	//out_Color = vec4(result, 1.0f);

    // Efect de ceata
    vec3 fogColor = vec3(0.5, 0.5, 0.5);
    float dist=length(inViewPos - FragPos);
    // float fogFactor=exp(-0.002*dist); // intre 0 si 1; 1 corespunde aproape de obiect
    float fogFactor=1.0;
    out_Color = vec4(mix(fogColor,result,fogFactor), 1.0f);
    
    if ( codCol==1 )
		out_Color=vec4 (0.0, 0.0, 0.0, 0.0);
  }
 