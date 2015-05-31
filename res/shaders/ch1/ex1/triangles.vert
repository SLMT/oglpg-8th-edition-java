#version 430 core

// Note that we use 'location = 0' to link data 
// from our buffer to the vertex shader
layout(location = 0) in vec4 vPosition;

void
main()
{
	// Copy input data to output data
	gl_Position = vPosition;
}