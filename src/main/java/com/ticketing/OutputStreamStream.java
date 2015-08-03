package com.ticketing;

import java.io.*;
import com.caucho.vfs.*;


class OutputStreamStream extends StreamImpl {

      OutputStream _out;

      OutputStreamStream(OutputStream out) {
         _out = out;
      }

      /**
       * Returns true if this is a writable stream.
       */
      @Override
      public boolean canWrite() {
         return true;
      }

      /**
       * Writes a buffer to the underlying stream.
       *
       * @param buffer the byte array to write.
       * @param offset the offset into the byte array.
       * @param length the number of bytes to write.
       * @param isEnd true when the write is flushing a close.
       */
      @Override
      public void write(byte[] buffer, int offset, int length, boolean isEnd)
              throws IOException {
         _out.write(buffer, offset, length);
      }
   }
