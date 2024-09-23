#version 410 core

in vec3 vertPosition;
in vec4 vertColor;
in vec3 vertNormal;

layout (location = 0) out vec4 fragColor;

uniform mat4 view;

uniform vec3 lightColor;
uniform float ambientLight;
uniform vec3 directionalLight;

void main() {
  vec3 normal = normalize(vertNormal);
  vec3 lightDirection = normalize(vec3(view * vec4(directionalLight, 1.0)) - vertPosition);

  vec3 ambient = ambientLight * lightColor;

  vec3 diffuse = clamp(dot(normal, lightDirection), 0.0, 1.0) * lightColor;

  vec3 viewDirection = normalize(-vertPosition);
  vec3 reflectDirection = reflect(-lightDirection, normal);
  vec3 specular = pow(clamp(dot(viewDirection, reflectDirection), 0.0, 1.0), 32) * lightColor;

  fragColor = vec4((ambient + diffuse + specular) * vertColor.rgb, vertColor.a);
}
