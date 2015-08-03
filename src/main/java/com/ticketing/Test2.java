package com.ticketing;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.net.*;
import java.nio.file.*;
import java.io.*;


import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.NullValue;
import com.caucho.quercus.env.JavaValue;
import com.caucho.quercus.env.Value;
import com.caucho.quercus.page.InterpretedPage;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.parser.QuercusParser;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.quercus.program.JavaClassDef;
import com.caucho.quercus.QuercusContext;
import com.caucho.vfs.Path;
import com.caucho.vfs.MergePath;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StdoutStream;
import com.caucho.vfs.StreamImpl;
import com.caucho.vfs.WriteStream;


public class Test2
{
	public static void main(String[] args) {
		try{
			String filename = "test3.php";
			QuercusContext quercusContext = new QuercusContext();
			quercusContext.init();
			ClassLoader classLoader = Test2.class.getClassLoader();
			File file = new File(classLoader.getResource("test3.php").getFile());
			Reader oReader = new FileReader(file);

			QuercusProgram program = QuercusParser.parse(quercusContext, null, oReader);
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			OutputStreamStream s = new OutputStreamStream(os);
			WriteStream out = new WriteStream(s);

			out.setNewlineString("\n");

			try {
				out.setEncoding("iso-8859-1");
			} catch (Exception e) {
			}
			QuercusPage page = new InterpretedPage(program);

			Env env = new Env(quercusContext, page, out, null, null);

			Map<String, Object> attributes = new HashMap<>();
			attributes.put("message", "Hello World!");
			attributes.put("filename", "views/test.php");
			attributes.put("basedir", quercusContext.getPwd() + "/");

			JavaClassDef classDef = env.getJavaClassDefinition(attributes.getClass());
			env.setGlobalValue("attributes", new JavaValue(env, attributes, classDef));

			Value value = NullValue.NULL;
			try{
				value = program.execute(env);

			}catch(Exception e){
				e.printStackTrace();
			}
			out.flushBuffer();
			out.free();

			os.flush();

			System.out.println(os.toString("UTF-8"));

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
