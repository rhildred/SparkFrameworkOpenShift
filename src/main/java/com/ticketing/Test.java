package com.ticketing;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


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
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StdoutStream;
import com.caucho.vfs.StreamImpl;
import com.caucho.vfs.StringPath;
import com.caucho.vfs.WriteStream;
 

public class Test 
{
    public static void main(String[] args) {
        try{
            Path path = new StringPath("<?php return $attributes->get(\"message\"); ?>");
            QuercusContext quercusContext = new QuercusContext();
            ReadStream reader = path.openRead();
            QuercusProgram program = QuercusParser.parse(quercusContext, null, reader);
            WriteStream out = new WriteStream(StdoutStream.create());
            QuercusPage page = new InterpretedPage(program);

            Env env = new Env(quercusContext, page, out, null, null);

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");
            
            
            JavaClassDef classDef = env.getJavaClassDefinition(attributes.getClass());
            env.setGlobalValue("attributes", new JavaValue(env, attributes, classDef));

            Value value = NullValue.NULL;

            value = program.execute(env);

            out.flushBuffer();
            out.free();

            System.out.println(value);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}