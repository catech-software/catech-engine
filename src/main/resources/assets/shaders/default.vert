#version 410 core

in vec3 position;
in vec4 color;
in vec3 normal;

out vec3 vertPosition;
out vec4 vertColor;
out vec3 vertNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
  gl_Position = projection * view * model * vec4(position, 1.0);
  vertPosition = vec3(view * model * vec4(position, 1.0));
  vertColor = color;
  vertNormal = mat3(transpose(inverse(view * model))) * normal;
}
