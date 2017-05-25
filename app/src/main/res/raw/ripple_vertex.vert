attribute vec4 position;
attribute vec2 texcoord;
varying vec2 texcoordVarying;

void main() {
    gl_Position = position;
    texcoordVarying = texcoord;
}