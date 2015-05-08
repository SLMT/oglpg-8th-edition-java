package slmt.oglpg.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class ShaderLoader {

	public static int loadShaders(ShaderInfo shaders[]) {
		if (shaders == null)
			return 0;

		int program = GL20.glCreateProgram();

		for (ShaderInfo entry : shaders) {
			int shader = GL20.glCreateShader(entry.type);

			entry.shader = shader;

			String source = readShader(entry.filename);
			if (source == null) {
				// Delete all shaders
				for (ShaderInfo en : shaders) {
					GL20.glDeleteShader(en.shader);
					en.shader = 0;
				}
				
				return 0;
			}

			GL20.glShaderSource(shader, source);
			GL20.glCompileShader(shader);
			
			IntBuffer compiled = BufferUtils.createIntBuffer(1);
			GL20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, compiled);
			if (compiled.get(0) == 0) {
				String log = GL20.glGetShaderInfoLog(shader);
				System.err.println(log);
				return 0;
			}
			
			GL20.glAttachShader(program, shader);
		}
		
		GL20.glLinkProgram(program);
		
		IntBuffer linked = BufferUtils.createIntBuffer(1);
		GL20.glGetProgramiv(program, GL20.GL_LINK_STATUS, linked);
		if (linked.get(0) == 0) {
			String log = GL20.glGetProgramInfoLog(program);
			System.err.println(log);
			
			// Delete all shaders
			for (ShaderInfo en : shaders) {
				GL20.glDeleteShader(en.shader);
				en.shader = 0;
			}
			
			return 0;
		}
		
		return program;
	}

	private static String readShader(String filename) {
		BufferedReader reader = null;

		try {
			// Open the file
			reader = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the file
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
