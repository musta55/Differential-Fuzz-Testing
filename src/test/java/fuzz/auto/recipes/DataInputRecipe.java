package fuzz.auto.recipes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * DataInput: the engine used to build an empty DataInputStream, so the first read threw
 * EOFException (AbstractWritableAdapter.readFields never got past readInt()). Most of the time
 * this returns a well-formed [length][serialized Map] record, the format readFields expects;
 * otherwise raw fuzz bytes, for the malformed-input path.
 */
final class DataInputRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    ByteArrayOutputStream record = new ByteArrayOutputStream();
    // Raw bytes on 3, not 0: once the input is used up every value reads as 0, and the
    // well-formed record must be what an exhausted input gets.
    if (data.consumeInt(0, 3) == 3) {
      record.write(data.consumeBytes(64));
    } else {
      HashMap<String, Object> map = new HashMap<>();
      int n = data.consumeInt(0, 3);
      for (int i = 0; i < n; i++) {
        map.put(data.consumeAsciiString(8), data.consumeInt());
      }
      ByteArrayOutputStream serialized = new ByteArrayOutputStream();
      try (ObjectOutputStream out = new ObjectOutputStream(serialized)) {
        out.writeObject(map);
      }
      DataOutputStream out = new DataOutputStream(record);
      out.writeInt(serialized.size());
      out.write(serialized.toByteArray());
    }
    return new DataInputStream(new ByteArrayInputStream(record.toByteArray()));
  }
}
