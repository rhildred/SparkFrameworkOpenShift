package com.ticketing;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.NullValue;
import com.caucho.quercus.env.JavaValue;
import com.caucho.quercus.env.Value;
import com.caucho.quercus.env.ConstStringValue;
import com.caucho.quercus.page.InterpretedPage;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.parser.QuercusParser;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.quercus.program.JavaClassDef;
import com.caucho.quercus.QuercusContext;
import com.caucho.vfs.Path;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StdoutStream;
import com.caucho.vfs.StreamImpl;
import com.caucho.vfs.StringPath;
import com.caucho.vfs.WriteStream;

public class PHPRenderer{
    protected QuercusContext _quercus;
    public PHPRenderer(){
        _quercus = new QuercusContext();
        _quercus.init();
    }
    public String render(String sFile, String sPage){
        return this.render(sFile, sPage, null);
    }
    public String render(String sFile, String sPage, Object oJava){
        String rc = "";
        try{
            ByteArrayOutputStream os = new ByteArrayOutputStream();
			ClassLoader classLoader = Test2.class.getClassLoader();
			File file = new File(classLoader.getResource("viewWrapper.php").getFile());
			Reader oReader = new FileReader(file);
            QuercusProgram program = QuercusParser.parse(_quercus, null, oReader);

			OutputStreamStream s = new OutputStreamStream(os);
			WriteStream out = new WriteStream(s);

			out.setNewlineString("\n");

			try {
				out.setEncoding("iso-8859-1");
			} catch (Exception e) {
			}
            QuercusPage page = new InterpretedPage(program);

            Env env = new Env(_quercus, page, out, null, null); 
            if(oJava != null){
                JavaClassDef classDef = env.getJavaClassDefinition(oJava.getClass());
                env.setGlobalValue("model", new JavaValue(env, oJava, classDef));
            }
            env.setGlobalValue("filename", new ConstStringValue(sFile));
            env.setGlobalValue("sPage", new ConstStringValue(sPage));
            env.setGlobalValue("basedir", new ConstStringValue(_quercus.getPwd() + "/"));

			Value value = NullValue.NULL;
			try{
				value = program.execute(env);

			}catch(Exception e){
				e.printStackTrace();
			}
			out.flushBuffer();
			out.free();
            os.flush();

            rc = os.toString("UTF-8");

        }catch(Exception e){
            e.printStackTrace();
        }
        return rc;
    }
}