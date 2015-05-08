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

	enum VaoIds { TRIANGLES };
	enum BufferIds { ARRAY_BUFFER };
	enum AttribIds { V_POSITION };
	
	// Integer arrays in native memory
	IntBuffer vaos = BufferUtils.createIntBuffer(VaoIds.values().length);
	IntBuffer buffers = BufferUtils.createIntBuffer(BufferIds.values().length);
	
	final int NUM_VERTICES = 6;
	
	public void init() {
		GL30.glGenVertexArrays(vaos);
		GL30.glBindVertexArray(vaos.get(VaoIds.TRIANGLES.ordinal()));
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(NUM_VERTICES * 2);
		vertices.put(new float[]{
			// Triangle 1
			-0.90f, -0.90f,
			+0.85f, -0.90f,
			-0.90f, +0.85f,
			// Triangle 2
			+0.90f, -0.85f,
			+0.90f, +0.90f,
			-0.85f, +0.90f
		});
		vertices.rewind(); // buffer must be rewind to let LWJGL get the correct memory address
		
		GL15.glGenBuffers(buffers);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffers.get(BufferIds.ARRAY_BUFFER.ordinal()));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		
		ShaderInfo shaders[] = {
				new ShaderInfo(GL20.GL_VERTEX_SHADER, VERTEX_SHADER_FILE),
				new ShaderInfo(GL20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_FILE)
		};
		
		int program = ShaderLoader.loadShaders(shaders);
		GL20.glUseProgram(program);
		
		GL20.glVertexAttribPointer(AttribIds.V_POSITION.ordinal(),
				2, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(AttribIds.V_POSITION.ordinal());
	}
	
	public void display() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL30.glBindVertexArray(vaos.get(VaoIds.TRIANGLES.ordinal()));
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, NUM_VERTICES);
		
		// No need for this since we will swap buffer after calling display()
		// GL11.glFlush();
	}
	
	public static void main(String[] args) {
		new Window(new Triangles()).loop();
	}
}
