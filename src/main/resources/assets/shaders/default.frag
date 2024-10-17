#version 410 core

in vec3 vertPosition;
in vec4 vertColor;
in vec2 vertTexCoord;
in vec3 vertNormal;

layout (location = 0) out vec4 fragColor;

uniform vec3 viewPosition;
uniform vec3 lightColor;
uniform float ambientLight;
uniform vec3 directionalLight;

uniform sampler2D baseColorTex;

void main() {
  vec4 color = mix(vertColor, texture(baseColorTex, vertTexCoord), texture(baseColorTex, vertTexCoord).a);

  vec3 normal = normalize(vertNormal);
  vec3 lightDirection = normalize(-directionalLight);

  vec3 ambient = ambientLight * lightColor;

  vec3 diffuse = clamp(dot(normal, lightDirection), 0.0, 1.0) * lightColor;

  vec3 viewDirection = normalize(viewPosition - vertPosition);
  vec3 reflectDirection = reflect(-lightDirection, normal);
  vec3 specular = pow(clamp(dot(viewDirection, reflectDirection), 0.0, 1.0), 32) * lightColor;

  fragColor = vec4((ambient + diffuse + specular) * color.rgb, color.a);
}
