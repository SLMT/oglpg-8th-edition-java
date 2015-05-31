package slmt.oglpg.ch1.ex1;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import slmt.oglpg.util.OpenGLTask;
import slmt.oglpg.util.ShaderInfo;
import slmt.oglpg.util.ShaderLoader;
import slmt.oglpg.util.Window;

public class Triangles implements OpenGLTask {

	// Shader files
	private static final String VERTEX_SHADER_FILE = "res/shaders/ch1/ex1/triangles.vert";
	private static final String FRAGMENT_SHADER_FILE = "res/shaders/ch1/ex1/triangles.frag";

	enum VaoIds {
		TRIANGLES
	};

	enum BufferIds {
		ARRAY_BUFFER
	};

	// The index of vPosition attribute in the vertex shader
	private static final int V_POSITION = 0;

	// Integer arrays in native memory
	IntBuffer vaos = BufferUtils.createIntBuffer(VaoIds.values().length);
	IntBuffer buffers = BufferUtils.createIntBuffer(BufferIds.values().length);

	final int NUM_VERTICES = 6;

	@Override
	public void init() {
		// Generate n names(ids) for vertex arrays and return them to the given
		// buffer
		GL30.glGenVertexArrays(vaos);
		// Bind the first name (Triangles) on a newly created vertex array
		GL30.glBindVertexArray(vaos.get(VaoIds.TRIANGLES.ordinal()));

		// Save the vertices of triangles in a native memory space
		FloatBuffer vertices = BufferUtils.createFloatBuffer(NUM_VERTICES * 2);
		vertices.put(new float[] {
				// Triangle 1
				-0.90f, -0.90f, +0.85f, -0.90f, -0.90f, +0.85f,
				// Triangle 2
				+0.90f, -0.85f, +0.90f, +0.90f, -0.85f, +0.90f });
		// The buffer must be rewinded to let LWJGL get the correct memory
		// address
		vertices.rewind();

		// Generate n names(ids) for buffers
		GL15.glGenBuffers(buffers);
		// Specify the current active buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,
				buffers.get(BufferIds.ARRAY_BUFFER.ordinal()));
		// Allocate a memory space for the active buffer in the OpenGL server,
		// the copy the data from the given array to it
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

		ShaderInfo shaders[] = {
				new ShaderInfo(GL20.GL_VERTEX_SHADER, VERTEX_SHADER_FILE),
				new ShaderInfo(GL20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_FILE) };
		// Use our helper function to load the necessary shader files
		int program = ShaderLoader.loadShaders(shaders);
		GL20.glUseProgram(program);
		
		// Link the current active buffer to the specified attribute in the shader
		GL20.glVertexAttribPointer(V_POSITION, 2, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(V_POSITION);
		
		// Change the color for cleaning frame buffer
		// GL11.glClearColor(0.82f, 0.83f, 0.82f, 1.0f);
	}

	@Override
	public void display() {
		// Clean the frame buffer for drawing
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		// Draw triangles
		GL30.glBindVertexArray(vaos.get(VaoIds.TRIANGLES.ordinal()));
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, NUM_VERTICES);

		// No need for this since we will swap buffer after calling display()
		// GL11.glFlush();
	}

	public static void main(String[] args) {
		new Window(new Triangles(), 512, 512).loop();
	}
}
