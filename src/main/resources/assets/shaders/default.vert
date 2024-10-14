#version 410 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texCoord;
layout(location = 3) in vec3 normal;

out vec3 vertPosition;
out vec4 vertColor;
out vec2 vertTexCoord;
out vec3 vertNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
  gl_Position = projection * view * model * vec4(position, 1.0);
  vertPosition = vec3(view * model * vec4(position, 1.0));
  vertColor = color;
  vertTexCoord = texCoord;
  vertNormal = mat3(transpose(inverse(model))) * normal;
}
